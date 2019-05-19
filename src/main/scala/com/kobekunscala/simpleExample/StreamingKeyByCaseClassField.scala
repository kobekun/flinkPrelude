package com.kobekunscala.simpleExample

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

object StreamingKeyByCaseClassField {

  /**
    * scala和java中的tuple对比：scala中tuple从1开始，java从0开始
    *
    * class WC(var complex: ComplexNestedClass, var count: Int) {
    * def this() { this(null, 0) }
    * }
    *
    * class ComplexNestedClass(
    *  var someNumber: Int,
    *  someFloat: Float,
    *  word: (Long, Long, String),
    *  hadoopCitizen: IntWritable) {
    *  def this() { this(0, 0, (0, 0, ""), new IntWritable(0)) }
    * }
    *
    * These are valid field expressions for the example code above:
    *
    * "count": The count field in the WC class.
    *
    * "complex": Recursively selects all fields of the field complex of POJO type ComplexNestedClass.
    *
    * "complex.word._3": Selects the last field of the nested Tuple3.
    *
    * "complex.hadoopCitizen": Selects the Hadoop IntWritable type.
    *
    * @param args
    */

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val text = env.socketTextStream("localhost",9999)

    text.flatMap(_.split("\t"))
      .map(x => WC(x, 1))
      .keyBy("word")
      .timeWindow(Time.seconds(5))
      .sum("count").print
      .setParallelism(1)

    env.execute("StreamingKeyByCaseClassField")
  }
}
