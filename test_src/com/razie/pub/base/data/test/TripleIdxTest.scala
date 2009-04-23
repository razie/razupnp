/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.base.data.test

import com.razie.pub.base._
import com.razie.pub.base.data._
import org.scalatest.junit._

/** testing the triple index */
class TripleIdxTest extends JUnit3Suite {

  def expectOneOf[T] (l:List[T], t:T) = {
    if (! l.contains(t)) 
      expect("expectedOneOf: " + l.mkString(",") ) {t}
  }

  def expectSameAs[T] (l1:List[T], l2:List[T]) = {
    for (val t <- l2)
    if (! l1.contains(t.toString)) 
      expect("expectedSameAs: " + l1.mkString(",") ) {l2.mkString(",")}
  }

  def testWithInt:Unit = {
    val idx = new TripleIdx[Int,Int,Int]
  
   idx.put(1,11,111)
   idx.put(1,12,122)
 
   println("A:"+idx.get1k(1))
   expect (List(11,12)) {idx.get1k(1)}

   println("C:"+idx.get2(1,11))
   expect (Some(111)) {idx.get2(1,11)}
  }
  
  private[this] implicit def str2ActionI (s:String) : ActionItem = new ActionItem (s)
    
  def testActionItem = {
    val idx = new TripleIdx[String,ActionItem,Int]
  
   idx.put("Movie","organize",111)
   idx.put("Movie","moveto",122)
 
   println("AA:"+idx.get1k("Movie"))
   expectSameAs(List("organize", "moveto"), idx.get1k("Movie"))
   
   println("AC:"+idx.get2("Movie","organize"))
   expect (Some(111)) {idx.get2("Movie","organize")}
  }
}
