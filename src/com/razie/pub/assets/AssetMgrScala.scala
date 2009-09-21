/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.assets

import com.razie.pub.base._
import com.razie.pub.base.data._

/** 
 * this should be used by scala code, to get the scala version...assuming the current one is indeed a scala version
 */
object AssetMgrScala {
  
  def instance : AssetMgrTrait = 
    AssetMgr.instance().asInstanceOf[AssetMgrTrait]  
  
    /** inject/overwrite an action on a set of asset types */
  def inject (injected:AssetCmdInjector) : Unit = 
    instance inject injected

    // TODO move this to an instance?
   /** list all assets of the given type at the given location */
   def find(ttype:String, env:AssetLocation, recurse:Boolean)
   :scala.collection.mutable.Map[AssetKey, AssetBrief] = {
      RazElement.tomap (AssetMgr.find (ttype, env,recurse))
//      scala.collection.jcl.Conversions.convertMap(AssetMgr.find (ttype, env,recurse))
   }
}
