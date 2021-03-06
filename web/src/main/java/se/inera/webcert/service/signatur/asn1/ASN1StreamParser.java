package se.inera.webcert.service.signatur.asn1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

/**
 * Semi-generic byte stream parser.
 *
 * Usually used to extract data from a base64-encoded ASN.1 netId signature by
 * matching a supplied sequence of bytes with the content of the stream.
 *
 * If a match is found, the up-front known number of bytes is read and returned as "result".
 *
 * For unknown-length values, an alternate implementation will have to be developed that uses the
 * ASN.1 triplet mechanism to determine data length.
 *
 * Created by eriklupander on 2015-09-01.
 */
public class ASN1StreamParser {

    /**
     * Tries to extract a value from the supplied base64-encoded InputStream by trying to match
     * a sequence of bytes in the stream with the supplied marker sequence of bytes.
     *
     * @param is
     *      A BASE64-encoded byte stream, typically an ASN.1 signature from NetID
     * @param marker
     *      A known sequence of bytes denoting where the wanted value starts in the stream.
     * @param dataLength
     *      The number of bytes to read following the marker, when found.
     * @return
     *      String representation of the found value or null if the marker wasn't present in the byte stream.
     * @throws IOException
     */
    public String parse(InputStream is, int[] marker, int dataLength) throws IOException {
        byte[] bytes = IOUtils.toByteArray(is);
        byte[] decoded = Base64.decodeBase64(bytes);

        ByteArrayInputStream bais = new ByteArrayInputStream(decoded);

        LimitedQueue<Integer> buffer = new LimitedQueue<>(marker.length);
        try {
            while (bais.available() > 0) {
                int b = unsignByte(bais);
                buffer.add(b);

                if (buffer.size() == marker.length && match(buffer, marker)) {
                    // Extract val
                    StringBuilder buf = new StringBuilder();
                    for (int a = 0; a < dataLength; a++) {
                        int c = unsignByte(bais);
                        buf.append((char) c);
                    }
                    return buf.toString();
                }
            }
        } finally {
            try { is.close(); } catch(Exception e) {};
            try { bais.close(); } catch(Exception e) {};
        }
        return null;
    }

    /**
     * The ASN.1 data stream from NetID is supplied as Base64-encoded bytes which Java treats as signed integers
     * Perform the [byte] & 0xFF trick to transform the unsigned byte value into an int.
     */
    private int unsignByte(ByteArrayInputStream bais) {
        return bais.read() & 0xFF;
    }

    /**
     * Iterate over the current sequence of bytes in the buffer. If a non-matching byte
     * is encountered, the iteration terminates and the method returns false.
     *
     * All bytes in the buffer must exactly match the supplied marker sequence.
     *
     * @param buffer
     * @param marker
     * @return
     */
    private boolean match(LimitedQueue<Integer> buffer, int[] marker) {
        for (int a = 0; a < marker.length; a++) {
            if (!buffer.get(a).equals(marker[a])) {
                 return false;
            }
        }
        return true;
    }


    /**
     * Internal subclass of LinkedList popping out elements on FIFO-basis when the defined capacity (i.e. limit) is
     * reached.
     *
     * @param <E>
     */
    private class LimitedQueue<E> extends LinkedList<E> {
        private int limit;

        public LimitedQueue(int limit) {
            this.limit = limit;
        }

        @Override
        public boolean add(E o) {
            super.add(o);
            while (size() > limit) {
                super.remove();
            }
            return true;
        }
    }
}
