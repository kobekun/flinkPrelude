package com.kobekunscala.batch

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala._
import org.apache.flink.configuration.Configuration

import scala.collection.mutable.ListBuffer

/**
  * Broadcast variables allow you to make a data
  * set available to all parallel instances of an operation
  *
  * 广播变量允许您使数据集可用于操作的所有并行实例
  */
object BroadcastVariable {

  def main(args: Array[String]): Unit = {

    val env = ExecutionEnvironment.getExecutionEnvironment

    val toBroadcast = env.fromElements(1,2,3)

    val data = env.fromElements("a", "b")

    data.map(new RichMapFunction[String, String] {

      //Traversable集合的接口
      var broadcastSet: Traversable[String] = null

      override def open(parameters: Configuration): Unit = {

        val allSet: ListBuffer[String] = null
        import scala.collection.JavaConverters._
        broadcastSet = getRuntimeContext().getBroadcastVariable[String]("broadcastSetName").asScala

        for(b <- broadcastSet){
          println(b)
        }
      }

      override def map(value: String): String = {
        value
      }
    }).withBroadcastSet(toBroadcast, "broadcastSetName").print
  }
}
