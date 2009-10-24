package scala.razie

import com.razie.pub.assets._
import com.razie.pub.base.ActionItem;
import com.razie.pub.base.AttrAccess;
import com.razie.pub.comms.ActionToInvoke;

object Asset {
   def apply (key:AssetKey) = new AssetHandle (key)
   def apply (key:String) = new AssetHandle (AssetKey.fromString(key))
   def apply (a:AssetBase) = if (a.isInstanceOf[AssetHandle]) 
      a.asInstanceOf[AssetHandle] 
      else new AssetHandle (a)
}

/**
 * this represents a full asset
 * 
 * TODO junit
 * 
 * @author razvanc99
 */
class AssetHandle (val key:AssetKey) extends AssetBase {
   var actual: AssetBase = null
  
   def this (a : AssetBase) = {
      this (a.getKey())
      actual=a
   }

   def brief = if (isResolved) actual.getBrief() else AssetMgr.brief(key)
   def getBrief = brief
   
   def resolve = if (actual == null) AssetMgr.getAsset(key) else actual
   def isResolved = actual != null
   def resolveIfLocal = if (isResolved || key.location.isLocal) resolve else this
   
   /**
    * convenience method - another form of invoke
    *
    * @param method action/method name
    * @param args
    * @return
    */
   def invoke(method:String, args:AnyRef* ) : AnyRef =
      invoke(method, (new AttrAccess.Impl(args)).asInstanceOf[AttrAccess]);

   /**
    * invoke a local/remote asset
    *
    * TODO implement sync/async flavors via a messaging service abstraction
    *
    * @param method action/method name
    * @param args
    * @return
    */
   def invoke(method:String , aa:AttrAccess ) : AnyRef =
      action (method, aa).act(null)

   /**
    * make an invocable action-to-invoke
    *
    * @param method action/method name
    * @param args
    * @return
    */
   def action(method:String , aa:AttrAccess ) : AssetActionToInvoke =
      if (key.getLocation() == null || (key.getLocation().isLocal() || !key.getLocation().isRemote()))
         new AssetActionToInvoke(key, new ActionItem(method), aa);
   else
      new AssetActionToInvoke(key.getLocation().toHttp(), key, new ActionItem(method), aa);

   override def getKey = key
}
