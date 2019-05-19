package com.kobekunscala.stream

import org.apache.flink.streaming.api.scala._

object DataStreamSocketDataSourceScala {


  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    socketFunction(env)

    env.execute("DataStreamSocketDataSourceScala")
  }

  def socketFunction(env: StreamExecutionEnvironment): Unit ={

    val data = env.socketTextStream("localhost",9999)

    //不能再data紧后面设置并行度，需要在print后面设置，不同地方设置效果不一样
    data.print.setParallelism(1)
  }
}
