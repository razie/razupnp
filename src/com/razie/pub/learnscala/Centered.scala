package com.razie.pub.learnscala

object Kuk extends Application {
  val m = new java.util.HashMap[Int,String]()
  
}
trait Centered {
  val center:Point
  
  def offset (offsetBy:Point) = center += offsetBy
}

trait AnotherCentered {
  def center:Point
  def offset (offsetBy:Point) = center += offsetBy
}

class AnotherShape extends AnotherCentered {
  override val center:Point = new Point(0,0)
}