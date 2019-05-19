package com.kobekunscala.stream

import java.{lang, util}

import org.apache.flink.streaming.api.collector.selector.OutputSelector
import org.apache.flink.streaming.api.scala._

object DataStreamTransformation {


  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

//    filterFunction(env)

//    unionFunction(env)

    splitSelectFunction(env)

    env.execute("DataStreamTransformation")
  }

  def filterFunction(env: StreamExecutionEnvironment): Unit ={

    val data = env.addSource(new CustomeNonParallelSourceFunction).setParallelism(1)

    data.map(x => {
      println("recieved: " + x)
      x
    }).filter(_%2 == 0).print
  }

  def unionFunction(env: StreamExecutionEnvironment): Unit ={

    val data1 = env.addSource(new CustomeNonParallelSourceFunction).setParallelism(1)
    val data2 = env.addSource(new CustomeNonParallelSourceFunction).setParallelism(1)

    val data = data1.union(data2)

    data.print.setParallelism(1)
  }

  def splitSelectFunction(env: StreamExecutionEnvironment): Unit ={

    val data = env.addSource(new CustomeNonParallelSourceFunction).setParallelism(1)


    //split的数据类型是SplitStream
    val split = data.split(new OutputSelector[Long] {
      override def select(value: Long): lang.Iterable[String] = {

        val list = new util.ArrayList[String]()
        if(value % 2 == 0){
          list.add("even")
        }else{
          list.add("odd")
        }
        list
      }
    })

    //下面两种操作都不行，必须借助OutputSelector这个类的select方法进行拆分

//    val split = data.split(
//        (num: Int) =>
//          (num%2) match {
//            case 0 => List("even")
//            case 1 => List("odd")  //此处不能写成case 1: xxx
//          }
//      List("even","odd")
//
//    ).select("even")


//    val split = data.split((num: Int) =>{
//      val list = new util.ArrayList[String]()
//      if(num%2 == 0){
//        list.add("even")
//      }else{
//        list.add("odd")
//      }
//      list
//    })

    //select的返回值是DataStream
    val even = split select("even")

    val odd = split select("odd")

    val all = split select("even","odd")

//    even.print()
    odd.print()
//    all.print()

  }
}
