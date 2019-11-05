package com.kobekunscala

object Fanxinglei {

  class Stack[A] {
    private var elements: List[A] = Nil
    def push(x: A) { elements = x :: elements }
    def peek: A = elements.head
    def pop(): A = {
      val currentTop = peek
      elements = elements.tail
      currentTop
    }
  }
  // Stack 类的实现中接受类型参数 A。 这表示其内部的列表，var elements: List[A] = Nil，只能够存储类型 A 的元素。
  //方法 def push 只接受类型 A 的实例对象作为参数
  def main(args: Array[String]): Unit = {
    val stack = new Stack[Int]
    stack.push(1)
    stack.push(2)
    println(stack.pop)  // prints 2
    println(stack.pop)  // prints 1
  }
}
