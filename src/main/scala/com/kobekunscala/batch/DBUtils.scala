package com.kobekunscala.batch

import scala.util.Random

object DBUtils {


  def getConnection() ={

    new Random().nextInt(10) + ""
  }

  def returnConnection(conn: String): Unit ={

  }
}
