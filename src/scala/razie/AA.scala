/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package scala.razie

import com.razie.pub.base._

/** simplify usage of AttrAccess */
object AA {
   def apply (s:AnyRef*):AA = {val x = new AA(); x.setAttr(s:_*); x }
   def apply ():AA = new AA()

   /** simplify accessing attributes of asset classes 
    * 
    * @param s asset, of any class
    * @param name identifies the attribute
    * @return the attribute value, if we can find something yielding attribute values...
    */
   def a (s:Any, name:String):Option[AnyRef] = s match {
      case x:AttrAccess => Some(x a name)
      case x:HasAttrAccess => Some(x.attr a name)
      case _ => None // TODO 2-3 to try reflection...
   }
   
   /** simplify accessing attributes of asset classes 
    * 
    * @param s asset, of any class
    * @param name identifies the attribute
    * @return the attribute value, if we can find something yielding attribute values...
    */
   def sa (s:Any, name:String):Option[String] = a (s,name) match {
      case Some(null) => None
      case Some(x) => Some(x.toString)
      case _ => None 
   }
}

/** TODO 3-3 will add some scala niceties for AttrAccess usage 
 * 
 * TODO 3-3 scala has issues iwth inheriting embedded classes */
class AA extends AttrAccessImpl {
	
}