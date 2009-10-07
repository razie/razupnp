package scala.razie

import com.razie.pub.assets._
import com.razie.pub.comms._

object Agent {

   def apply (url:String) = handle(url)
  
   /** create a localhost handle with the given port
    * 
    * @param port the port the agent listens on
    */
   def local (port:String) = {
	  new AgentHandle("localhost-"+port, "localhost", "127.0.0.1", port, "http://localhost:"+port) 
   }
   
   def handle (url:String) = {
      val (host,port) = hostport(url)
	  new AgentHandle(host+"-"+port, host, host, port, url) 
   }
   
	def hostport (url:String) : (String,String) = {
		val v = new AssetLocation (url)
		(v.getHost, v.getPort)
	}
	
}
