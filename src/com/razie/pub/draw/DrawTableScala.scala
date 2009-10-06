/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.draw

import com.razie.pub.draw.Renderer.Technology
import com.razie.pub.draw.DrawTable.MyRenderer
import com.razie.pub.base.AttrAccess

object DrawTableScala {
//   implicit def dttos (d:DrawTable):DrawTableScala = new DrawTableScala

   def asTable (o:Iterable[AttrAccess]) : DrawTable = {
      val table = new DrawTableScala()
      val whatamess:Iterable[String]= scala.collection.JavaConversions.asIterable(o.head.getPopulatedAttr)
      table.headers = whatamess
      table.prefCols = whatamess.size

      for (x <- o; a <- table.headers) table.write (x.a(a))
             
      table
   }
}

/** enhanced DrawTable
 * 
 */
class DrawTableScala extends DrawTable {
	// TODO set prefCol == headers.size when setting headers
   var headers : Iterable[String] = List()
   
   override def getRenderer(t:Technology) = new DTSRenderer()

}

class DTSRenderer extends DrawTable.MyRenderer {

   override def canRender(o:AnyRef, t:Technology) =
      o.isInstanceOf[DrawTable];

   override def renderHeader(o:AnyRef, technology:Technology, stream:DrawStream):AnyRef = {
      val table = o.asInstanceOf[DrawTableScala]
                                   
      if (Technology.HTML.equals(technology)) {
         // TODO stream or use StringBuilder
         var x = "<table valign=center" + table.htmlWidth + ">\n";
            
         if (! table.headers.isEmpty) {
            x += DrawTable.MyRenderer.makeTR (table)
            table.headers foreach (x += "<td>" + _ + "</td>")
            x += "</tr>\n"
         }
            
         x
      }
      else
         "?"
   }
}
