package scala.razie

import org.w3c.dom._
import com.razie.pub.agent._
import com.razie.pub.lightsoa._
import com.razie.pub.assets._
import com.razie.pub.assets.AssetMgr.Meta
import com.razie.pub.base.data._

/** describes an association between metas */
class MetaAssoc (
      val aEnd:String, val zEnd:String, val stereotype:String, 
      val aCard:String, val zCard:String, val aRole:String, val zRole:String) { 
}

object Metas {
   /** get a meta by name */
   def meta (name:String) = AssetMgr.meta(name)
   
   def assocsFrom (aEnd:String) = assocs.filter (_.aEnd == aEnd)
   def assocsTo (zEnd:String) = assocs.filter (_.zEnd == zEnd)
   
   def addAssoc (ma:MetaAssoc) = assocs.append(ma)
   
   val assocs = new collection.mutable.ListBuffer[MetaAssoc]()
   
   def addMeta (m:Meta) = 
         AssetMgr.instance().register(m)
}

trait SimpleXml {
   def ax (e:Element, name:String, dflt:String="") = 
      if (e.hasAttribute (name)) e.getAttribute(name) else dflt

   def a (e:RazElement, name:String, dflt:String="") = 
      if (e.ha(name)) e.a(name) else dflt
}

object Meta extends SimpleXml {
   def fromXml (e:RazElement) =
      new AssetMgr.Meta(
            scala.razie.AI cmdicon (a(e, "name"), a(e, "icon")), 
            "", a(e, "inventory"))
}

object MetaAssoc  extends SimpleXml {
   /** when defined by itself */
   def fromXml (e:RazElement) =
      new MetaAssoc (
            aEnd = a(e,"aEnd"),
            zEnd = a(e, "zEnd"),
            stereotype = a(e, "stereotype", "assoc"), 
            aCard = a(e, "aCard", "0"), 
            zCard = a(e, "zCard", "n"), 
            aRole = a(e, "aRole"), 
            zRole = a(e, "zRole")
            )
   
   /** when defined under a meta parent tag, which is the "aEnd" */
   def fromXml (e:RazElement, m:RazElement) =
      new MetaAssoc (
            aEnd = a(e,"aEnd", a(m, "name")),
            zEnd = a(e, "zEnd"),
            stereotype = a(e, "stereotype", "assoc"), 
            aCard = a(e, "aCard", "0"), 
            zCard = a(e, "zCard", "n"), 
            aRole = a(e, "aRole"), 
            zRole = a(e, "zRole")
            )
}