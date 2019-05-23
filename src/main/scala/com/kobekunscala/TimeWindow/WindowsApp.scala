package com.kobekunscala.TimeWindow

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

object WindowsApp {


  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val text = env.socketTextStream("localhost",9999)

    text.flatMap(_.split(","))
      .map((_,1))
      .keyBy(0)
//      .timeWindow(Time.seconds(5))  //指定固定时间的滚动窗口
      //窗口大小是10s，滑动时间是5s,下面指定滑动窗口
      .timeWindow(Time.seconds(10),Time.seconds(5))
//      指定固定元素的窗口
//      .countWindow(5)
      .sum(1)
      .print()
      .setParallelism(1)

    env.execute("WindowsApp")

    //滑动窗口   只出现两个窗口的数据 这个我理解  但是不理解的是为什么数据一定会出现在第2个窗口?
  }
}

//(a,6)
//(b,4)
//(c,2)
//(a,6)
//(b,4)
//(c,2)
//(a,6)
//(c,2)
//(b,4)
//(b,4)
//(a,6)
//(c,2)