/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.draw

import com.razie.pub.lightsoa._
import com.razie.pub.draw.DrawStream
import com.razie.pub.draw.Renderer
import com.razie.pub.base._
import com.razie.pub.base.log._
import com.razie.pub.agent._

/** 
 * special drawing service: can prepare a page and invoke it later.
 * 
 * Don't you hate it when you write some loops and have to prepare some pages to display later, as a callback? What you end up doing is to pass yourself lots of information via the URL so you can re-build the page/entity again. Alternatively, you do what is maybe a lot of processing to reload and re-build whatever displays you need.
 * 
 * This class addresses that: simply build the displays where you're looping and have everything handy and just cache them here for a while.  
 * 
 * Note that this approach, although often simplifies programming a alot, is both more processing and more memory intensive than the callback!
 * 
 * On the other hand, why make those huge URLs, which you have to serialize to the client, they have to parse and render them and then do it all over again in reverse...?
 * 
 * @author Razie
 */
//TODO @SoaService(){val name = "DrawScreenService", val bindings = Array("http"), val descr = "draw prepared screens" }
@SoaService(name = "DrawScreenService", bindings = Array("http"), descr = "draw prepared screens" )
class DrawScreenService extends AgentService {

   /** the second initialization phase: the agent is starting up */
   override def onStartup() : Unit = {
   }

   /** the agent needs to shutdown this service. You must join() all threads and return to agent. */
   override def onShutdown() : Unit = {
   }

   /** NEEDS at least a parm called "screen" */
   @SoaMethod(descr = "draw a generated screen")
   @SoaAllParms
   def draw (parms:AttrAccess) = {
      MyCache.get(parms.sa("screen")) match {
         case Some (d:DrawCallback.CallbackFun) => d(parms)
         case Some (d:Drawable) => d
         case _ => "No page found with screen=" + parms.sa("screen")
      }
   }
    
   @SoaMethod(descr = "list all screens in cache")
   @SoaStreamable
   def list (out:DrawStream):Unit =
      // TODO nicer, use the _ number for the display...
   MyCache.keys.foreach {out write new InternalDrawCallback (_)}

}

object DrawScreenServiceStatic {
   val DRAW = new ActionItem ("draw")
   
   def autoRegister () = {
      // TODO verify current agent has the service - if not, add it...
      val a = Agent.instance
      if (a.locateService("DrawScreenService") == null) {
         Log.logThis("WARN_PROG auto-registering the drawscreen service")
         a.register( new DrawScreenService)
      }
   }
}
