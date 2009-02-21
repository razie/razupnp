package com.razie.pub.data.test

import com.razie.pub.data._
import com.razie.pub.base._
import com.razie.pub.base.data._
import com.razie.pub.assets._
import org.scalatest.junit._

/** testing the RazElement */
class TestRazElement extends JUnit3Suite {

   // using implicit conversion...
   val doc:RazElement= new XmlDoc().load("testrazelement", getClass().getResource("testing.xml"));
  
   def testA =
     expect ("roota") { doc a "name" }
   
   def testXpa =
     expect ("11") { doc xpa "/root/parent[@name='1']/child[@name='11']/@name" }
  
   def testXpe =
     expect ("11") { doc xpe "/root/parent[@name='1']/child[@name='11']" a "name" }
  
   def testXpl =
     expect (3) { doc xpl "/root/parent[@name='1']/child" size }
}

