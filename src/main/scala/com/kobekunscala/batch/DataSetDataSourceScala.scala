package com.kobekunscala.batch

import com.kobekunjava.batch.Person
import org.apache.flink.api.scala._

object DataSetDataSourceScala {

  def main(args: Array[String]): Unit = {

    val env = ExecutionEnvironment.getExecutionEnvironment

//    fromCollection(env)
//    textFile(env)
    csvFile(env)
  }

  def fromCollection(env: ExecutionEnvironment): Unit ={

    val data = 1 to 10
    env.fromCollection(data).print
  }

  def textFile(env: ExecutionEnvironment): Unit ={

    //可以读文件，也可以读文件夹  下面filepath可以写成文件夹
    val data = env.readTextFile("C:\\Users\\mouse\\Desktop\\hello.txt")
    data.print
  }

  def csvFile(env: ExecutionEnvironment): Unit ={

    val filepath = "C:\\Users\\mouse\\Desktop\\people.csv"
//    env.readCsvFile[(String,Int,String)](filepath,ignoreFirstLine=true).print

//    env.readCsvFile[(String,Int)](filepath,ignoreFirstLine = true).print

//    env.readCsvFile[(String,Int)](filepath,ignoreFirstLine = true,includedFields = Array(0,1)).print

    case class MyCaseClass(name: String, age: Int)

//    env.readCsvFile[MyCaseClass](filepath,ignoreFirstLine = true,includedFields = Array(0,1)).print

    //Array中的字段来自于Person，不能写错
    //java.lang.IllegalArgumentException: Field "work1" not part of POJO type com.kobekunjava.batch.Person

    env.readCsvFile[Person](filepath,ignoreFirstLine = true,pojoFields = Array("name","age","work")).print
  }
}
