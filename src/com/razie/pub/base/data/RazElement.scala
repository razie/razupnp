/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.base.data

import com.razie.pub.base.data._

import org.w3c.dom._

// TODO need to optimize these - i convert to RazE stuff all the time, no caching - should use a map for caching? is that useful?

/** xml conversions.
 * 
 *  This stuff here is more from my learning scala days, but i got used to the shortcuts...
 */
object RazElement {
      implicit def toraz (e:org.w3c.dom.Element) : RazElement = new RazElementJava (e)
     
      implicit def torazdoc (e:XmlDoc) : RazElement = new RazElementJava (e e)
     
      implicit def torazse (e:scala.xml.Elem) : RazElement = new RazElementScala (e)
}

/** simplify xpath access to dom. conversions in RJX */
trait RazElement {

   /** get attribute value */
   def a (name:String) : String
 
   /** has attribute populated? */
   def ha (name:String) : Boolean
  
   /**
    * get attribute by path
    *
    * i.e. "/config/mutant/@someattribute"
    *
    * @param path identifies the xpath
    * @return never null
    */
   def xpa (path:String) : String

   /** get child element by path */
   def xpe (path:String) : RazElement
  
   /** get child elements by path */
   def xpl (path:String) : List[RazElement]
 
   /** get tag's name */
   def name : String
  
   /** return the list of children */
   def children : List[RazElement]

   /** node's value - if any, for CDATA or alike */
   def nodeVal : String
}

/** simplify xpath access to dom. conversions in RJX  */
class RazElementJava (val e:org.w3c.dom.Element) extends RazElement {

   def a (name:String) : String = e.getAttribute (name)
  
   def ha (name:String) = e.hasAttribute (name)
  
   /**
    * i.e. "/config/mutant/@someattribute"
    *
    * @param path identifies the xpath
    * @name identifies the name attribute of the element - could also be part of xpath instead
    * @return never null
    */
   def xpa (path:String) : String = XmlDoc.getAttr (e, path)
  
   def xpe (path:String) : RazElement = XmlDoc.getEntity (e, path)
  
   def xpl (path:String) : List[RazElement] = {
//      val l = XmlDoc.listEntities (e, path)
//      val lre : scala.collection.mutable.Buffer[Element] = scala.collection.JavaConversions.asBuffer(l)
    
      for (val x <- razie.RJS list XmlDoc.listEntities (e, path))
         yield new RazElementJava(x)
   }
  
   def name : String = e.getNodeName
  
   def children : List[RazElement] = {
      val children = e.getChildNodes()
    
      val k = for {
         i <- 0 to children.getLength()-1
         x = children.item(i)
         if x.isInstanceOf[RazElement]
      }
      yield x.asInstanceOf[RazElement]

      // TODO i don't like this syntax
      k.toList
   }
  
   def nodeVal : String = RiXmlUtils.getOptNodeVal (e)
}

/** simplify xpath access to dom . conversions in RJX */
class RazElementScala (val e:scala.xml.Elem) extends RazElement {

   def a (name:String) : String =
      (e \ ((if (name.startsWith("@")) "" else "@")+name)).text

   def ha (name:String) = e attribute name match {
      case None => false
      case _ => true
   }
  
   /** TODO implement full xpath - for now use XP instead */
   def xpa (path:String) : String = this a path
  
   def xpe (path:String) : RazElement =
      xpl (path).first
  
   /** TODO implement full xpath - for now use XP instead */
   def xpl (path:String) : List[RazElement] =
      e.elements.filter(_.label==(path.replaceFirst("/",""))).map(x =>
         new RazElementScala(x.asInstanceOf[scala.xml.Elem])).toList
  
   def name : String = e label

   // TODO optimize
   def children : List[RazElement] =
      e.child.filter(_.isInstanceOf[scala.xml.Elem]).map(x =>
         new RazElementScala(x.asInstanceOf[scala.xml.Elem])).toList
  
   // TODO implement properly - i don't know if text covers CDATA as well
   def nodeVal : String = {
      val ret:String = e.text
//      if (e != null) {
//         for (Node child = elem.getFirstChild(); child != null; child = child.getNextSibling()) {
//            if (child.getNodeType() == Node.CDATA_SECTION_NODE || child.getNodeType() == Node.TEXT_NODE) {
//               ret = ret == null ? child.getNodeValue() : ret + child.getNodeValue();
//            }
//         }
//      }
      return ret;
   }
}
