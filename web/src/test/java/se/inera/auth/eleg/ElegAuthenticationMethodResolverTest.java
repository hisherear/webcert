package se.inera.auth.eleg;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.auth.common.BaseSAMLCredentialTest;
import se.inera.webcert.hsa.model.AuthenticationMethod;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by eriklupander on 2015-08-26.
 */
@RunWith(MockitoJUnitRunner.class)
public class ElegAuthenticationMethodResolverTest extends BaseSAMLCredentialTest {

    private static final java.lang.String MOBILT_BANK_ID_LOGIN_METHOD = "ccp11";
    private static final java.lang.String BANK_ID_LOGIN_METHOD = "ccp10";
    private static final java.lang.String NET_ID_LOGIN_METHOD = "ccp8";
    private static final java.lang.String INDETERMINATE_LOGIN_METHOD = "";
    private static final java.lang.String UNKNOWN_LOGIN_METHOD = "ccp7";



    @Mock
    ElegAuthenticationAttributeHelper elegAuthenticationAttributeHelper;

    @InjectMocks
    ElegAuthenticationMethodResolverImpl testee;

    @BeforeClass
    public static void setup() throws Exception {
        bootstrapSamlAssertions();

    }

    @Test
    public void testBankID() {
        when(elegAuthenticationAttributeHelper.getAttribute(any(SAMLCredential.class), anyString())).thenReturn(BANK_ID_LOGIN_METHOD);
        AuthenticationMethod authMetod = testee.resolveAuthenticationMethod(buildPrivatlakareSamlCredential());
        assertEquals(AuthenticationMethod.BANK_ID, authMetod);
    }

    @Test
    public void testMobiltBankID() {
        when(elegAuthenticationAttributeHelper.getAttribute(any(SAMLCredential.class), anyString())).thenReturn(MOBILT_BANK_ID_LOGIN_METHOD);
        AuthenticationMethod authMetod = testee.resolveAuthenticationMethod(buildPrivatlakareSamlCredential());
        assertEquals(AuthenticationMethod.MOBILT_BANK_ID, authMetod);
    }

    @Test
    public void testNetID() {
        when(elegAuthenticationAttributeHelper.getAttribute(any(SAMLCredential.class), anyString())).thenReturn(NET_ID_LOGIN_METHOD);
        AuthenticationMethod authMetod = testee.resolveAuthenticationMethod(buildPrivatlakareSamlCredential());
        assertEquals(AuthenticationMethod.NET_ID, authMetod);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoIssuerThrowsException() {
        when(elegAuthenticationAttributeHelper.getAttribute(any(SAMLCredential.class), anyString())).thenReturn(null);
        testee.resolveAuthenticationMethod(buildPrivatlakareSamlCredential());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testIndeterminateIssuerThrowsException() {
        when(elegAuthenticationAttributeHelper.getAttribute(any(SAMLCredential.class), anyString())).thenReturn(INDETERMINATE_LOGIN_METHOD);
        testee.resolveAuthenticationMethod(buildPrivatlakareSamlCredential());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknwonIssuerThrowsException() {
        when(elegAuthenticationAttributeHelper.getAttribute(any(SAMLCredential.class), anyString())).thenReturn(UNKNOWN_LOGIN_METHOD);
        testee.resolveAuthenticationMethod(buildPrivatlakareSamlCredential());
    }
}