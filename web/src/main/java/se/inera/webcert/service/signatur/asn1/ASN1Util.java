package se.inera.webcert.service.signatur.asn1;

/**
 * A declarative interface for accessing data within an ASN.1 container.
 *
 * Created by eriklupander on 2015-09-04.
 */
public interface ASN1Util {

    String parsePersonId(String asn1Signature);

}
