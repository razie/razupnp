   /**
    * Razvan's public code. 
    * Copyright 2008 based on Apache license (share alike) see LICENSE.txt for details.
    */
package com.razie.pub.assets

   import com.razie.pub.base.ActionItem;
   import com.razie.pub.base.AttrAccess;
   import com.razie.pub.comms.ActionToInvoke;
   
   /**
    * this represents a full asset
    * 
    * TODO find a GOOD use for this - I think we need it but not sure if it simplifies clients...
    * 
    * @author razvanc99
    */
class AssetHandle (val key:AssetKey) extends Referenceable {

       /**
        * convenience method - another form of invoke
        * 
        * @param method
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
        * 
        * @param method
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

       override def getKey = key
}
