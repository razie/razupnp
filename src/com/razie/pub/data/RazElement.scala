package com.razie.pub.data

import com.razie.pub.base.data._

import org.w3c.dom._

/** companion class to define the implicit conversion below */
object RazElement {
  implicit def toraz (e:org.w3c.dom.Element) : RazElement = 
    new RazElement (e)
  
  implicit def torazdoc (e:XmlDoc) : RazElement = 
    new RazElement (e e)
}

/** simplify xpath access to dom */
class RazElement (val e:org.w3c.dom.Element) {

  implicit def jltoa[A](ij:java.util.List[A]) : Array[A] = {
    val l:Array[A] = new Array[A](ij.size)

  //TODO optimize this or even remove it
    for (i <- 0 to ij.size-1)
      l(i) = ij.get(i)
    
    l
  }
  
  //TODO optimize this or even remove it
  implicit def jltol[A](ij:java.util.List[A]) : List[A] = {
    val l:Array[A] = ij

    l.toList
  }
  
  def a (name:String) : String = e.getAttribute (name)
  
  def ha (name:String) = e.hasAttribute (name)
  
  def xpa (path:String) : String = XmlDoc.getAttr (e, path)
  
  def xpe (path:String) : RazElement = XmlDoc.getEntity (e, path)
  
  def xpl (path:String) : List[RazElement] = {
    // TODO optimize...
    val l = XmlDoc.listEntities (e, path)

    val lre : List[Element] = l
    
    val rere = for (val e <- lre) yield new RazElement(e)
    rere
    }
}
