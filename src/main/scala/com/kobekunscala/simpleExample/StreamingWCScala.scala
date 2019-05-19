package com.kobekunscala.simpleExample

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

/**
  * 数据来源于socket的flink streaming应用程序 --->scala
  *
  * 使用socket首先启动socket
  */

object StreamingWCScala {

  def main(args: Array[String]): Unit = {


    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val text = env.socketTextStream("localhost",9999)

    text.flatMap(_.split("\t"))
      .map((_,1))
      .keyBy(0)
      .timeWindow(Time.seconds(5))
      .sum(1)
      .setParallelism(1).print

    env.execute("StreamingWCScala")
  }
}
