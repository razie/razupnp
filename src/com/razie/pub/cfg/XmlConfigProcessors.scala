package com.razie.pub.cfg

import com.razie.pub.base._

/** simple registry for XML tag processors */
@NoStaticSafe
object XmlConfigProcessors {
   def inst () = NoStaticS.get[XmlConfigProcessors] match {
         case Some (p) => p
         case None => NoStaticS.put[XmlConfigProcessors](new XmlConfigProcessors)
      }
                                                                         
   def put (tag:String, processor:XmlConfigProcessor) = inst.map.put (tag, processor)      
   
   def contains (tag:String) = inst.map.contains (tag)      

   /** process an element: find its processor and pass it on */
   def eat (e:org.w3c.dom.Element) = inst.map.get(e.getTagName()) match {
         case Some(p) => p.eat (e)
         case None => throw new IllegalStateException ("No processors for tag <"+e.getTagName+">")
      }
}

/** most likely should remove this class... */
@NoStaticSafe
class XmlConfigProcessors {
   val map = new collection.mutable.HashMap[String,XmlConfigProcessor]
}
