package com.kobekunscala.stream

import com.kobekunjava.stream.Student
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.streaming.api.scala._

object ScalaCustomSinkToMysql {


  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val source = env.socketTextStream("localhost",7777)

    val student = source.map(new RichMapFunction[String,Student] {

      override def map(value: String): Student = {

        val stu = new Student()

        val splits = value.split(",")

        stu.setId(splits(0).toInt)

        stu.setName(splits(1))

        stu.setAge(splits(2).toInt)

        stu
      }
    })

    student.addSink(new SinkToMySql).setParallelism(1)

    env.execute("ScalaCustomSinkToMysql")
  }
}
