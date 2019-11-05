package com.kobekunscala

object Xiebian {

  /**
    * 如果Father是Son父类，则Person[Father]也是Person[Son]父类，这就是协变
    * []里继承关系：Grandson->Son->Father
    * Person的继承关系Person[Grandson]->Person[Son]->Person[Father]
    */
    class Father
    class Son extends Father
    class Grandson extends Son
    class Person[+T] (val name: String) //+T协变
    def main(args: Array[String]): Unit = {
      def makeMoney(person:Person[Son]) {
        println(s"你是${person.name}，你应该去工作赚钱")
      }
      val father = new Person[Father]("father")
      val son = new Person[Son]("儿子")
      val grandson = new Person[Grandson]("孙子")
//          makeMoney(father) //  编译报错，父亲老了不需要赚钱
      makeMoney(son)
      makeMoney(grandson)
    }
}
