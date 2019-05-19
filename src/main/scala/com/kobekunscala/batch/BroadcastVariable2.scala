//package com.kobekunscala.batch
//
//import org.apache.flink.api.common.functions.{MapFunction, RichMapFunction}
//import org.apache.flink.api.scala._
//import org.apache.flink.configuration.Configuration
//
//import scala.collection.immutable.HashMap
//
//object BroadcastVariable2 {
//
//  def main(args: Array[String]): Unit = {
//
//    val env = ExecutionEnvironment.getExecutionEnvironment
//
//    val ds1 = env.fromElements("1","2","3","4")
//    val ds2 = env.fromElements("a","b","c","d")
//
//    ds1.map(new RichMapFunction[(String,(String,String))] {
//
//      var ds2: Traversable[String] = null
//
//      override def open(parameters: Configuration): Unit = {
//
//        import scala.collection.JavaConverters._
//         ds2 = getRuntimeContext.getBroadcastVariable[String]("bcast").asScala
//      }
//      override def map(key: String): (String,String) = {
//        var result: String = ""
//        for(bcastVariable <- ds2){
//          result = result + bcastVariable + " "
//        }
//        (key,result)
//      }
//    }).with
//  }
//}
