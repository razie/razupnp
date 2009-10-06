package com.razie.pub.cfg

/** these can process elements in an XML configuration file. 
 * Then register with the registry and you'll be called */
trait XmlConfigProcessor {
   def eat (e:org.w3c.dom.Element) : Unit
}
