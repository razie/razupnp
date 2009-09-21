package com.razie.pub.plugin

import java.net.URL
import java.io.File
import com.razie.pub.base.data.XmlDoc
import com.razie.pub.base.data.RazElement
import com.razie.pub.base._
import com.razie.pub.assets._
import com.razie.pub.base.log._

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
	    	
	        for (meta <- RazElement.tolist(doc.listEntities("/plugin/metaspecs/metaspec"))) {
	            AssetMgr.instance().register(new AssetMgr.Meta(new ActionItem(meta.getAttribute("name"), meta.getAttribute("icon")), "", meta
	                  .getAttribute("inventory")));
	         }
	    
	      // initialize asset finders and players...
	      for (e <- RazElement.tolist(doc.listEntities("/plugin/assetfinders/assetfinder"))) 
	         AssetMgr.instance().registerFinder (e)
	         
	         val classname = RazElement.torazdoc (doc) xpa "/plugin/@classname"
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
	}


   val logger = Log.Factory.create("Plugins");
}
