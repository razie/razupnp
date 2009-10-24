package com.razie.pub.assets

/** describes an association between metas 
 * 
 * TODO 1-1 do assocs need a namespace?
 * */
class MetaAssoc (
      val name:String,
      val aEnd:String, val zEnd:String, val stereotype:String, 
      val card:String="0..1-0..n", val aRole:String="", val zRole:String="") { 
   // TODO i need to implement the traceback: who injected/defined this association
   val traceback:String = null
}

/** this is a full specification of a meta: the meta itself together with all neccessary associations. Note that the associations can be only from and to the current meta
 * 
 * @param m the meta specification 
 * @param assocs all associations to and from the meta
 */
class MetaSpec (val meta:Meta, val assocs:List[MetaAssoc]){
   def this (m:Meta) = this (m, List())
}

/** implemented by all classes who know their metas - simplifies code Assets.manage(new MyAsset()) */
trait HasMeta {
   def metaSpec : MetaSpec
}
