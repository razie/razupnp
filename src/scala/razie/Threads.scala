package scala.razie


object Threads {
   
   def run (f: =>Unit) {
      new java.lang.Thread(new java.lang.Runnable() {
         override def run() = {
            f
         }
      }).start();

   }
}
