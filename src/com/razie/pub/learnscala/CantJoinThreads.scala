package com.razie.pub.learnscala

/** if you remove the toList from the Range in the for loop, checkout the result */
object CantJoinThreads {
   
   def main (argv:Array[String]) = {
    val threads = Array.tabulate (5) (_=>new java.lang.Thread)
    println ("1 "+threads.mkString)
    println ("2 "+threads.mkString)
    println ("3 "+threads.mkString)
//  val result = repeatAndWait (5) {new Integer(4)}
//  println (">>>this should be all 4s>>> "+result.mkString)
}

   def othermain (argv:Array[String]) = {
         val threads = for (t <- 0 until 5) yield new java.lang.Thread()
         println ("1 "+threads.mkString)
         println ("2 "+threads.mkString)
         println ("3 "+threads.mkString)
//       val result = repeatAndWait (5) {new Integer(4)}
//       println (">>>this should be all 4s>>> "+result.mkString)
   }

   /** repeat a number of times on as many threads - but wait for all threads to finish */
    def repeatAndWait[A<:AnyRef] (i:Int) (f: => A) : Iterable[A] = {
            val threads = for (t <- scala.razie.Range.excl(0,i)) yield new java.lang.Thread() {
                var result : A = null
                override def run() = { 
                    this.result = f 
                    println("=="+this.result)
                }
            }
            
        threads.foreach (_.start)
        threads.foreach (_.join)
        println (threads.length + " threads done")
   
        val res = threads.map (_.result)
        res.toList
  }
}
