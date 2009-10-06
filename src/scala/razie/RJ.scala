package scala.razie

import scala.collection._
import com.razie.pub.base.data._

/** conversions from java to scala collections
 * 
 * probably the thing I hate the most about scala: interacting with Java collections 
 * 
 * use like this: RJS(javalist).foreach -OR- RJS apply javalist foreach -OR- RJS list javalist sort 
 */
object RJS {
  def apply[A](ij:java.lang.Iterable[A]) : scala.collection.Iterable[A] = JavaConversions.asIterable(ij)
  
  def apply[A](ij:java.util.List[A]) : scala.collection.mutable.Buffer[A] = JavaConversions.asBuffer(ij)
  
  def list[A](ij:java.util.List[A]) : List[A] = JavaConversions.asBuffer(ij).toList
  
  def apply[A, B](ij : java.util.Map[A, B]) : scala.collection.mutable.Map[A,B] = JavaConversions.asMap(ij)
}

/** conversions from scala to java collections
 * 
 * probably the thing I hate the most about scala: interacting with Java collections 
 * 
 * use like this RSJ(scalalist)
*/
object RSJ {
   def apply[A](ij:scala.collection.Iterable[A]) : java.lang.Iterable[A] = JavaConversions.asIterable(ij)
   
   def apply[A](ij:scala.collection.mutable.Buffer[A]) : java.util.List[A] = JavaConversions.asList(ij)
   
//   def apply[A](ij:scala.List[A]) : java.util.List[A] = JavaConversions.asList(ij)
   
//   def apply[A, B](ij : scala.collection.mutable.Map[A, B]) : java.util.Map[A,B] = JavaConversions.asMap(ij)
}

/** xml conversions.
 * 
 *  This stuff here is more from my learning scala days, but i got used to the shortcuts...
 */
object RJX {
   def apply (e:org.w3c.dom.Element) : RazElement = new RazElementJava (e)
     
   def apply (e:XmlDoc) : RazElement = new RazElementJava (e e)
     
   def apply (e:scala.xml.Elem) : RazElement = new RazElementScala (e)
}


/** other useful conversions */
object RJ {
}
