package com.kobekunscala

object Nibian {

  /**
    * 逆变是Person将[]里面的继承关系逆转过来
    * []里继承关系：Grandson->Son->Father
    * Person的继承关系：Person[Grandson]<-Person[Son]<-Person[Father]
    */

    class Father
    class Son extends Father
    class Grandson extends Son
    class Person[-T] (val name: String) //-T逆变
    def main(args: Array[String]): Unit = {
      def makeMoney(person:Person[Son]) {
        println(s"你是${person.name}，你应该去工作赚钱")
      }
      val father = new Person[Father]("father")
      val son = new Person[Son]("儿子")
      val grandson = new Person[Grandson]("孙子")
      makeMoney(father)
      makeMoney(son)
//        makeMoney(grandson)  //  编译报错，孙子还小，不需要赚钱
    }
}
