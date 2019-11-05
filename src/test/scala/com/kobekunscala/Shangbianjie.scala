package com.kobekunscala

object Shangbianjie {

  class Person(val name: String){}
  class Teacher(name:String)
  class Student(name: String) extends Person(name)
  class Play[T <: Person](p: T) {}


  def main(args: Array[String]): Unit = {
    val wiki=new  Student("Wiki")
    val tom=new Teacher("Tom")
    val play =new Play(wiki) //[Person]可以省略自行推导
    //   val s=new Play(tom) 报错 因为Teacher不是Person子类。这就是上边界
  }
}


//为什么需要边界：在指定泛型类型的时候，有时，我们需要对泛型的类型的范围进行界定，而不是任意的类型
//
//上边界：[A<B]
//
//上边界特性：左边的类型参数A是右边类型B的子类
//
//作用：我们可能要求某个泛型，它就必须是某个类A的子类，这样在程序中就可以放心地调用A类的方法
//
//比如：我们并不知道类型T到底有没有compareTo方法，编译报错，所以要使用上边界，是参数类型T的类型是含有compareTo的类或者其子类
//
//编译报错：class Pair[T](val first:T, val second:T) {def smaller = if (first.compareTo(second)) }
//编译正确：class Pair[T <: Comparable[T]](val first:T, val second:T) {def smaller = if (first.compareTo(second)) }



//下边界：[A>B]
//下边界特性：左边的类型参数A是右边类型B父类
//注意：如果是在调用的时候省略了[T]，让scala自动去推断，
//scala会自动向上转型，使编译通过而不会报错（无论参数是什么类型都会编译通过）
