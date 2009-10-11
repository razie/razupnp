package scala.razie


object Threads {
   
   def run (f: =>Unit) : java.lang.Thread = {
      val thread = new java.lang.Thread(new java.lang.Runnable() {
         override def run() = {
            f
         }
      })
      thread.start()
      thread
   }
   
   def foreach[A] (as:Iterable[A]) (f:A =>Unit) : Iterable[java.lang.Thread] = {
	   val threads = for (a <- as) yield
	      new java.lang.Thread(new java.lang.Runnable() {
	         override def run() = {
	            f(a)
	         }
	      })
	   threads.foreach (_.start())
	   threads
	   }
   
   def join (threads:Iterable[java.lang.Thread]) = {
      threads.foreach (_.join)
   }
}
