/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.assets

import com.razie.pub.base._
import com.razie.pub.base.data._
import com.razie.pub.assets._
import scala.collection._
import com.razie.pub.base.data._


/** 
 * This adds scala niceties to the java AssetMgr, especially injections. You can inject new actions on assets as well as override existing actions
 * 
 * TODO NEED mechanism to call "super.action" when overriding...
 */
trait AssetMgrTrait {

   //-------------------associations
   def getAssocs() : List[MetaAssoc] = List() // TODO - use this, default is nothing
  
   //-------------------injections
   
   
   // You can inject these on-the-fly functions or AssetCmdInjector instances
   type InjectedFun = (AssetKey, Referenceable, String, ScriptContext) => AnyRef

   // tiny little helper
   private[this] implicit def str2ActionItem (s:String) : ActionItem = new ActionItem (s)

   // don't you just love it how we created a new Tuple type on the fly? I do!!!

   // these are Java-type injected objects
   val injectedActions = new TripleIdx[String,String, (ActionItem,AssetCmdInjector)]
   // these are Scala-type injected functions
   val injectedFun = new TripleIdx[String,ActionItem,InjectedFun]

   
   /** classic Java type injection: create subclass with definition */
   def inject (injected:AssetCmdInjector) : Unit = {
      for (meta <- injected.entityTypes; action <- injected.actions)
    	  injectedActions.put(meta, action.name, (action, injected))
   }
  
   /** scala type inject: function */
   def inject (meta:String, action:ActionItem, injected:InjectedFun) : Unit = {
      injectedFun.put(meta, action, injected)
   }

   /** find an injection */
   def injection (meta:String, cmd:String) : Option[InjectedFun] = {
      //injectedActions.get2 (meta,new ActionItem(cmd)) match {
      injectedActions.get2 (meta, cmd) match {
         case Some(x) => Some(x._2.doAction _)
         case None => injectedFun.get2(meta,cmd)
      }
      //None
   }

   /** list all injections for a meta - for display i guess */
   def injections (meta:String) : Array[ActionItem] = {
      //var ret : Array[String] = Array()
      var ret : List[ActionItem] = List()
    
      val aa = injectedActions.get1v(meta)
        
      for (x <- injectedActions.get1v(meta))
    	  ret = x._1 :: ret

      ret = ret ++ injectedFun.get1k(meta)
    
      ret.reverse.toArray
   }
   
}
