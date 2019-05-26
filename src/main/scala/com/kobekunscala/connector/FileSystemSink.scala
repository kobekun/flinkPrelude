package com.kobekunscala.connector


import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.fs.StringWriter
import org.apache.flink.streaming.connectors.fs.bucketing.{BucketingSink, DateTimeBucketer}

object FileSystemSink {


  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val data = env.socketTextStream("localhost",9999)

    //要是一个文件夹，否则
    //Caused by: java.io.FileNotFoundException: Destination exists and
    // is not a directory: C:\Users\mouse\Desktop\kobekunSummary.txt
    val filepath = "C:\\Users\\mouse\\Desktop"

    //定义sink
    val sink = new BucketingSink[String](filepath)

    //设置桶文件写的地方
    //Sets the{@link Bucketer} to use for determining the bucket files to write to.
    sink.setBucketer(new DateTimeBucketer("yyyy-MM-dd--HHmm"))

    //设置writer流，以这样的流写进来的数据到桶中
    sink.setWriter(new StringWriter())

//    When a bucket part file is older than the roll over interval,
//    a new bucket part file is started and the old one is closed.
    //设置成桶时间间隔
    sink.setBatchRolloverInterval(2000)

    data.addSink(sink)


    //这样写出的文件太小

//    This will create a sink that writes to bucket files that follow this schema:
//
//      /base/path/{date-time}/part-{parallel-task}-{count}
    env.execute("FileSystemSink")
  }
}
