/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
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
