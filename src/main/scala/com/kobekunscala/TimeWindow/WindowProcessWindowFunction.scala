package com.kobekunscala.TimeWindow

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

object WindowProcessWindowFunction {


  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val text = env.socketTextStream("localhost",9999)

    text.flatMap(_.split(","))//把输入的数字进行拆分
      .map(x => (1,x.toInt)) //map成tuple
      .keyBy(_._1) //取tuple中的第一个字段作为key
      .timeWindow(Time.seconds(5)) //选择5s的时间窗
      .process(new MyProcessWindowFunction()) //对输入的元素进行一次性处理
      .print()
      .setParallelism(1)

    env.execute("WindowProcessWindowFunction")
  }
}
