package com.razie.pub.base

object NoStaticS {

   /**
    * create a static for the current thread for the given class
    * 
    * @param o
    *            the instance to use in this and related threads
    * @return the same object you put in
    */
   def put[A] (o:A)(implicit m:scala.reflect.Manifest[A]) : A = 
      NoStatics.put(m.erasure, o).asInstanceOf[A]

   /**
    * get the instance/static for this thread of the given class on this thread
    */
   def get[A](implicit m:scala.reflect.Manifest[A]) : Option[A] = {
      val cl = NoStatics.get(m.erasure)
      if (cl != null) Some(cl.asInstanceOf[A]) else None
   }

}
