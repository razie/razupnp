package com.razie.pub.assets

import com.razie.pub.base._

object AssetMgrScala {
  
  def instance : AssetMgrTrait = 
    AssetMgr.instance().asInstanceOf[AssetMgrTrait]  
  
    /** inject/overwrite an action on a set of asset types */
  def inject (injected:AssetCmdInjector) : Unit = 
    instance inject injected

}