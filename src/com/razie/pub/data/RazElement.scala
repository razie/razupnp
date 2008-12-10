package com.razie.pub.data

import com.razie.pub.draw._
import com.razie.pub.draw.widgets._
import com.razie.pub.base._
import com.razie.pub.base.data._

import org.w3c.dom._

/** companion class to define the implicit conversion below */
object RazElement {
  implicit def toraz (e:org.w3c.dom.Element) : RazElement = 
    new RazDomElement (e)
  
  implicit def xmldoc2razelem (e:XmlDoc) : RazElement = 
    new RazXmlDoc (e)
}

/** simplify xpath access to dom */
trait RazElement {

  def a (name:String) : String
  
  def xpa (path:String) : String
  
  def xpe (path:String) : Element
  
  def xpl (path:String) : List[Element]
}

/** simplify xpath access to dom */
class RazDomElement (val e:org.w3c.dom.Element) extends RazElement {

  implicit def jltoa[A](ij:java.util.List[A]) : Array[A] = {
    val l:Array[A] = new Array[A](ij.size)

    for (i <- 0 to ij.size-1)
      l(i) = ij.get(i)
    
    l
  }
  
  //TODO optimize this or even remove it
  implicit def jltol[A](ij:java.util.List[A]) : List[A] = {
    val l:Array[A] = ij

    l.toList
  }
  
  override def a (name:String) : String = e.getAttribute (name)
  
  override def xpa (path:String) : String = XmlDoc.getAttr (e, path)
  
  override def xpe (path:String) : Element = XmlDoc.getEntity (e, path)
  
  override def xpl (path:String) : List[Element] = XmlDoc.listEntities (e, path)
}

/** simplify xpath access to dom */
class RazXmlDoc (d:XmlDoc) extends RazDomElement (d.getDocument.getDocumentElement) {

  override def a (name:String) : String = throw new UnsupportedOperationException ("can't get attr of an xmldoc")
}
