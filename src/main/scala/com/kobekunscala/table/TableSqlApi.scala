package com.kobekunscala.table

import org.apache.flink.api.scala._
import org.apache.flink.table.api.TableEnvironment
import org.apache.flink.types.Row

object TableSqlApi {

  def main(args: Array[String]): Unit = {

    val env = ExecutionEnvironment.getExecutionEnvironment
    //设置tableapi环境变量
    val tabEnv = TableEnvironment.getTableEnvironment(env)

    val filepath = "C:\\Users\\mouse\\Desktop\\sales.csv"

    //参数中并不是所有字段全写, 返回值 DataSet
    val csv = env.readCsvFile[SalesLog](filepath,ignoreFirstLine = true)

//    csv.print()

    // 返回值Table  DataSet => Table
    val salesTable = tabEnv.fromDataSet(csv)

    //注册表
    tabEnv.registerTable("sales",salesTable)
    //sql查询 返回值为Table
    val tableResult = tabEnv.sqlQuery("select customerId,sum(amountPaid) money from sales group by customerId")

    //表转换成DataSet进行打印
    tabEnv.toDataSet[Row](tableResult).print()

  }

}
