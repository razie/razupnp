package com.razie.pub.upnpscala

import com.razie.pub.upnp._
import com.razie.pub.lightsoa._
import com.razie.pub.base.log._
import com.razie.pub.base.data._
//import scala.razie._

/** just a static factory */
object UpnpServiceFactory {
	/** create a bridge upnp service instance from an annotated SoaService class. NOTE that after this, you must fill in the upnp service attributes. The alternative is to derive from the default service and override attributes */
	def fromAnnotation (o:AnyRef, attrs: UpnpServiceAttrs) : UpnpService = {
      		 // TODO implement
      val binding = new UpnpSoaBinding (o, "bibiku")
      new DefaultService (binding, attrs)
	}
	
}

/** bag of upnp service attributes. Base class of the real thing since you can add them separately as well.
 * 
 * TODO add link with description of the attributes
 * 
 * */
trait UpnpServiceAttrs {
   def xmlns : String 
   def serviceType : String
   def serviceId : String
   def SCPDURL : String
   def controlURL : String
   def eventSubURL : String
}

/** stands in for a upnp service. For services, you don't have to derive your class, just use the SoaService and SoaMethod annotations 
 * 
 * 
 * */
trait UpnpService extends UpnpServiceAttrs {
  val binding : UpnpSoaBinding
  
//   def actions : List[AnyRef]
//   def stateVars : List[AnyRef]
                    
  /** to be included in the device's descriptor */
   def toBriefUpnpXml() = {
		<service> 
			<serviceType>{serviceType}</serviceType> 
			<serviceId>{serviceId}</serviceId> 
			<SCPDURL>{SCPDURL}</SCPDURL> 
			<controlURL>{controlURL}</controlURL> 
			<eventSubURL>{eventSubURL}</eventSubURL> 
		</service> 
   }

   /** full service descriptor */
   def toFullUpnpXml = {
//	   <?xml version="1.0"?>
	   <scpd xmlns={xmlns} >
	   	<specVersion>
	   		<major>1</major>
	   		<minor>0</minor>
	   	</specVersion>
		{
			   if (binding.methods.size > 0) {
	   	<actionList>
	      {for ( val a <- razie.RJS(binding.methods.values)) 
    	     yield actionXml(a)}
	   	</actionList>
			  }
			}
		<serviceStateTable>
		</serviceStateTable>
  </scpd>
   }
   
   def actionXml (m:java.lang.reflect.Method) = {
      if (m.getAnnotation(classOf[SoaMethod]) != null) {
  		<action>
			<name>{m.getName()}</name>
			<argumentList>
			{
            for (val a:String <- m.getAnnotation(classOf[SoaMethod]).args()) {
				<argument>
					<name>{a}</name>
//					<relatedStateVariable>Time</relatedStateVariable>
					<direction>in</direction>
				</argument>
             }
			}
				<argument>
					<name>Result</name>
					<relatedStateVariable>Result</relatedStateVariable>
					<direction>out</direction>
				</argument>
			</argumentList>
		</action>
		} 
   }
   
   def varXml = {
		<stateVariable sendEvents="yes">
			<name>Time</name>
			<dataType>string</dataType>
			</stateVariable>
   }
   
   /** generic sink for actions */
   def actionInvoked (action:String, args:AnyRef) = {
	Log.alarmThis ("NOT IMPLEMENTED!!!")}
   
   /** generic sink for state vars */
   def stateVar (varName:String):AnyRef = {
	Log.alarmThis ("NOT IMPLEMENTED!!!")
	null
	}
   
   def cyberService : org.cybergarage.upnp.Service = null // TODO
}

/** sample default service - you should reset the upnp attributes */
class DefaultService (val binding:UpnpSoaBinding, src:UpnpServiceAttrs) extends UpnpService {
   val xmlns=if (src.xmlns != null) src.xmlns else "urn:schemas-upnp-org:service-1-0"
   val serviceType = if (src.serviceType != null) src.serviceType else "urn:schemas-upnp-org:service:mutant:1"
   val serviceId = if (src.serviceId != null) src.serviceId else "urn:schemas-upnp-org:serviceId:mutant:1"
   val SCPDURL = if (src.SCPDURL != null) src.SCPDURL else "/service/agent-description.xml"
   val controlURL = if (src.controlURL != null) src.controlURL else "/service/timer/control"
   val eventSubURL = if (src.eventSubURL != null) src.eventSubURL else "/service/timer/eventSub"
			
   val actions : List[AnyRef] = List()
   val stateVars : List[AnyRef] = List()
}