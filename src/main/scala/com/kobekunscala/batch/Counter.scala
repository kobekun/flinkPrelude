package com.kobekunscala.batch

import org.apache.flink.api.common.accumulators.LongCounter
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala._
import org.apache.flink.configuration.Configuration
import org.apache.flink.core.fs.FileSystem.WriteMode

/**
  * 计数器编程：
  * 1、定义计数器
  * 2、注册计数器
  * 3、获取计数器
  */
object Counter {

  def main(args: Array[String]): Unit = {

    val env = ExecutionEnvironment.getExecutionEnvironment

    val data = env.fromElements("hadoop", "spark", "flink", "pyspark", "storm")

//    data.map(new RichMapFunction[String, Long]() {
//
//      var counter = 0l

//      override def map(value: String): Long = {
//
//        counter = counter + 1
//
//        println("counter: " + counter)
//
//        counter
//      }
//    }).setParallelism(3).print

//    data.print()

    val info = data.map(new RichMapFunction[String,String] {

      //1、定义计数器
      var counter = new LongCounter()


      //通过open方法，注册计数器 不管并行度为几，计数器都是进行累加的
      override def open(parameters: Configuration): Unit = {
        //2、注册计数器
        getRuntimeContext.addAccumulator("ele-counts-scala", counter)
      }
      override def map(value: String): String = {

        counter.add(1)

        value
      }
    })


    val filepath = "C:\\Users\\mouse\\Desktop\\sink-scala-counter-out"


    info.writeAsText(filepath, WriteMode.OVERWRITE).setParallelism(5)

    val jobResult = env.execute("Counter")


    //3、获取计数器
    val num = jobResult.getAccumulatorResult[Long]("ele-counts-scala")

    println("num: " + num)
  }
}
