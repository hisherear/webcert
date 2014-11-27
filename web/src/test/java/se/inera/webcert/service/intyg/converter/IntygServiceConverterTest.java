package se.inera.webcert.service.intyg.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.common.internal.Utlatande;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendType;
import se.inera.webcert.service.intyg.converter.IntygServiceConverterImpl.Operation;

public class IntygServiceConverterTest {

    private IntygServiceConverterImpl converter = new IntygServiceConverterImpl();
    
    
    @Test
    public void testBuildSendTypeFromUtlatande() throws Exception {
        
        Utlatande utlatande = createUtlatandeFromJson();
        
        SendType res = converter.buildSendTypeFromUtlatande(utlatande);
        
        assertNotNull(res);
        
        assertNotNull(res.getAvsantTidpunkt());
        
        assertThat(res.getVardReferensId(), containsString("SEND-123-"));
                
        assertEquals("123", res.getLakarutlatande().getLakarutlatandeId());
        assertEquals("Test Testorsson", res.getLakarutlatande().getPatient().getFullstandigtNamn());
        assertEquals("19121212-1212", res.getLakarutlatande().getPatient().getPersonId().getExtension());
        assertNotNull(res.getLakarutlatande().getSigneringsTidpunkt());
        //assertEquals("VardgivarId", res.getAdressVard().getHosPersonal().getForskrivarkod());
        assertEquals("En Läkare", res.getAdressVard().getHosPersonal().getFullstandigtNamn());
        assertEquals("Personal HSA-ID", res.getAdressVard().getHosPersonal().getPersonalId().getExtension());
        assertEquals("Kir mott", res.getAdressVard().getHosPersonal().getEnhet().getEnhetsnamn());
        assertEquals("VardenhetY", res.getAdressVard().getHosPersonal().getEnhet().getEnhetsId().getExtension());
        assertEquals("123456789011", res.getAdressVard().getHosPersonal().getEnhet().getArbetsplatskod().getExtension());
        assertEquals("Landstinget Norrland", res.getAdressVard().getHosPersonal().getEnhet().getVardgivare().getVardgivarnamn());
        assertEquals("VardgivarId", res.getAdressVard().getHosPersonal().getEnhet().getVardgivare().getVardgivareId().getExtension());
        
    }

    @Test
    public void testConcatPatientName() {
        
        List<String> fNames = Arrays.asList("Adam", "Bertil", "Cesar");
        List<String> mNames = Arrays.asList("Davidsson");
        String lName = "Eriksson";
        
        String name = converter.concatPatientName(fNames, mNames, lName);
        
        assertEquals("Adam Bertil Cesar Davidsson Eriksson", name);
    }
    
    @Test
    public void testConcatPatientNameWithSomeNamesBlank() {
        
        List<String> fNames = Arrays.asList("Adam", "", "Bertil");
        List<String> mNames = Arrays.asList(" ");
        String lName = "Eriksson";
        
        String name = converter.concatPatientName(fNames, mNames, lName);
        
        assertEquals("Adam Bertil Eriksson", name);
    }
    
    @Test
    public void testBuildVardRefId() {
        
        LocalDateTime ts = LocalDateTime.parse("2014-01-01T12:34:56.123");
        
        String res = converter.buildVardReferensId(Operation.REVOKE, "ABC123", ts);
        
        assertNotNull(res);
        assertEquals(res, "REVOKE-ABC123-20140101T123456.123");
    }
    
    private Utlatande createUtlatandeFromJson() throws Exception {
        // TODO Auto-generated method stub
        return new CustomObjectMapper().readValue(
                new ClassPathResource("IntygServiceTest/utlatande.json").getFile(), Utlatande.class);
    }
    
}
