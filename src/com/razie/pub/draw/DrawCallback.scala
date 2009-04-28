package com.razie.pub.draw

import com.razie.pub.comms._
import com.razie.pub.base.ActionItem
import com.razie.pub.agent._
import com.razie.pub.base.log._

/** TODO FIXME this is not multi-thread safe... */
private object MyCache {
   final val MAXTIMEMSEC = 5 * 60 * 1000
   
   var counter:int = 1
   
   var cache : collection.mutable.Map[String,(Drawable,int, Long)] = 
     new collection.mutable.HashMap[String, (Drawable,int, Long)]()

   def put (screen:Drawable, validSec:int) : String = {
     clean
     val url:String = counter.toString
     counter+=1
     cache.put (url,(screen,validSec, System.currentTimeMillis()))
     url
     }
   
   def get (url:String) : Option[Drawable] = {
     clean
     cache.get(url) match {
       case Some((d, _, _)) => new Some(d)
       case None => None
       }
    }
   
   // TODO unit-test this
   def clean () = {
     // TODO only do this every like 1 min - or better, based on size, not more often, eh?
     val curt = System.currentTimeMillis
     cache.retain((x,y) => (curt - y._3 < MAXTIMEMSEC))
   }
   
   def clear () = cache.clear
   
   def keys = cache.keys
}

/** often you want different screens pre-built and invoked at a later time. 
 * This class allows you to register a pre-built screen and you get an invocable URL for that screen
 */
class DrawCallback (screen:Drawable, validSec:int) 
   extends ServiceActionToInvoke (classOf[DrawScreenService].getSimpleName, 
                                  DrawScreenServiceStatic.DRAW, "screen", 
                                  MyCache.put (screen,validSec)) {
     DrawScreenServiceStatic.autoRegister
}

class InternalDrawCallback (screenNo:String) 
   extends ServiceActionToInvoke (classOf[DrawScreenService].getSimpleName, 
                                  DrawScreenServiceStatic.DRAW, "screen", 
                                  screenNo) {
     DrawScreenServiceStatic.autoRegister
}
