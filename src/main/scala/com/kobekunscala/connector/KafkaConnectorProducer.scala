package com.kobekunscala.connector


import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchemaWrapper

object KafkaConnectorProducer {


  def main(args: Array[String]): Unit = {


    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //checkpoint常用参数设置
    env.enableCheckpointing(5000)
    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
    env.getCheckpointConfig.setCheckpointTimeout(10000)
    env.getCheckpointConfig.setMaxConcurrentCheckpoints(1)

    //从socket接受数据
    val dataFromSocket = env.socketTextStream("localhost", 9999)

    val topic = "kobekuntest"
    val serialization = new KeyedSerializationSchemaWrapper[String](new SimpleStringSchema())
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "192.168.137.12:9092")

    //通过flink将数据sink到kafka
//    val kafkaSink = new FlinkKafkaProducer[String](topic, serialization, properties)

    val kafkaSink = new FlinkKafkaProducer[String](topic, serialization,
      properties, FlinkKafkaProducer.Semantic.EXACTLY_ONCE)

    dataFromSocket.addSink(kafkaSink)
    env.execute("KafkaConnectorConsumer")
  }
}
//Semantic.EXACTLY_ONCE: uses Kafka transactions to provide exactly-once semantic.
//Whenever you write to Kafka using transactions,
//do not forget about setting desired isolation.level (read_committed or read_uncommitted
//- the latter one is the default value) for any application consuming records from Kafka.

//无论何时使用事务写入Kafka，都不要忘记为任何使用Kafka记录的应用程序设置所需的isolation.level
////（read_committed或read_uncommitted  - 后者是默认值）。

//Semantic.EXACTLY_ONCE mode relies on the ability to commit transactions that were started
//before taking a checkpoint, after recovering from the said checkpoint.
//If the time between Flink application crash and completed restart is larger than
//Kafka’s transaction timeout there will be data loss (Kafka will automatically abort transactions
//that exceeded timeout time).
//Having this in mind, please configure your transaction timeout appropriately to your expected down times.

//Semantic.EXACTLY_ONCE模式依赖于在从所述检查点恢复之后提交在获取检查点之前启动的事务的能力。
//如果Flink应用程序崩溃和完成重新启动之间的时间大于Kafka的事务超时，
//则会丢失数据（Kafka将自动中止超出超时时间的事务）。
//考虑到这一点，请根据预期的停机时间适当配置您的交易超时。

//Kafka brokers by default have transaction.max.timeout.ms set to 15 minutes.
//This property will not allow to set transaction timeouts for the producers larger
//than it’s value. FlinkKafkaProducer011 by default sets the transaction.timeout.ms
//property in producer config to 1 hour, thus transaction.max.timeout.ms
//should be increased before using the Semantic.EXACTLY_ONCE mode.

//默认情况下，Kafka代理将transaction.max.timeout.ms设置为15分钟。
//此属性不允许为生产者设置大于其值的事务超时。 默认情况下，
//FlinkKafkaProducer011将producer config中的transaction.timeout.ms属性设置为1小时，
//因此在使用Semantic.EXACTLY_ONCE模式之前应该增加transaction.max.timeout.ms。