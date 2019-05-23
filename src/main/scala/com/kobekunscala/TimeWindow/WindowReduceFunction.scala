package com.kobekunscala.TimeWindow

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

object WindowReduceFunction {


  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val text = env.socketTextStream("localhost",9999)

    text.flatMap(_.split(","))
      .map(x => (1,x.toInt))
      .keyBy(0) //因为key是1，所有的元素都到一个task去执行
      .timeWindow(Time.seconds(5))
      //不是等待窗口中所有的数据进行一次性处理，而是数据两两处理   增量的过程
      .reduce((v1,v2) => {

        println(v1 + "..." + v2)

        (v1._1,v1._2 + v2._2)

      })
      .print()
      .setParallelism(1)

    env.execute("WindowsApp")
  }
}
//nc.exe -l -p 9999
//2,3,4,5,6
//
//(1,2)...(1,3)
//(1,5)...(1,4)
//(1,9)...(1,5)
//(1,14)...(1,6)
//(1,20)
