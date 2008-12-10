package com.razie.pub.test

import com.razie.pub.data._
import com.razie.pub.base._
import junit.framework._

object App extends Application {
  junit.textui.TestRunner.run(classOf[TestTestTripleTT])
}

/** TODO proper junit */
class TestTestTripleTT extends TestCase {

  def testWithInt = {
    val idx = new TripleIdx[Int,Int,Int]
  
   idx.put(1,11,111)
   idx.put(1,12,122)
 
   println("A:"+idx.get1k(1))
   Assert.assertTrue(idx.get1k(1)==List(11,12))
   println("C:"+idx.get2(1,11))
   Assert.assertTrue(idx.get2(1,11) == Some(111))
  }
  
  private[this] implicit def str2ActionI (s:String) : ActionItem = new ActionItem (s)
    
  def testActionItem = {
    val idx = new TripleIdx[String,ActionItem,Int]
  
   idx.put("Movie","organize",111)
   idx.put("Movie","moveto",122)
 
   println("AA:"+idx.get1k("Movie"))
   //Assert.assertTrue(idx.get1k("Movie")==List("organize", "moveto"))
   println("AC:"+idx.get2("Movie","organize"))
   //Assert.assertTrue(idx.get2("Movie","organize") == Some(111))
  }
}