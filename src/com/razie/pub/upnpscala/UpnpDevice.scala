package com.razie.pub.upnpscala

object UpnpDevice {
//	def fromCyber (d:org.cybergarage.upnp.Device) = {
//		new UpnpDevice () {
//			icyberDevice = d
//			
//			   override def xmlns:String = ""
//			   override def versionMin:int = ""
//			   override def versionMaj:int = ""
//			   override def deviceType :String = d.getDeviceType
//			   override def friendlyName :String = d.getFriendlyName
//			   override def manufacturer :String = d.getManufacturer
//			   override def manufacturerURL :String
//			   override def modelDescription :String
//			   override def modelName :String
//			   override def modelNumber :String
//			   override def modelURL :String
//			   override def serialNumber :String
//			   override def UDN :String
//			   override def UPC:String
//		}
//	}
}

/** 
 * simple upnp device model: has attributes, embedded devices and services
 * 
 * For a detailed description of the attributes, see "UPnP Device Architecture 1.0"
 * 
 * NOTE that the URLBase attribute is added by the CyberGarage support at runtime
 * 
 * TODO resolve formatting when the scala plugin actually works :)
 */
trait UpnpDevice {
   def devices : List[UpnpDevice]
   def services : List[UpnpService]

   def xmlns:String
   def versionMin:int
   def versionMaj:int
   def deviceType :String
   def friendlyName :String
   def manufacturer :String
   def manufacturerURL :String
   def modelDescription :String
   def modelName :String
   def modelNumber :String
   def modelURL :String
   def serialNumber :String
   def UDN :String
   def UPC:String
   
   /** create the full xml description */
   def toFullUpnpXml = {
   //<?xml version="1.0" ?> 
   <root xmlns={xmlns}>
   <specVersion>
		<major>{versionMaj}</major> 
		<minor>{versionMin}</minor> 
	</specVersion>
	
   {toUpnpXml}
	
	</root>
   }

   def toUpnpXml:scala.xml.Elem = {
	<device>
		<deviceType>{deviceType}</deviceType> 
		<friendlyName>{friendlyName}</friendlyName> 
		<manufacturer>{manufacturer}</manufacturer> 
		<manufacturerURL>{manufacturerURL}</manufacturerURL> 
		<modelDescription>{modelDescription}</modelDescription> 
		<modelName>{modelName}</modelName> 
		<modelNumber>{modelNumber}</modelNumber> 
		<modelURL>{modelURL}</modelURL> 
		<serialNumber>{serialNumber}</serialNumber> 
		<UDN>{UDN}</UDN> 
		<UPC>{UPC}</UPC> 
		
		{
		   if (services.size > 0) {
		      <serviceList> 
		         {for (val s <- services) yield (s.toBriefUpnpXml)}
              </serviceList> 
		  }
		}
		
		{
			   if (devices.size > 0) {
			      <deviceList> 
			         {for (val s <- devices) yield (s.toUpnpXml)}
	              </deviceList> 
			  }
			}
	</device>
   }

   
   /** note that by this time, you've added all services and sub-devices...
    * 
    * TODO can't change the descriptions after starting...?
    */
   def start = {
	   cyberDevice.start
   }

   /** access with actualDevice() */
   protected var icyberDevice : org.cybergarage.upnp.Device = null
   
   /** the actual cybergarage device, for advanced tweaking of upnp flags 
    * 
    * TODO can't change the descriptions after starting...?
    * */
   def cyberDevice : org.cybergarage.upnp.Device = {
if (icyberDevice == null)			
	icyberDevice = new DeviceBridge(this)
icyberDevice
		   }
	 
   def service (id:String):UpnpService = services.filter (_.serviceId == id).head
	   
}

/** some attributes should not change unless you really know what you're doing */
abstract class DefaultUpnpDevice extends UpnpDevice {
   val xmlns="urn:schemas-upnp-org:device-1-0"
   val versionMaj=1
   val versionMin=0
   
   /** special NOTE about UDN. I believe it's a good idea to generate UDNs that don't change, per instance, see the samples */
   val UDN= "uuid:" + java.util.UUID.randomUUID().toString();

}