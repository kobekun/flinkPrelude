package com.kobekunjava.simpleExample;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

public class StreamingKeyByStringFieldJava {

    public static void main(String[] args) throws Exception {

        //1、准备执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //2、从socket中读取数据
        DataStreamSource<String> text = env.socketTextStream("localhost",9999);

//        text.print();
        //3、进行转换 transform
        text.flatMap(new FlatMapFunction<String, WC>() {

            public void flatMap(String value, Collector<WC> collector) throws Exception {
                String[] tokens = value.toLowerCase().split("\t");

                for (String token: tokens) {
                    if(token.length() > 0){
                        collector.collect(new WC(token,1));
                    }
                }
            }
        }).keyBy("word")
                .timeWindow(Time.seconds(5))
                .sum("count").print()
                .setParallelism(1);

        env.execute("StreamingKeyByStringFieldJava");
    }
}
