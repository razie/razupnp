package scala.razie

/** some logging basics 
 * 
 * @author razvanc
 */
object Log {
	   def logThis (msg:String, e:Throwable*) {
		      if (e.length > 0)
		         com.razie.pub.base.log.Log.logThis (msg, e(0)) 
		     else
		        com.razie.pub.base.log.Log.logThis (msg) 
		   }
		   
	   def alarmThis (msg:String, e:Throwable*) {
		      if (e.length > 0)
		         com.razie.pub.base.log.Log.alarmThis (msg, e(0)) 
		     else
		        com.razie.pub.base.log.Log.alarmThis (msg) 
		   }
		   
   def traceThis (f : => Any) = {
      if (com.razie.pub.base.log.Log.isTraceOn()) {
         val p = f
         p match {
            case s:String => com.razie.pub.base.log.Log.traceThis (s) 
            case (s:String,e:Throwable) => com.razie.pub.base.log.Log.traceThis (s,e) 
            case _ => com.razie.pub.base.log.Log.traceThis (p.toString)
         }
      }
   }
}
