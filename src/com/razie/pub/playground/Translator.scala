package com.razie.pub.playground

import com.razie.pubstage.comms.ObjectStream

/**
 * translators adapt some kind of input into some other kind of input, for instance browsing an
 * external site, translate into mutant assets
 * 
 * @author razvanc
 * @version $Id$
 * 
 */
trait Translator {

    /**
     * translate the input stream into the output stream. translation can be serial or parallel,
     * it's all hidden behind the stream concept
     */
    def translate(input:ObjectStream) : ObjectStream

}

/** simplify implementing this on the fly in java code */
abstract class TranslatorImpl extends Translator {
   
}