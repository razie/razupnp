package scala.razie

import com.razie.pub.assets._
import com.razie.pub.base.ActionItem;
import com.razie.pub.base.AttrAccess;
import com.razie.pub.comms.ActionToInvoke;

object Asset {
   def apply (key:AssetKey) = new AssetHandle (key)
}

/**
 * this represents a full asset
 * 
 * TODO junit
 * 
 * @author razvanc99
 */
class AssetHandle (val key:AssetKey) extends Referenceable {

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
    def invoke(method:String , aa:AttrAccess ) : AnyRef = {
        // 1. figure out url to home agent
        // 2. invoke remote
        val action =  if (key.getLocation() == null || (key.getLocation().isLocal() || !key.getLocation().isRemote())) 
           new AssetActionToInvoke(key, new ActionItem(method), aa);
        else
            new AssetActionToInvoke(key.getLocation().toHttp(), key, new ActionItem(method), aa);
        
        action.act(null);
    }

    /**
     * invoke a local/remote asset
     * 
     * TODO implement sync/async flavors via a messaging service abstraction
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
