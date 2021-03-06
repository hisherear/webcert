package se.inera.webcert.certificatesender.services;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.xml.ws.WebServiceException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.inera.intyg.clinicalprocess.healthcond.certificate.sendcertificatetorecipient.v1.SendCertificateToRecipientResponseType;
import se.inera.webcert.exception.PermanentException;
import se.inera.webcert.exception.TemporaryException;
import se.inera.webcert.client.SendCertificateServiceClient;
import se.riv.clinicalprocess.healthcond.certificate.v1.ErrorIdType;
import se.riv.clinicalprocess.healthcond.certificate.v1.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.v1.ResultType;

/**
 * Created by eriklupander on 2015-05-22.
 */
@RunWith(MockitoJUnitRunner.class)
public class CertificateSendProcessorTest {

    private static final String INTYGS_ID1 = "intygs-id-1";
    private static final String PERSON_ID1 = "19121212-1212";
    private static final String RECIPIENT1 = "recipient1";
    private static final String LOGICAL_ADDRESS1 = "logicalAddress1";

    @Mock
    SendCertificateServiceClient sendServiceClient;

    @InjectMocks
    CertificateSendProcessor certificateSendProcessor = new CertificateSendProcessor();

    @Test
    public void testSendCertificate() throws Exception {
        // Given
        SendCertificateToRecipientResponseType response = createResponse(ResultCodeType.OK, null);
        when(sendServiceClient.sendCertificate(INTYGS_ID1, PERSON_ID1, RECIPIENT1, LOGICAL_ADDRESS1)).thenReturn(response);

        // When
        certificateSendProcessor.process(INTYGS_ID1, PERSON_ID1, RECIPIENT1, LOGICAL_ADDRESS1);

        // Then
        verify(sendServiceClient).sendCertificate(INTYGS_ID1, PERSON_ID1, RECIPIENT1, LOGICAL_ADDRESS1);
    }

    @Test(expected = TemporaryException.class)
    public void testSendCertificateThrowsTemporaryOnApplicationError() throws Exception {
        // Given
        SendCertificateToRecipientResponseType response = createResponse(ResultCodeType.ERROR, ErrorIdType.APPLICATION_ERROR);
        when(sendServiceClient.sendCertificate(INTYGS_ID1, PERSON_ID1, RECIPIENT1, LOGICAL_ADDRESS1)).thenReturn(response);

        // When
        certificateSendProcessor.process(INTYGS_ID1, PERSON_ID1, RECIPIENT1, LOGICAL_ADDRESS1);

        // Then
        verify(sendServiceClient).sendCertificate(INTYGS_ID1, PERSON_ID1, RECIPIENT1, LOGICAL_ADDRESS1);
    }

    @Test(expected = TemporaryException.class)
    public void testSendCertificateThrowsTemporaryOnTechnicalError() throws Exception {
        // Given
        SendCertificateToRecipientResponseType response = createResponse(ResultCodeType.ERROR, ErrorIdType.TECHNICAL_ERROR);
        when(sendServiceClient.sendCertificate(INTYGS_ID1, PERSON_ID1, RECIPIENT1, LOGICAL_ADDRESS1)).thenReturn(response);

        // When
        certificateSendProcessor.process(INTYGS_ID1, PERSON_ID1, RECIPIENT1, LOGICAL_ADDRESS1);

        // Then
        verify(sendServiceClient).sendCertificate(INTYGS_ID1, PERSON_ID1, RECIPIENT1, LOGICAL_ADDRESS1);
    }

    @Test(expected = PermanentException.class)
    public void testSendCertificateThrowsPermanentOnRevokedError() throws Exception {
        // Given
        SendCertificateToRecipientResponseType response = createResponse(ResultCodeType.ERROR, ErrorIdType.REVOKED);
        when(sendServiceClient.sendCertificate(INTYGS_ID1, PERSON_ID1, RECIPIENT1, LOGICAL_ADDRESS1)).thenReturn(response);

        // When
        certificateSendProcessor.process(INTYGS_ID1, PERSON_ID1, RECIPIENT1, LOGICAL_ADDRESS1);

        // Then
        verify(sendServiceClient).sendCertificate(INTYGS_ID1, PERSON_ID1, RECIPIENT1, LOGICAL_ADDRESS1);
    }

