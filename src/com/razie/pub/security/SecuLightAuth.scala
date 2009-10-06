/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.secu

import com.razie.pub.comms._
import com.razie.pub.util._
import com.razie.pub.base._
import com.razie.pub.base.log._
import java.security._

// TODO SECURITY_ISSUE check passwords
/** 
 * PK-based authentication of the agent nation
 * 
 * Keys are generated locally and all public keys shared among the agents. Each agent requesting something from another, will authenticate its request with its private key. The target will verify that indeed, the remote is who he/she claims.
 * 
 * Read more on security at http://wiki.homecloud.ca/security
 */
class SecuLightAuth (val store:String, prefix:String, pwd:String) extends LightAuth (prefix) {
   val ks = KS.load (store, pwd)
  
   // TODO implement this cache so we don't load every time
   val keys = new scala.collection.mutable.HashMap[String, (PrivateKey, PublicKey)]()
   
   val (mypk, mypubk) = ks.loadKeys(Agents.me.name, pwd)
   val ARG2 = "mutant.secret"
   val ARG1 = "mutant.name"
 
   override def httpSecParms (url: java.net.URL):AttrAccess = {
      if (mypk != null)
         new AttrAccess.Impl (ARG1, Agents.me.name, ARG2, sign(url.getPath()))
      else 
         null
   }
   
   override def iauthorize(socket:java.net.Socket, url:String, httpArgs:AttrAccess):LightAuth.AuthType = {
       val clientip = socket.getInetAddress().getHostAddress();

       val newurl = if (url != null) url.replaceAll (" HTTP/.*", "") else null
       
      // if Agents doesn't know myself, this should succeed, it's not a proper server but maybe
      // some sort of a test???

      // TODO this auth is really weak anyways...
      val debug1 = Agents.getMyHostName()

      var res = LightAuth.AuthType.ANYBODY
      
      // TODO is this correct in linux?
      if (clientip.startsWith(Agents.getHomeNetPrefix())
              || Agents.agent(Agents.getMyHostName()) == null
              || clientip.equals(Agents.agent(Agents.getMyHostName()).ip)) {
          res = LightAuth.AuthType.INHOUSE;
      } else if (Comms.isLocalhost(clientip)) {
          res = LightAuth.AuthType.INHOUSE;
      } else 
         if (httpArgs != null && httpArgs.isPopulated(ARG1) && httpArgs.isPopulated(ARG2)) {
           if (verify (httpArgs.sa(ARG2), newurl, httpArgs.sa(ARG1))) {
              Log.logThis ("AUTH_RECON: accepted remote agent: "+httpArgs.sa(ARG1) )
              res = LightAuth.AuthType.SHAREDSECRET;
           } else if (false) {
             // TODO identify friends 
             res = LightAuth.AuthType.FRIEND;
           }
      }
      
      res
   }  
  
   def sign (secret:String) = {
      var s: String=null
      try {
         s = Base64.encodeBytes(KS.sign(secret.getBytes, mypk))
      } catch {
         case e:Exception => Log.logThis ("ERR_AUTH: cant sign secret", e)
      }
      s
   }

   private[this] def verify (signed:String, original:String, agent:String) = {
           val a = Agents.agent(agent)
           var verified=false
           
           if (a == null) {
              Log.logThis ("ERR_AUTH SECURITY_ISSUE: don't know remote agent: "+agent )
           } else {
              // TODO SECU use password
              val (pk, puk) = KS.load(store, "pass").loadKeys (agent, "pass")
              verified = if (KS.verify(Base64.decode (signed), original.getBytes, puk)) true else false
           }              
     verified 
   }
}
