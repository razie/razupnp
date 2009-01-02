package com.razie.pub.assets

import com.razie.pub.base._
import com.razie.pub.base.data._
import com.razie.pub.assets._
import scala.collection._
import com.razie.pub.data._


/** got only abstract methods so it turns into a java interface... */
trait AssetMgrTrait {
  
    type fun = (AssetKey, Referenceable, String, ScriptContext) => AnyRef

    private[this] implicit def str2ActionI (s:String) : ActionItem = new ActionItem (s)
  
    val injectedActions = new TripleIdx[String,String, (ActionItem,AssetCmdInjector)]
    val injectedFun = new TripleIdx[String,ActionItem,fun]

    
    /** classic Java type injection: create subclass with definition */
    def inject (injected:AssetCmdInjector) : Unit = {
        for (meta <- injected.entityTypes; action <- injected.actions)
        injectedActions.put(meta, action.name, (action, injected))
    }
  
    /** new type inject */
    def inject (meta:String, action:ActionItem, injected:fun) : Unit = {
        injectedFun.put(meta, action, injected)
    }

    /** */
    def injection (t:String, cmd:String) : Option[fun] = {
        //injectedActions.get2 (t,new ActionItem(cmd)) match {
        injectedActions.get2 (t, cmd) match {
            case Some(x) => Some(x._2.doAction _)
            case None => injectedFun.get2(t,cmd)
        }
        //None
    }
  
    def injections (t:String) : Array[ActionItem] = {
        //var ret : Array[String] = Array()
        var ret : List[ActionItem] = List()
    
        val aa = injectedActions.get1v(t)
        
        for (x <- injectedActions.get1v(t))
          ret = x._1 :: ret

        ret = ret ++ injectedFun.get1k(t)
    
        ret.reverse.toArray
    }
}
