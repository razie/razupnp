/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.assets

import com.razie.pub.base._

/** 
 * this should be used by scala code, to get the scala version...assuming the current one is indeed a scala version
 */
object AssetMgrScala {
  
  def instance : AssetMgrTrait = 
    AssetMgr.instance().asInstanceOf[AssetMgrTrait]  
  
    /** inject/overwrite an action on a set of asset types */
  def inject (injected:AssetCmdInjector) : Unit = 
    instance inject injected

}