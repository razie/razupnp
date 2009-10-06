/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details. No warranty implied nor any liability assumed for this code.
 */
package com.razie.pub.playground

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.razie.pub.draw.DrawStream;
import com.razie.pub.draw.Renderer;
import com.razie.pub.draw.SimpleDrawStream;
import com.razie.pub.draw.Renderer.Technology;

/**
 * simple factory
 */
object MediaRssDrawStream  {
val RSS_BEGIN="<rss>"
val RSS_END="</rss>"
	
    /** use this to stream to some other stream (http etc)...) */
    def make(wrapped:DrawStream): MediaRssDrawStream = {
        val res = new MediaRssDrawStream(wrapped)
//        res.switchTechnology(Renderer.Technology.HTML)
        wrapped.write(RSS_BEGIN)
        res
    }

    /**
     * use this to stream into a string..use toString at the end
     *
     * @throws IOException
     */
    def make() : MediaRssDrawStream = {
        val res = new MediaRssDrawStream(new SimpleDrawStream(Technology.MEDIA_RSS, new ByteArrayOutputStream()));
        // buffer = (ByteArrayOutputStream) ((SimpleDrawStream) super.proxied).out;
        // ((SimpleDrawStream) proxied).writeBytes(UpnpUtils.DIDL_BEG.getBytes());
//        res.renderObjectToStream(RSS_BEGIN);
        res
    }
}

/**
 * a drawing stream for DIDL lists, used in UPNP. Will use UPNP as the rendering technology and add
 * DIDL header/footer.
 *
 * to stream to an http socket use new DIDLDrawStream (new HttpDrawStream(socket)) to stream
 * directly to a socket use new DIDLDrawStream (new SimpleDrawStream(socket.getOutputStream)) to
 * stream to string use new DIDLDrawStream () and toString() at the end
 *
 * @author razvanc
 * @version $Id$
 *
 */
class MediaRssDrawStream (wrapped:DrawStream) extends com.razie.pub.draw.DrawStream.DrawStreamWrapper(wrapped) {

    var closed:boolean = false;

    override def close() = {
        // TODO not correct, since BG threads may still produce stuff...
        if (!closed)
            renderObjectToStream(MediaRssDrawStream.RSS_END);
        // ((SimpleDrawStream) proxied).writeBytes(UpnpUtils.DIDL_END.getBytes());
        closed = true;
    }

    override def toString() = {
        proxied.toString();
    }
}
