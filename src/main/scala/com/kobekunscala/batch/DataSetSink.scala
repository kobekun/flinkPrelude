package com.kobekunscala.batch

import org.apache.flink.api.scala._
import org.apache.flink.core.fs.FileSystem.WriteMode

object DataSetSink {

  def main(args: Array[String]): Unit = {

    val env = ExecutionEnvironment.getExecutionEnvironment

    val data = 1 to 10

    val text = env.fromCollection(data)

    //默认情况下 spark中sink-out指定的是文件夹，flink中指定的是文件
    val filepath = "C:\\Users\\mouse\\Desktop\\sink-out"

//    text.writeAsText(filepath) //没有东西输出 除非删除filepath

    //指定并行度后，sink-out为文件夹，文件分五个写入到目录下的文件中
    text.writeAsText(filepath, WriteMode.OVERWRITE).setParallelism(5)

    env.execute("DataSetSink")


  }
}
