package com.razie.pub.base.data

import com.razie.pub.base.RazScript
import com.razie.pub.base.ScriptContext
import com.razie.pub.base.TimeOfDay
import com.razie.pub.WinExec

/** a windows cmd script */
class ScriptCmd (val script:String) extends RazScript {

    /** @return the statement */
    override def toString() = "cmd:\n" + script

    /**
     * execute the script with the given context
     * 
     * @param c the context for the script
     */
   override def eval(c:ScriptContext) : AnyRef = {
      var result:AnyRef = "";

      try {
    	 result = WinExec.execAndWait (script, "")
        } catch {
          case e:Exception =>
            throw new RuntimeException("While processing script: " + this.script, e);
        }
    
        result;
    }
}

object ScriptCmdTestApp extends Application{
    var script = "cmd /C dir";
    var js = new ScriptCmd(script);
    System.out.println(js.eval(new ScriptContext.Impl()));
}
