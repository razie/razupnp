package scala.razie

import com.razie.pub.base._
import com.razie.pub.agent._
import com.razie.pub.assets._
import com.razie.pub.comms._

/** syntax simplifier
 * 
 * <code>Service ("player") action ("play", AA ("movie", ref)) </code>
 * 
 * TODO junit
 */
object Service {
   def apply (location:AgentHandle, name:String) : ServiceHandle = 
      new ServiceHandle (location, name)
   def apply (name:String) : ServiceHandle = 
      new ServiceHandle (Agents.me(), name)
}

/** a service handle */
class ServiceHandle (val loc:AgentHandle, val name:String) {
	
	   /**
	    * create an action to invoke
	    * 
	    * @param method action/method name
	    * @param args
	    * @return an action-to-invoke
	    */
   def action (method:String, args:AttrAccess*) = 
	   if (loc == Agents.me)
	      new ServiceActionToInvoke(name, AI(method), args:_*)
	   else
	      new ServiceActionToInvoke(loc.url, name, AI(method), args:_*)
   
   /**
    * create an action to invoke
    * 
    * @param action action/method name
    * @param args
    * @return an action-to-invoke
    */
   def action (action:ActionItem, args:AttrAccess*) = {
	   if (loc == Agents.me)
	      new ServiceActionToInvoke(name, action, args:_*)
	   else
	      new ServiceActionToInvoke(loc.url, name, action, args:_*)
   }

   /**
    * create an action to invoke
    * 
    * @param method action/method name
    * @param args
    * @return an action-to-invoke
    */
   def action(method:String, args:AnyRef* ) : AnyRef = 
       action(method, (new AttrAccess.Impl(args)).asInstanceOf[AttrAccess]);

}