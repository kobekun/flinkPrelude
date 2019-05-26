package com.kobekunscala.connector

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

object KafkaConnectorConsumer {


  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val topic = "kobekuntest"

    val properties = new Properties()

    //必须配置idea所在机器上的 hostname和IP的映射关系
    // c:/windows/system32/drivers/etc找到hosts文件，打开hosts文件并在最后面添加一条记录
    properties.setProperty("bootstrap.servers", "192.168.137.12:9092")
    properties.setProperty("group.id", "test")

    val data = env.addSource(new FlinkKafkaConsumer[String](topic, new SimpleStringSchema(), properties))

    data.print()
    env.execute("KafkaConnectorConsumer")
  }
}
//此处可以消费上次socket中发过来的数据  ---> //myConsumer.setStartFromGroupOffsets()  // the default behaviour  *****
//val myConsumer = new FlinkKafkaConsumer08[String](...)
//myConsumer.setStartFromEarliest()      // start from the earliest record possible
//myConsumer.setStartFromLatest()        // start from the latest record
//myConsumer.setStartFromTimestamp(...)  // start from specified epoch timestamp (milliseconds)
