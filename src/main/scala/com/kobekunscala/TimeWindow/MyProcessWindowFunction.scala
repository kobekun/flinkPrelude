package com.kobekunscala.TimeWindow

import java.lang

import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

//https://www.jianshu.com/p/a7bf47b40147
class MyProcessWindowFunction
  extends ProcessWindowFunction[(Int,Int),String,Int,TimeWindow]{

  //在调用的类中new MyProcessWindowFunction一直报错是因为ProcessWindowFunction包导的不对
//  scala应该是：org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
//  而java用的是：org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction
  override def process(key: Int,
                       context: Context,
                       elements: Iterable[(Int, Int)],
                       out: Collector[String]): Unit = {

      println("~~~~~~~~~~~~~~~~~")

      var count = 0L

      for(in <- elements){
        count = count + 1
      }

      out.collect(s"Window: ${context.window} ,count: $count")
  }
}

