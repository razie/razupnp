package com.razie.pub.agent

import java.lang.reflect.Method;

import com.razie.pub.base._
import com.razie.pub.base.data._
import com.razie.pub.lightsoa._
import com.razie.pub.comms.ActionToInvoke;
import com.razie.pub.comms.ServiceActionToInvoke;
import com.razie.pub.draw._
import com.razie.pub.draw.widgets._

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
      for (val s <- RazElement.tolist(Agent.instance().copyOfServices()).sort(_.getClass().getSimpleName()<_.getClass().getSimpleName())) {
//      for (val s <- RazElement.tolist(Agent.instance().copyOfServices())) {
//         table.write(s.getClass().getName().replaceFirst(".*\\.", ""));
         table.write(s.getClass().getSimpleName());
         table.write(s.status().drawBrief());
         if (s.getClass().getAnnotation(classOf[SoaService]) != null) {
            val svcname = s.getClass().getAnnotation(classOf[SoaService]).name();
            var sm = new StringBuilder("Methods: ");
            for (val m <- s.getClass().getMethods())
               sm append method(svcname, m);
            table.write(new DrawToString(sm));
         } else
            table.write("-");
      }
      table;
   }

   /** prepare a method to html */
   private def method(service:String , m:Method ):StringBuilder ={
      var sm = new StringBuilder(); // we add the name later, as a link...
      val parms = new AttrAccess.Impl();

//      sm.
      if (m.getAnnotation(classOf[SoaMethod]) != null) {
         sm append "("
         if (m.getAnnotation(classOf[SoaAllparms]) != null)
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

         if (parms.size() > 0) {
            val ai = new ActionItem(m.getName());
            val df = new DrawForm(ai, new ServiceActionToInvoke(service, ai), parms);
            val ati:ActionToInvoke = new DrawCallback(df, 60);

            b = new NavButton(ai, ati.makeActionUrl());
         } else {
            val ati = new ServiceActionToInvoke(service, new ActionItem(m.getName()));
            b = new NavButton(ati.actionItem, ati.makeActionUrl());
         }

         b.style(NavLink.Style.ONELINE, NavLink.Size.SMALL);
         sm.insert (0, b.render(Renderer.Technology.HTML, null))

         sm append ") ; ";
      }

      sm;
   }
}
