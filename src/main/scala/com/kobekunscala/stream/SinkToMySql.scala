package com.kobekunscala.stream

import java.sql.{Connection, DriverManager, PreparedStatement}

import com.kobekunjava.stream.Student
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction

class SinkToMySql extends RichSinkFunction[Student]{ //Student为输入的数据类型

  var conn:Connection = null

  var ps:PreparedStatement = null

  def getConnection(): Connection ={

    var connection: Connection = null

    Class.forName("com.mysql.jdbc.Driver")

    val url = "jdbc:mysql://localhost:3306/imooc_flink"

    connection = DriverManager.getConnection(url,"root","881105")

    connection
  }

  override def open(parameters: Configuration): Unit = {

    println("open")

    super.open(parameters)

    conn = getConnection()

    ps = conn.prepareStatement("insert into student(id,name,age) values (?,?,?);")

  }

  override def invoke(value: Student): Unit = {

    println("invoke...")

    ps.setInt(1,value.getId.toInt)

    ps.setString(2,value.getName)

    ps.setInt(3,value.getAge.toInt)

    ps.executeUpdate()
  }


  override def close(): Unit = {

    super.close()

    if(ps != null){
      ps.close()
    }

    if(conn != null){
      conn.close()
    }
  }
}
