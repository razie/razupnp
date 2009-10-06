/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.assets

import com.razie.pub.base._

/** these inject actions on some entity types: they enumerate the entity types supported and the actions injected and then can execute the actions.
 * 
 * this is abstract - can be implemented by Java code. In Scala you'd normally inject functions, no need to create new classes/objects
 */
trait AssetCmdInjector {
   def entityTypes : Array[String]
   def actions : Array[ActionItem]
  
   def doAction (entityKey:AssetKey, entity:Referenceable, action:String, ctx:ScriptContext) : AnyRef
}
