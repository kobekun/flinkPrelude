package com.kobekunscala

object Fanxinghanshu {

  def main(args: Array[String]): Unit = {
    def getfiled[T](content: T) = {
      content match {
        case a:Int =>println("这是Int类型")
        case a:String =>println("这是Stringt类型")
        case _ =>println("这是未知类型")
      }
    }
    getfiled("2")       //通过给泛型声明的变量传递值来让scala自动推断泛型的实际类型。
  }
}
