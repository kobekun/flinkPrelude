package com.kobekunscala.simpleExample

import org.apache.flink.api.scala._

/**
  * Created by mouse on 2019/5/2.
  *
  * flink批处理应用程序
  *
  * 在指定的工作空间
  * 使用mvn快速创建一个flink的 scala项目
      *mvn archetype:generate                               \
      *-DarchetypeGroupId=org.apache.flink              \
      *-DarchetypeArtifactId=flink-quickstart-scala     \
      *-DarchetypeVersion=1.8.0
  *
  */
object BatchWCScala {

  def main(args: Array[String]) {

    val input = "file:\\C:\\Users\\mouse\\Desktop\\text.txt"

    val env = ExecutionEnvironment.getExecutionEnvironment

    val data = env.readTextFile(input)

//    data.print  scala的精简之处

    data.flatMap(_.toLowerCase.split("\t"))
      .filter(_.nonEmpty)
      .map((_,1))
      .groupBy(0)
      .sum(1).print

  }
}
