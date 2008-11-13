package com.razie.pub.assets

import com.razie.pub.base._
import com.razie.pub.assets._
import scala.collection._
import com.razie.pub.data._

object FuckYou {
   final val ORGANIZE = new ActionItem("organize")
  }

/** got only abstract methods so it turns into a java interface... */
trait AssetMgrTrait {
  
  type fun = (AssetKey, Referenceable, String, ScriptContext) => AnyRef

  private[this] implicit def str2ActionI (s:String) : ActionItem = new ActionItem (s)
  
  val injectedActions = new TripleIdx[String,ActionItem,AssetCmdInjector]
  val injectedFun = new TripleIdx[String,ActionItem,fun]

    
  /** classic Java type injection: create subclass with definition */
  def inject (injected:AssetCmdInjector) : Unit = {
    for (meta <- injected.entityTypes; action <- injected.actions) 
      injectedActions.put(meta, action, injected)
  }
  
  /** new type inject */
  def inject (meta:String, action:ActionItem, injected:fun) : Unit = {
      injectedFun.put(meta, action, injected)
  }

  /** */
  def injection (t:String, cmd:String) : Option[fun] = {
    //injectedActions.get2 (t,new ActionItem(cmd)) match {
    injectedActions.get2 (t, new ActionItem(FuckYou.ORGANIZE.name)) match {
      case Some(x) => Some(x.doAction _)
      case None => injectedFun.get2(t,cmd)
      }
    //None
  }
  
  def injections (t:String) : Array[ActionItem] = {
    //var ret : Array[String] = Array()
    var ret : List[ActionItem] = List()
    
    ret = ret ++ injectedActions.get1(t)
    ret = ret ++ injectedFun.get1(t)
    
    ret.reverse.toArray
  }
}
