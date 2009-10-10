package com.razie.pub.upnpscala

import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.device.DeviceChangeListener;
import org.cybergarage.upnp.device.NotifyListener;
import org.cybergarage.upnp.device.SearchResponseListener;
import org.cybergarage.upnp.ssdp.SSDPPacket;
import org.w3c.dom.Element;

/** represents an UPNP control point. Control points keep track of devices on the network */
trait UpnpControlPoint {

	// TODO make this protected or something
        var cp : ControlPoint

        def start = cp.start()
        def stop = cp.stop()

        def refreshDevices

        // TODO optimize
        def allDevices () : List[UpnpDevice] 

        // TODO optimize
        def devicesOfType (devicetype:String) : List[UpnpDevice]

        // TODO optimize
        def devicesWithService (service:String) : List[UpnpDevice]
}

// TODO add notification stuff
class UpnpCPBridge (val cp : UpnpControlPoint) extends ControlPoint {
        cp.cp = this
}