    @Test(expected = PermanentException.class)
    public void testSendCertificateThrowsPermanentOnValidationError() throws Exception {
        // Given
        SendCertificateToRecipientResponseType response = createResponse(ResultCodeType.ERROR, ErrorIdType.VALIDATION_ERROR);
        when(sendServiceClient.sendCertificate(INTYGS_ID1, PERSON_ID1, RECIPIENT1, LOGICAL_ADDRESS1)).thenReturn(response);

        // When
        certificateSendProcessor.process(INTYGS_ID1, PERSON_ID1, RECIPIENT1, LOGICAL_ADDRESS1);

        // Then
        verify(sendServiceClient).sendCertificate(INTYGS_ID1, PERSON_ID1, RECIPIENT1, LOGICAL_ADDRESS1);
    }

    @Test(expected = TemporaryException.class)
    public void testSendCertificateThrowsPermanentOnWebServiceException() throws Exception {
        // Given
        when(sendServiceClient.sendCertificate(INTYGS_ID1, PERSON_ID1, RECIPIENT1, LOGICAL_ADDRESS1)).thenThrow(new WebServiceException());

        // When
        certificateSendProcessor.process(INTYGS_ID1, PERSON_ID1, RECIPIENT1, LOGICAL_ADDRESS1);

        // Then
        verify(sendServiceClient).sendCertificate(INTYGS_ID1, PERSON_ID1, RECIPIENT1, LOGICAL_ADDRESS1);
    }

    @Test
    public void testSendCertificateOnInfoMessage() throws Exception {
        // Given
        SendCertificateToRecipientResponseType response = createResponse(ResultCodeType.INFO, null);
        when(sendServiceClient.sendCertificate(INTYGS_ID1, PERSON_ID1, RECIPIENT1, LOGICAL_ADDRESS1)).thenReturn(response);

        // When
        certificateSendProcessor.process(INTYGS_ID1, PERSON_ID1, RECIPIENT1, LOGICAL_ADDRESS1);

        // Then
        verify(sendServiceClient).sendCertificate(INTYGS_ID1, PERSON_ID1, RECIPIENT1, LOGICAL_ADDRESS1);
    }

//    @Test(expected = IllegalArgumentException.class)
//    public void testIntygsIdIsMissing() throws Exception {
//        try {
//            certificateSendProcessor.process(null, PERSON_ID1, RECIPIENT1, LOGICAL_ADDRESS1);
//        } catch (IllegalArgumentException e) {
//            assertTrue(e.getMessage().contains(Constants.INTYGS_ID));
//            throw e;
//        }
//        fail();
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testPersonIdIsMissing() throws Exception {
//        try {
//            certificateSendProcessor.process(INTYGS_ID1, null, RECIPIENT1, LOGICAL_ADDRESS1);
//        } catch (IllegalArgumentException e) {
//            assertTrue(e.getMessage().contains(Constants.PERSON_ID));
//            throw e;
//        }
//        fail();
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testRecipientIsMissing() throws Exception {
//        try {
//            certificateSendProcessor.process(INTYGS_ID1, PERSON_ID1, null, LOGICAL_ADDRESS1);
//        } catch (IllegalArgumentException e) {
//            assertTrue(e.getMessage().contains(Constants.RECIPIENT));
//            throw e;
//        }
//        fail();
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testLogicalAddressIsMissing() throws Exception {
//        try {
//        certificateSendProcessor.process(INTYGS_ID1, PERSON_ID1, RECIPIENT1, null);
//        } catch (IllegalArgumentException e) {
//            assertTrue(e.getMessage().contains(Constants.LOGICAL_ADDRESS));
//            throw e;
//        }
//        fail();
//    }

    private SendCertificateToRecipientResponseType createResponse(ResultCodeType resultCodeType, ErrorIdType errorType) {
        ResultType resultType = new ResultType();
        resultType.setResultCode(resultCodeType);
        if (errorType != null) {
            resultType.setErrorId(errorType);
        }
        SendCertificateToRecipientResponseType responseType = new SendCertificateToRecipientResponseType();

        responseType.setResult(resultType);
        return responseType;
    }

}
