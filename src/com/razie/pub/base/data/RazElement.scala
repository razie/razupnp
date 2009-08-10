package com.razie.pub.base.data

import com.razie.pub.base.data._

import org.w3c.dom._

/** companion object to define the implicit conversion below */
object RazElement {
  implicit def toraz (e:org.w3c.dom.Element) : RazElement = 
    new RazElementJava (e)
  
  implicit def torazdoc (e:XmlDoc) : RazElement = 
    new RazElementJava (e e)
  
  implicit def torazse (e:scala.xml.Elem) : RazElement = 
    new RazElementScala (e)
 
  // TODO nicer
  implicit def tomap[A,B] (m:java.util.Map[A,B]) : scala.collection.mutable.Map[A,B] = {
    val ret = new scala.collection.mutable.HashMap[A,B]
    val iter = m.entrySet.iterator
    while (iter.hasNext) { 
      val e = iter.next
      ret.put (e.getKey, e.getValue)
      }
    ret
  }
  
  implicit def toarray[A](ij:java.util.List[A]) : Array[A] = {
    val l:Array[A] = new Array[A](ij.size)

  //TODO optimize this or even remove it
    for (i <- 0 to ij.size-1)
      l(i) = ij.get(i)
    
    l
  }
  
  //TODO optimize this or even remove it HOW???
  implicit def tolist[A](ij:java.util.List[A]) : List[A] = {
    val l:Array[A] = ij

    l.toList
  }
  
   implicit def toRazList[T](enumeration:java.util.List[T]):RazList[T] =
       new RazList(enumeration)
   
}

class RazList[T](enumeration:java.util.List[T]) extends Iterator[T] {
  val iter = enumeration.iterator
  def hasNext:Boolean =  iter.hasNext
  def next:T = iter.next
}

/** simplify xpath access to dom */
trait RazElement {

  /** get attribute value */
  def a (name:String) : String
 
  /** has attribute populated? */
  def ha (name:String) : Boolean
  
    /**
     * get attribute by path
     * 
     * i.e. "/config/mutant/@localdir"
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

/** simplify xpath access to dom */
class RazElementJava (val e:org.w3c.dom.Element) extends RazElement {

  def a (name:String) : String = e.getAttribute (name)
  
  def ha (name:String) = e.hasAttribute (name)
  
    /**
     * i.e. "/config/mutant/@localdir"
     * 
     * @param path identifies the xpath
     * @name identifies the name attribute of the element - could also be part of xpath instead
     * @return never null
     */
  def xpa (path:String) : String = XmlDoc.getAttr (e, path)
  
  def xpe (path:String) : RazElement = XmlDoc.getEntity (e, path)
  
  def xpl (path:String) : List[RazElement] = {
    // TODO optimize...
    val l = XmlDoc.listEntities (e, path)

    val lre : List[Element] = RazElement.tolist(l)
    
    for (val x <- lre) yield new RazElementJava(x)
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

/** simplify xpath access to dom */
class RazElementScala (val e:scala.xml.Elem) extends RazElement {

  def a (name:String) : String = (e \ name).text

  def ha (name:String) = e attribute name match {
    case None => false
    case _ => true
  }
  
  /** TODO implement */
  def xpa (path:String) : String = this a path
  
  /** TODO implement */
  def xpe (path:String) : RazElement = 
    xpl (path).first
  
  /** TODO implement */
  def xpl (path:String) : List[RazElement] = 
    e.elements.filter(_.label==path).map(x =>
      new RazElementScala(x.asInstanceOf[scala.xml.Elem])).toList
  
  def name : String = e label

    // TODO optimize
  def children : List[RazElement] = 
    e.elements.filter(_.isInstanceOf[scala.xml.Elem]).map(x =>
      new RazElementScala(x.asInstanceOf[scala.xml.Elem])).toList
  
  // TODO implement
  def nodeVal : String = {
      val ret:String = null;
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
