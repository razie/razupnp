/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.agent

import java.lang.reflect.Method;

import com.razie.pub.base._
import com.razie.pub.comms._
import com.razie.pub.assets._
import com.razie.pub.base.data._
import com.razie.pub.lightsoa._
import com.razie.pub.comms.ActionToInvoke;
import com.razie.pub.comms.ServiceActionToInvoke;
import com.razie.pub.draw._
import com.razie.pub.draw.widgets._

object PageServices {
   
   // TODO share with frealking assets - got my ears caught in this and copy/paste hacked it...
   def methodButton(k:AssetKey , m:Method ): NavButton ={
      val parms = new AttrAccess.Impl();

      if (m.getAnnotation(classOf[SoaMethod]) != null) {
         if (m.getAnnotation(classOf[SoaAllParms]) == null)
            for (val a:String <- m.getAnnotation(classOf[SoaMethod]).args()) 
               parms.set(a, "")

         // adding link to call the method
         var b:NavButton = null;
         val ai = new ActionItem(m.getName());

         b = maker2 (k, ai, parms)

         b
      } else null
   }
   
   /** prepare a method to html */
   def method(service:String , m:Method , maker:(String, ActionItem, AttrAccess)=>NavButton):StringBuilder ={
      var sm = new StringBuilder(); // we add the name later, as a link...
      val parms = new AttrAccess.Impl();

      if (m.getAnnotation(classOf[SoaMethod]) != null) {
         sm append "("
         if (m.getAnnotation(classOf[SoaAllParms]) != null)
            sm append "..." ;
         else {
            var first = true;
            for (val a:String <- m.getAnnotation(classOf[SoaMethod]).args()) {
               sm append (if (first) "" else ",") + a;
               parms.set(a, "")
               first = false;
            }
         }

         // adding link to call the method
         var b:NavButton = null;
         val ai = new ActionItem(m.getName());

         b = maker (service, ai, parms)

         b.style(NavLink.Style.ONELINE, NavLink.Size.SMALL);
         sm.insert (0, b.render(Renderer.Technology.HTML, null))
         sm append ") ; ";
         
         sm
      }

      sm
   }

   def maker1 (service:String, ai:ActionItem, parms:AttrAccess):NavButton = {
      if (parms.size() > 0) {
         val df = new DrawForm(ai, new ServiceActionToInvoke(service, ai), parms);
         val ati:ActionToInvoke = DrawCallback(df, 60);
         new NavButton(ai, ati.makeActionUrl())
      } else {
         val ati = new ServiceActionToInvoke(service, ai);
         new NavButton(ati.actionItem, ati.makeActionUrl());
      }
   }
   
   def maker2 (k:AssetKey, ai:ActionItem, parms:AttrAccess):NavButton = {
      val actoi = new AssetActionToInvoke(Agents.me.url, k, ai);
      if (parms.size() > 0) {
         val df = new DrawForm(ai, actoi, parms);
         val ati:ActionToInvoke = DrawCallback(df, 60);
         new NavButton(ai, ati.makeActionUrl())
      } else {
         val ati = actoi
         new NavButton(ati.actionItem, ati.makeActionUrl());
      }
   }
}

/** paint a page with the status of all services in this agent... */
class PageServices extends DrawableSource {

   override def makeDrawable = drawLeft()

   private def drawLeft() = {
      val table = new DrawTable();
      table.prefCols = 3;
      table.horizAlign = DrawTable.HorizAlign.LEFT;
      table.packed = true;
      table.rowColor = "#292929";

      // TODO reusability - can do this with any set of objects, not just services
      for (val s <- scala.collection.JavaConversions.asBuffer(Agent.instance().copyOfServices()).toList.sort(_.getClass().getSimpleName()<_.getClass().getSimpleName())) {
//      for (val s <- RazElement.tolist(Agent.instance().copyOfServices())) {
//         table.write(s.getClass().getName().replaceFirst(".*\\.", ""));
         table.write(s.getClass().getSimpleName());
         table.write(s.status().drawBrief());
         if (s.getClass().getAnnotation(classOf[SoaService]) != null) {
            val svcname = s.getClass().getAnnotation(classOf[SoaService]).name();
            var sm = new StringBuilder("Methods: ");
            for (val m <- s.getClass().getMethods())
               sm append PageServices.method(svcname, m, PageServices.maker1) 
            table.write(new DrawToString(sm));
         } else
            table.write("-");
      }
      table;
   }

}