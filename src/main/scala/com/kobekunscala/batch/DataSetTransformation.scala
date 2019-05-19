package com.kobekunscala.batch

import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.scala._

import scala.collection.mutable.ListBuffer

object DataSetTransformation {

  def main(args: Array[String]): Unit = {

    val env = ExecutionEnvironment.getExecutionEnvironment

//    mapFunction(env)

//    filterFuntion(env)

//    mapPartitionFunction(env)

//    firstFunction(env)

//    flatMapFunction(env)

//    distinctFunction(env)

//    joinFunction(env)

//    outJoinFunction(env)

    crossFunction(env)
  }

  def mapFunction(env: ExecutionEnvironment): Unit ={

    val data = env.fromCollection(List(1,2,4,5,7))

//    data.map(x => x + 1).print

    //map作用在数据的每个元素之上
    data.map(_ + 1).print
  }

  def filterFuntion(env: ExecutionEnvironment): Unit ={

    val data = env.fromCollection(List(1,2,4,5,7))

    data.map(_ + 1).filter(_ > 4).print
  }

  def mapPartitionFunction(env: ExecutionEnvironment): Unit ={

    val students = new ListBuffer[String]

    for(i <- 1 to 100){
      students.append("student: " + i)
    }

    val data = env.fromCollection(students)//.setParallelism(4)

//    data.map(x => {
//
//      //每个元素存储到数据库中需要先获取连接
//      val connection = DBUtils.getConnection
//
//      println(connection + "...")
//
//      DBUtils.returnConnection(connection)
//
//    }).print

    //几个并行度，获取几个分区，一个分区得到一个连接

    //每个分区有一个迭代器，对分区中的元素进行转换
    data.mapPartition(x =>{
      val connection = DBUtils.getConnection()
      println(connection + "...")

      DBUtils.returnConnection(connection)

      x
    }).print()

  }

  def firstFunction(env: ExecutionEnvironment): Unit ={

    val info = ListBuffer[(Int, String)]()

    info.append((1, "Hadoop"))
    info.append((1, "Spark"))
    info.append((1, "Flink"))
    info.append((2, "Java"))
    info.append((2, "Spring Boot"))
    info.append((3, "Linux"))
    info.append((4, "VUE"))

    val data = env.fromCollection(info)

//    data.first(3).print

//    data.groupBy(0).first(2).print() //分组后取每组的前两个

    data.groupBy(0).sortGroup(1, Order.DESCENDING).first(2).print
  }

  def flatMapFunction(env: ExecutionEnvironment): Unit ={

    val info = ListBuffer[String]()

    info.append("hadoop,spark")
    info.append("hadoop,flink")
    info.append("flink,flink")

    val data = env.fromCollection(info)

//    data.map(_.split(",")).print
//    println("-----------------")
//    data.flatMap(_.split(",")).print

    data.flatMap(_.split(",")).map((_,1)).groupBy(0).sum(1).print
  }

  def distinctFunction(env: ExecutionEnvironment): Unit ={

    val info = ListBuffer[String]()

    info.append("hadoop,spark")
    info.append("hadoop,flink")
    info.append("flink,flink")

    val data = env.fromCollection(info)

    data.flatMap(_.split(",")).distinct().print()
  }

  def joinFunction(env: ExecutionEnvironment): Unit ={

    val info1 = ListBuffer[(Int, String)]()  // 编号  名字

    info1.append((1, "PK哥"))
    info1.append((2, "J哥"))
    info1.append((3, "小队长"))
    info1.append((4, "猪头呼"))

    val info2 = ListBuffer[(Int, String)]() //编号  城市

    info2.append((1, "西安"))
    info2.append((2, "广州"))
    info2.append((3, "北京"))
    info2.append((5, "上海"))


    val data1 = env.fromCollection(info1)

    val data2 = env.fromCollection(info2)

    data1.join(data2).where(0).equalTo(0).apply((first, second) => {
      (first._1,first._2,second._2)
    }).print()

  }

  def outJoinFunction(env: ExecutionEnvironment): Unit ={

    val info1 = ListBuffer[(Int, String)]()  // 编号  名字

    info1.append((1, "PK哥"))
    info1.append((2, "J哥"))
    info1.append((3, "小队长"))
    info1.append((4, "猪头呼"))

    val info2 = ListBuffer[(Int, String)]() //编号  城市

    info2.append((1, "西安"))
    info2.append((2, "广州"))
    info2.append((3, "北京"))
    info2.append((5, "上海"))

    val data1 = env.fromCollection(info1)

    val data2 = env.fromCollection(info2)

//    data1.leftOuterJoin(data2).where(0).equalTo(0).apply((first,second) => {
//
//      if(second == null){
//        (first._1,first._2,"-")
//      }else{
//        (first._1,first._2,second._2)
//      }
//    }).print

//    data1.rightOuterJoin(data2).where(0).equalTo(0).apply((first,second) => {
//
//      if(first == null){
//        (second._1,"-",second._2)
//      }else{
//        (first._1,first._2,second._2)
//      }
//    }).print

    data1.fullOuterJoin(data2).where(0).equalTo(0).apply((first,second) => {

      if(first == null){
        (second._1,"-",second._2)
      }else if(second == null){
        (first._1,first._2,"-")
      }else{
        (first._1,first._2,second._2)
      }
    }).print
  }

  def crossFunction(env: ExecutionEnvironment): Unit ={

    val info1 = List("曼联","曼城")
    val info2 = List(3,1,0)

    val data1 = env.fromCollection(info1)
    val data2 = env.fromCollection(info2)

    data1.cross(data2).print()
  }
}
