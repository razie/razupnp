package com.razie.pub.assets

import com.razie.pub.base._

/** this is abstract - can be implemented by Java code */
trait AssetCmdInjector {
  def entityTypes : Array[String]
  def actions : Array[ActionItem]
  
  def doAction (entityKey:AssetKey, entity:Referenceable, action:String, ctx:ScriptContext) : AnyRef
}
