package com.kobekunscala.batch

import org.apache.commons.io.FileUtils
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala._
import org.apache.flink.configuration.Configuration

object DistributedCache {


  def main(args: Array[String]): Unit = {

    val env = ExecutionEnvironment.getExecutionEnvironment

    val filepath = "C:\\Users\\mouse\\Desktop\\hello.txt"

    //1、注册一个本地\HDFS文件
    env.registerCachedFile(filepath, "kk-scala-dc")

    val data = env.fromElements("hadoop", "spark", "flink", "pyspark", "storm")

    data.map(new RichMapFunction[String,String] {

      override def open(parameters: Configuration): Unit = {

        //2、在open方法中获取到分布式缓存的内容即可
        val dcFile = getRuntimeContext()
          .getDistributedCache
          .getFile("kk-scala-dc")

        //返回值为java.util.List 不能用于下面增强的for循环，需转换成scala的List
        val lines = FileUtils.readLines(dcFile)
        import scala.collection.JavaConverters._

        val linesscala = lines.asScala

        for(ele <- linesscala){
          println(ele)
        }
      }
      override def map(value: String): String ={
        value
      }

    }).print
  }
}
