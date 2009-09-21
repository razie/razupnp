package com.razie.pub.plugin

/** these are normally created with classbyname and then the afterload is called 
 * 
 * There is a plugin discobvery process 
 */
trait Plugin {
   def loadphase1 = {}
   def loadphase2 = {}
   def unloadphase1= {}
   def unloadphase2= {}
}
