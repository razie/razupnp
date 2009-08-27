package com.razie.pub.webui

import com.razie.pub.lightsoa._
import com.razie.pub.draw._
import com.razie.pub.webui._
import com.razie.pub.agent._

/** this is basic JSP-like support in the form of a servlet/service. Will serve pages marked defined in presentation files. See MutantPresentation.add() */
//TODO @SoaService(){val name = "webui", val descr = "web ui service" }
@SoaService(name = "webui", descr = "web ui service")
class WebUiService extends AgentService {
  
//TODO    @SoaMethod(){val descr = "serve page by name", val args = Array( "name" )}
    @SoaMethod(descr = "serve page by name", args = Array( "name" ))
    @SoaStreamable
    def servePage (out:DrawStream, name:String):Unit = {
      out write MutantPresentation.getInstance.page(name)
    }

//TODO    @SoaMethod(){val descr = "serve page by classname", val args = Array( "name" )}
    @SoaMethod(descr = "serve page by classname", args = Array( "name" ))
    @SoaStreamable
    def serveClass (out:DrawStream, name:String):Unit = {
      out write MutantPresentation.paintPageCode(name)
    }

}
