package com.kobekunjava.simpleExample;


import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * 数据来源于socket的flink streaming应用程序 ---> java
 *
 * 需要安装netcat工具
 * 工具：https://blog.csdn.net/qq_37585545/article/details/82250984
 * 使用：https://blog.csdn.net/Zhanglihe992/article/details/78626247
 *
 */

public class StreamingWCJava {


    public static void main(String[] args) throws Exception{

        //1、准备执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //2、从socket中读取数据
        DataStreamSource<String> text = env.socketTextStream("localhost",9999);

        //3、转换  流处理中是keyBy，批处理中是groupBy

        text.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {

            public void flatMap(String value, Collector<Tuple2<String, Integer>> collector) throws Exception {
                String[] tokens = value.toLowerCase().split("\t");

                for (String token: tokens) {
                    if(token.length() > 0){
                        collector.collect(new Tuple2<String, Integer>(token, 1));
                    }
                }
            }
        }).keyBy(0)
          .timeWindow(Time.seconds(5))
          .sum(1)
          .setParallelism(1).print();

        env.execute("StreamingWCJava");
    }
}
