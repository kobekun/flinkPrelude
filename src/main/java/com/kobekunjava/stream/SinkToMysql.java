package com.kobekunjava.stream;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SinkToMysql extends RichSinkFunction<Student> {

    Connection conn;

    PreparedStatement ps;

    private Connection getConnection()  {

        Connection connection = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/imooc_flink";

            connection = DriverManager.getConnection(url, "root", "881105");

        } catch (ClassNotFoundException e) {

            e.printStackTrace();

        } catch (SQLException e) {

            e.printStackTrace();

        }


        return connection;
    }


    @Override
    public void open(Configuration parameters) throws Exception {

        super.open(parameters);

        conn = getConnection();

        String sql = "insert into student(id,name,age) values(?,?,?)";

        ps = conn.prepareStatement(sql);

        System.out.println("open");
    }

    //每插入一次记录调用一次
    public void invoke(Student student,Context context){

        System.out.println("invoke...");

        try {
            //为前面的占位符赋值
            ps.setInt(1,student.getId());

            ps.setString(2,student.getName());

            ps.setInt(3,student.getAge());

            ps.executeUpdate();

        } catch (SQLException e) {

            e.printStackTrace();

        }


    }

    public void close() throws Exception{

        super.close();

        if(ps != null){

            ps.close();

        }
        if(conn != null){

            conn.close();

        }
    }
}