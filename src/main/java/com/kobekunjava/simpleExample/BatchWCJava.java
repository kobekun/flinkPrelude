package com.kobekunjava.simpleExample;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * Created by mouse on 2019/5/2.
 *
 * 切换到工作空间
 *
 * 使用mvn快速创建一个flink的java项目
     mvn archetype:generate                               \
     -DarchetypeGroupId=org.apache.flink              \
     -DarchetypeArtifactId=flink-quickstart-java      \
     -DarchetypeVersion=1.8.0

 */
public class BatchWCJava {

    public static void main(String[] args) throws Exception {

        String input = "file:\\C:\\Users\\mouse\\Desktop\\text.txt";

        //1、获取运行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        //2、读文件
        DataSource<String> text = env.readTextFile(input);

//        text.print();
        //3、进行转换 transform
        text.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {

            public void flatMap(String value, Collector<Tuple2<String, Integer>> collector) throws Exception {
                String[] tokens = value.toLowerCase().split("\t");

                for (String token: tokens) {
                    if(token.length() > 0){
                        collector.collect(new Tuple2<String, Integer>(token, 1));
                    }
                }
            }
        }).groupBy(0).sum(1).print();
    }
}
