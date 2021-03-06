package se.inera.webcert.service.signatur.asn1;

/**
 * Defines a limited set of ASN.1 byte identifiers.
 *
 * Created by eriklupander on 2015-09-01.
 */
public interface ASN1Type {

    byte SET = 0x31;
    byte SEQUENCE = 0x30;
    byte OBJECT_IDENTIFIER = 0x06;
    byte UTF8_STRING = 0x0C;
    byte PRINTABLE_STRING = 0x13;

}
