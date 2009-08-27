/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.base.data.test;

import org.scalatest.junit._
import com.razie.pub.base.data._
import org.scalatest.SuperSuite


object MyRun extends Application {

//  override def main(args: Array[String]) = {
//  }

}

/**
 * junit tests for the XP stuff
 * 
 * @author razvanc99
 */
class TestXpString extends JUnit3Suite {

 def test11a = expect (List("root")) {
  new XP("/root").xpl(new StringXqSolver, "/root")
  }

 def test11b = expect (List("root")) {
  new XP("/root").xpl(new StringXqSolver, "root")
  }

 def test12 = expect (List("s1")) {
  new XP("/root/s1").xpl(new StringXqSolver, "/root/s1")
  }

 def test21 = expect (List(("a",List(null)))) { s("/a","a") }
 def test22 = expect (List(("a",List(null)))) { s("a","a") }
 def test23 = expect (List(("a",List("/b")))) { s("/a/b","a") }
 def test24 = expect (List(("a",List("/b")))) { s("a/b","a") }

 def s(src:String,path:String) = new StringXqSolver().getNext((src,List(src)),path,null)
}

/**
 * junit tests for the XP stuff
 * 
 * @author razvanc99
 */
class TestXpScala extends JUnit3Suite {

 def test41 = expect (List("a")) { ssx("/a").map(_.label) }
 def test42 = expect (List("a")) { ssx("a").map(_.label) }
 def test43 = expect (List("b1","b2")) { ssx("/a/b").map(_ \ "@ba") }
 
 def ssx(path:String) = new XP[scala.xml.Elem] (path).xpl(new ScalaDomXqSolver, TXXmls.x) 
 
// def test31 = expect (List("a")) { sx("/a").map(_.name) }
// def test32 = expect (List("a")) { sx("a").map(_.name) }
// def test33 = expect (List("b1","b2")) { sx("/a/b").map(_ a "ba") }
// 
// def sx(path:String) = new XP[RazElement] (path).xpl(new DomXqSolver, TXXmls.x) 
}

object TXXmls {
  def x = {
    <a aa="a1">
      <b ba="b1">
        <c ca="c1"/>
        <c ca="c2"/>
        <c ca="c3"/>
      </b>
      <b ba="b2">
        <c ca="c1"/>
        <c ca="c2"/>
        <c ca="c3"/>
      </b>
    </a>
    }
}
