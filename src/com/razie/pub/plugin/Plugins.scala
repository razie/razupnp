/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.plugin

import java.net.URL
import java.io.File
import com.razie.pub.base.data.XmlDoc
import com.razie.pub.base.data.RazElement
import com.razie.pub.base._
import com.razie.pub.assets._
import com.razie.pub.base.log._
import com.razie.pub.cfg._

/** simple&stupid plugin management */
object Plugins {
   val allPlugins = scala.collection.mutable.ListBuffer[Plugin]()

   // TODO need plugin dependencies
	def findAll (dir:URL) : List[URL] = {
		val files:Array[File] = new File(dir.toExternalForm).listFiles
		// TODO when scala 2.8 is fiexed: shopuld not have to do toList here
		for (f <- files.toList; if (f.getName.matches("plugin_.*\\.xml")) )
         yield f.toURL()
	}
	
	def init (plugin:URL) = {
      logger.log ("INIT_PLUGIN from " + plugin)
      val doc = new XmlDoc().load ("whatever", plugin)
	    
      // 1. class init
	    
      // 2. load config
	    	
      for (meta <- razie.RJX(doc) xpl "/plugin/metaspecs/metaspec") {
         razie.Metas.addMeta(razie.Meta.fromXml (meta))
         
         for (ma <- meta xpl "metaassoc")
            razie.Metas.addAssoc (razie.MetaAssoc.fromXml (ma, meta));
      }
      
      for (ma <- razie.RJX(doc) xpl "/plugin/metaspecs/metaassoc")
         razie.Metas.addAssoc (razie.MetaAssoc.fromXml (ma));
       
      // initialize asset finders and players...
      for (e <- razie.RJS(doc.listEntities("/plugin/assetfinders/assetfinder")))
         XmlConfigProcessors.eat (e)
	         
      val classname = razie.RJX apply doc xpa "/plugin/@classname"
      if (classname.length > 0) {
         try {
            // in development, the class would already be in classpath - screwy eclipse stuff
            var cloader = ClassLoader.getSystemClassLoader()
   
            // we need a jar file or assume it's in the classpath
            val jar = new URL (plugin.toExternalForm.replace ("\\.xml$",".jar"))
            // load the jar file in classpath
            if (new java.io.File(jar.toURI).exists())
               cloader = new java.net.URLClassLoader(Array(jar), cloader)
      
            val p = Class.forName (classname, true, cloader).newInstance ().asInstanceOf[Plugin];
            p.loadphase1
            allPlugins += p
         } catch {
            case e:Exception => logger.alarm("ERR_CANT_INIT_PLUGIN: classname=" + classname, e)
         }
      }
      
            // TODO Initialize all services - plugins may register services via XML?
//            for (Element e : MutantConfig.getInstance().listEntities(
//                  "/config/services/service")) {
//               try {
//                  AgentService svc = (AgentService) Class.forName(
//                        e.getAttribute("class")).newInstance();
//                  register(svc);
//               } catch (Exception ex) {
//                  Log.logThis("ERR_CANT_CREATE_SERVICE "
//                        + e.getAttribute("class"), ex);
//               }
//            }
      

   }


   val logger = Log.Factory.create("Plugins");
}
