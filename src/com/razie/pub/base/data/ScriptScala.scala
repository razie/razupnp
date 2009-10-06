/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.base.data

import com.razie.pub.base.RazScript
import com.razie.pub.base.ScriptContext
import com.razie.pub.base.TimeOfDay

/** an interpreted scala script */
class ScriptScala (val script:String) extends RazScript {

    /** @return the statement */
    override def toString() = "scala:\n" + script

    /**
     * execute the script with the given context
     * 
     * @param c the context for the script
     */
   override def eval(c:ScriptContext) : AnyRef = {
      var result:AnyRef = "";

      val env = new scala.tools.nsc.Settings
      val p = new scala.tools.nsc.Interpreter (env)         
      
      try {
         p.bind ("ctx", classOf[ScriptContext].getCanonicalName, c)
         
         val iter = c.getPopulatedAttr().iterator
         while (iter.hasNext) {
            val key = iter.next
            val obj = c.getAttr(key);
            p.bind (key, obj.getClass.getCanonicalName, obj)
         }

         // TODO fix this see http://lampsvn.epfl.ch/trac/scala/ticket/874 at the end,
         // there was some work with jsr223
            
            // Now evaluate the script
            val r = p.interpret (script)

            // TODO find the resulting value nicely
            result = if (c.isPopulated("result")) c a "result" else r.toString

            // TODO put back all variables
//            result = p.
            // add JS variables back into ScriptContext

            // convert to String
//            if (result instanceof Wrapper) {
//                result = ((Wrapper) result).unwrap();
//            } else {
//                result = Context.toString(result);
//            }
        } catch {
          case e:Exception =>
            throw new RuntimeException("While processing script: " + this.script, e);
        }
    
        result;
    }
}

object ScriptScalaTestApp extends Application{
    var script = "val y = 3; def f(x:int)={x+1}; val res=f(7); res";
    var js = new ScriptScala(script);
    System.out.println(js.eval(new ScriptContext.Impl()));

    script = "TimeOfDay.value()";
    js = new ScriptScala(script);
    var ctx = new ScriptContext.Impl();
    ctx.setAttr("TimeOfDay", new TimeOfDay(), null);
    System.out.println(js.eval(ctx));
}
