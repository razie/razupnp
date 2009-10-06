/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.base.data

import com.razie.pub.base._

/** add capability to support scala scripts */
@Factory()
class ScriptFactoryScala (val other:ScriptFactory, val dflt:Boolean) extends ScriptFactory {
   override def makeImpl (lang:String, s:String) = {
    if (lang != null && "SCALA" == lang.toUpperCase)
       new ScriptScala(s);
    else if (lang == null && dflt) new ScriptScala(s) 
    else if (lang != null && "CMD" == lang.toUpperCase)
       new ScriptCmd(s);
    else other.makeImpl (lang, s)
   }
}
