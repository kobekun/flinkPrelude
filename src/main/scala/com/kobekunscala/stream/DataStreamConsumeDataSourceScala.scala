package com.kobekunscala.stream

import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction
import org.apache.flink.streaming.api.scala._

object DataStreamConsumeDataSourceScala {


  /**
    * 自定义数据源  StreamExecutionEnvironment.addSource(sourceFunction)
    *
    * implementing the SourceFunction for non-parallel sources, or by
    * implementing the ParallelSourceFunction interface or
    * extending the RichParallelSourceFunction for parallel sources.
    *
    * org.apache.flink.streaming.api.scala._
    * org.apache.flink.api.scala._
    *
    * 隐式转换用到上面两个任意一个都可以
    *
    * @param args
    */
  def main(args: Array[String]): Unit = {


    val env = StreamExecutionEnvironment.getExecutionEnvironment

//    nonParallelDataSource(env)

//    parallelSourceFunction(env)

    richParallelSourceFunction(env)

    env.execute("DataStreamConsumeDataSourceScala")
  }

  def nonParallelDataSource(env: StreamExecutionEnvironment): Unit ={

    //这里并行度不能设置成除1以外的数，因为该源是没有并行度的源，并行度必须是1.详见代码

//    def setParallelism(parallelism: Int) = {
//      Preconditions.checkArgument(canBeParallel || parallelism == 1,
//      "The parallelism of non parallel operator must be 1.")
//      transformation.setParallelism(parallelism)
//      this
//    }

    val data = env.addSource(new CustomeNonParallelSourceFunction).setParallelism(1)

    data.print.setParallelism(1)
  }

  def parallelSourceFunction(env: StreamExecutionEnvironment): Unit ={

//    1> 1
//    3> 1
//    2> 2
//    4> 2
//    3> 3
//    1> 3
//    4> 4
//    2> 4

    val data = env.addSource(new CustomParallelSourceFunction).setParallelism(2)

    data.print
  }

  def richParallelSourceFunction(env: StreamExecutionEnvironment): Unit ={

    val data = env.addSource(new CustomRichParallelSourceFunction).setParallelism(3)

    data.print
  }
}
