/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.secu;

import java.io._
import java.security._
import java.security.spec._
import java.util.Date
import sun.security.x509._

/** key store support */
object KS {
  def load (store:String, pwd:String) = { val ks = new SimpleKeyStore(store); ks.load(pwd); ks}
  def create (store:String, pwd:String) = { val ks = new SimpleKeyStore(store); ks.create(pwd); ks}
   
  var initialized=false

  /** generate a key pair */
  def genKeys () : (PrivateKey, PublicKey) = {
    val keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
    val random = SecureRandom.getInstance("SHA1PRNG", "SUN");
    keyGen.initialize(1024, random);

    // the simpler BC version
    //      val keyGen = KeyPairGenerator.getInstance("RSA", "BC");
    //   keyGen.initialize(1024);

    val pair = keyGen.generateKeyPair();
    val priv = pair.getPrivate();
    val pub = pair.getPublic();
   
    (priv,pub)
  }

  /** restore a public key from encoded bytes */
  def pubKeyFromBytes (encKey:Array[byte]) = {
    val pubKeySpec = new X509EncodedKeySpec(encKey);
    val keyFactory = KeyFactory.getInstance("DSA", "SUN");
    val pubKey = keyFactory.generatePublic(pubKeySpec);
    pubKey
  }

  /** sign an array of bytes. The result should normally be then Byte64 encoded */
  def sign (in:Array[byte], pk:PrivateKey):Array[byte] = {
    /* Create a Signature object and initialize it with the private key */
    val dsa = Signature.getInstance("SHA1withDSA", "SUN");
    dsa.initSign(pk);

    /* Update and sign the data */
    dsa.update(in, 0, in.length);

    /* Now that all the data to be signed has been read in,
     generate a signature for it */

    dsa.sign(); // this returns the signed thing
  }

  /** verify a signed byte array */
  def verify (signed:Array[byte], original:Array[byte], pubKey:PublicKey) = {
    /* create a Signature object and initialize it with the public key */
    val sig = Signature.getInstance("SHA1withDSA", "SUN");
    sig.initVerify(pubKey);

    sig.update (original, 0, original.length)
      
    /* Update and verify the data */
    sig.verify(signed);
  }

  /** alias for the main certificate */
  final val CERT = "mainCert"
}

/** abstract keystore functionality. You create a store and keep keys in it... */
abstract class RazKeyStore (val store:String) {
//  if (! KS.initialized)
//    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//  KS.initialized=true

  def load(pwd:String)
  def create (pwd:String)
  def save(pwd:String)
  def loadKeys (pwd:String, alias:String ): (PrivateKey, PublicKey)
  def store (pwd:String, alias:String, pk:PrivateKey, pubk:PublicKey)
}

/** very simple storage of keys as files in a folder
 * 
 *  @param store: a folder where the keys will be stored
 */
class SimpleKeyStore (store:String) extends RazKeyStore (store) {
  def load(pwd:String) = {}
  def create (pwd:String) = {new File (store).mkdirs()}
  def save(pwd:String) = {}

  def loadKeys (alias:String, pwd:String) : (PrivateKey, PublicKey) = {
    if (new File (store+"/"+alias+".pubkey").exists()) {
      val puf = new FileInputStream(store+"/"+alias+".pubkey");
      val encKey = new Array[byte](puf.available());
      puf.read(encKey);
      puf.close();

      val pubKeySpec = new X509EncodedKeySpec(encKey);

      val keyFactory = KeyFactory.getInstance("DSA", "SUN");
      val pubKey = keyFactory.generatePublic(pubKeySpec);

      // priv
      if (new File (store+"/"+alias+".pkey").exists()) {
        val pf = new FileInputStream(store+"/"+alias+".pkey");
        val pencKey = new Array[byte](pf.available());
        pf.read(pencKey);
        pf.close();
        val pKeySpec = new PKCS8EncodedKeySpec(pencKey);
        val pKey = keyFactory.generatePrivate(pKeySpec);
        (pKey, pubKey)
      } else
        (null, pubKey)
    } else (null, null)
  }

  def store (pwd:String, alias:String, pk:PrivateKey, pubk:PublicKey) {
    if (pk != null) {
//         val key = pk.getEncoded();
      val key = new PKCS8EncodedKeySpec (pk.getEncoded()).getEncoded()
      val keyfos = new FileOutputStream(store+"/"+alias+".pkey");
      keyfos.write(key);
      keyfos.close();
    }

    val key = new X509EncodedKeySpec (pubk.getEncoded()).getEncoded()
//         val key = pubk.getEncoded();
    val keyfos = new FileOutputStream(store+"/"+alias+".pubkey");
    keyfos.write(key);
    keyfos.close();
  }
}
     
/** trying to get a handle on using the Java keystores 
 * 
 * TODO this is not complete - although it worked last time :)
 * 
 */
class JavaKeyStore (store:String) extends RazKeyStore (store) {
  var ks:KeyStore = KeyStore.getInstance("JKS");
  var cert:java.security.cert.Certificate=null

//  if (! KS.initialized)
//    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//  KS.initialized=true

  def load(pwd:String) = {
    val ksbufin = new BufferedInputStream(new FileInputStream(store));
    ks.load(ksbufin, pwd.toCharArray());
    cert = ks.getCertificate(KS.CERT)
  }

  def create (pwd:String) = {
    ks.load(null, pwd.toCharArray());

    val t = KS.genKeys()
    cert = newCert(pwd, KS.CERT, new KeyPair(t._2, t._1))
      
    // store away the keystore
    var fos:java.io.FileOutputStream = null;
    try {
      fos = new java.io.FileOutputStream(store);
      ks.store(fos, pwd.toCharArray);
    } finally {
      if (fos != null) {
        fos.close();
      }
    }

  }
   
  def save(pwd:String) = {
    // store away the keystore
    var fos:java.io.FileOutputStream = null;
    try {
      fos = new java.io.FileOutputStream(store);
      ks.store(fos, pwd.toCharArray);
    } finally {
      if (fos != null) {
        fos.close();
      }
    }
  }

  private def newCert (pwd:String, alias:String, pair:KeyPair) = {
   // TODO enable this below when trying this class...
   null
//    val x = org.bouncycastle.x509.examples.AttrCertExample.createAcIssuerCert(pair.getPublic(), pair.getPrivate());
//    ks.setCertificateEntry (alias, x)
//    x
  }

  def loadKeys (pwd:String, alias:String ): (PrivateKey, PublicKey) = {
    val priv = ks.getKey(alias, pwd.toCharArray())
  
    if (priv.isInstanceOf[PrivateKey]) {

      val cert = ks.getCertificate(alias);
      val encodedCert = cert.getEncoded();

      val pub = cert.getPublicKey();
   
      (priv.asInstanceOf[PrivateKey], pub)
    } else {
      (null, priv.asInstanceOf[PublicKey])
    }
   
    /* save the certificate in a file named "suecert" */
//   FileOutputStream certfos = new FileOutputStream("suecert");
//   certfos.write(encodedCert);
//   certfos.close();

    // NORMALLY you'd send the certificate over...
   
//   val certfis = new FileInputStream(certName);
//   java.security.cert.CertificateFactory cf =
//      java.security.cert.CertificateFactory.getInstance("X.509");
//   java.security.cert.Certificate cert = cf.generateCertificate(certfis);

  }

  def store (pwd:String, alias:String, pk:PrivateKey, puk:PublicKey) {
    ks.setKeyEntry(alias, pk, pwd.toCharArray, Array[java.security.cert.Certificate] ( cert ));
    // TODO how to store pubkeys
    save(pwd)
  }
}