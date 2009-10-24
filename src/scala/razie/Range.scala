package scala.razie

/** there, I fixed it! http://thereifixedit.com/ */
object Range {
   def excl (min:Int, max:Int) = new Range (min,max-1)
   def incl (min:Int, max:Int) = new Range (min,max)
}

class Range (min:Int, val max:Int) extends Iterator[Int] {
   var curr = min
   def hasNext:Boolean =  curr <= max
   def next:Int = { val oldcurr=curr; curr.+=(1); oldcurr}
}
