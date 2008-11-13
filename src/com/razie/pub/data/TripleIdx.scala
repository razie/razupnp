package com.razie.pub.data

import com.razie.pub.base._
import com.razie.pub.assets._
import scala.collection._

/** got only abstract methods so it turns into a java interface... */
class TripleIdx[A, B, C] {
  
  val idx = mutable.Map[A,mutable.Map[B,C]] ()

    def initMap (meta:A, ink:mutable.Map[A,mutable.Map[B, C]]):mutable.Map[B,C] = {
      val m = ink.get(meta)

      m match {
        case None => {
          val m:mutable.Map[B,C] = mutable.Map[B,C] ()
          ink.put (meta, m)
          m
        }
        case Some(x) => x
      }
    }

  /** new type inject */
  def put (a:A, b:B, c:C) : Unit = {
      initMap(a, idx).put (b, c)
  }

  /** */
  def get2 (a:A, b:B) : Option[C] = {
	if (idx.contains(a))
      idx.get(a).get.get(b)
    else 
      None
  }
  
  def get1 (a:A) : List[B] = {
//    var ret : List[B] = List()
    
	if (idx.contains(a)) 
	  Nil ++ idx.get(a).get.keySet
    else
      Nil   
  }
}
