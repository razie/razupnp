package com.razie.pub.test

import com.razie.pub.assets._
import com.razie.pub.base._
import org.scalatest.junit._
import razie.assets._
import razie.base._

// just to instantiate a trait
class SampleAssetMgrTrait extends AssetMgrInjector {
}

// injected sample - has 1 type and 2 actions
class SampleAJ extends AssetCmdInjection {
   val entityTypes = Array("Movie")
   val actions = Array(new ActionItem("organize"), new ActionItem("moveto"))
   
  def doAction (entityKey:AssetKey, entity:Referenceable, action:String, ctx:ScriptContext) : AnyRef = {
    println(action)
    "Ok."
  }
}

class TestAssetMgrTrait extends JUnit3Suite {
  
  type fun = (AssetKey, Referenceable, String, ScriptContext) => AnyRef
  private[this] implicit def str2ActionI (s:String) : ActionItem = new ActionItem (s)


  def testActionItem = {
   val am = new SampleAssetMgrTrait
 
   am.inject(new SampleAJ)
   
//   am.inject("Movie","organize",111)
   //am.inject("Movie","moveto",println ("moveto" + _ + _ + _ )))

  val aa = am.injections("Movie")
  println ("A: " + (aa mkString ","))
//   Assert.assertTrue(idx.get1k("Movie")==List("organize", "moveto"))
   //Assert.assertTrue(idx.get2("Movie","organize") == Some(111))
  }
}