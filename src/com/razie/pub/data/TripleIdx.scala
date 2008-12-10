package com.razie.pub.data

import com.razie.pub.base._
import com.razie.pub.assets._
import scala.collection._

/** a simple triple index: each A has a set of (B,C*) */
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

    /** get the C for (a,b)*/
    def get2 (a:A, b:B) : Option[C] = {
        if (idx.contains(a))
          idx.get(a).get.get(b)
        else
          None
    }
 
    /** get the list of B's for a*/
    def get1k (a:A) : List[B] = {
        if (idx.contains(a))
          Nil ++ idx.get(a).get.keySet
        else
          Nil
    }
    
    /** get the list of C's for a*/
    def get1v (a:A) : List[C] = {
        if (idx.contains(a)) {
          var ret:List[C] = List[C]()
          
          for (x<-idx.get(a).get.values)
            ret = x :: ret
          // TODO reverse may be useless since map doesn't preserve order?
          ret.reverse
          }
        else
          Nil
    }
}
