package com.kobekunjava.stream;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class JavaCustomSinkToMysql {


    public static void main(String[] args) throws Exception{

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> source = env.socketTextStream("localhost",7777);
                                                //.setParallelism(1);只设置这一个地方是没有用的

        SingleOutputStreamOperator<Student> studentSource = source.map(new MapFunction<String, Student>() {

            public Student map(String s) throws Exception {

                String[] splits = s.split(",");

                Student stu = new Student();

                stu.setId(Integer.parseInt(splits[0]));

                stu.setName(splits[1]);

                stu.setAge(Integer.parseInt(splits[2]));

                return stu;
            }
        });

        studentSource.addSink(new SinkToMysql()).setParallelism(1);//并行度在这里设置，这里决定open打印几次
        env.execute("JavaCustomSinkToMysql");
    }
}
//    open
//    open
//    open
//    open
// 启动程序后 打印结果是4个，默认情况和计算机的核数是一致的，可以设置并行度