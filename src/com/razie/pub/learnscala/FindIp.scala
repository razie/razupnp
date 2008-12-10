package com.razie.pub.learnscala

import java.net._

object FindIp {
  def main(args : Array[String]) : Unit = {
    val nes =  NetworkInterface.getNetworkInterfaces()
    val li : List[InetAddress] = List()
    
    while (nes.hasMoreElements) {
      val ne=nes.nextElement
      val iae = ne.getInetAddresses
      while (iae.hasMoreElements) {
        val ia=iae.nextElement
        ia :: li
        ia.getHostAddress
      }
    }
    
    //li.foreach((x:InetAddress)=>Unit = {println(x.ia.getHostName)})
    println(li)
  }
}
