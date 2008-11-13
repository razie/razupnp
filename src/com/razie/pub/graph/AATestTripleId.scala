package com.razie.pub.graph

import org.scalatest._
import com.razie.pub.data._

/** TODO proper junit */
class AATestTripleId extends Application {

  test1

  def test1 = {
    val idx = new TripleIdx[Int,Int,Int]
  
   idx.put(1,11,111)
   idx.put(1,12, 122)
 
   println("A:"+idx.get1(1))
   println("B:")
   println("C:"+idx.get2(1,11))
  }
}