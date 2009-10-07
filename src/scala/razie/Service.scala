package scala.razie

import com.razie.pub.base._
import com.razie.pub.agent._
import com.razie.pub.assets._
import com.razie.pub.comms._

/** 
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
   def action (action:String, args:AttrAccess*) = 
	   if (loc == Agents.me)
	      new ServiceActionToInvoke(name, AI(action), args)
	   else
	      new ServiceActionToInvoke(loc.url, name, AI(action), args)
   
   def action (action:ActionItem, args:AttrAccess*) = {
	   if (loc == Agents.me)
	      new ServiceActionToInvoke(name, action, args)
	   else
	      new ServiceActionToInvoke(loc.url, name, action, args)
   }
}