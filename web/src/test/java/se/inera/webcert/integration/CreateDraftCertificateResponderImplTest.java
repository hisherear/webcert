package se.inera.webcert.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.inera.ifv.hsawsresponder.v3.MiuInformationType;
import se.inera.webcert.hsa.services.HsaPersonService;
import se.inera.webcert.integration.builder.CreateNewDraftRequestBuilder;
import se.inera.webcert.integration.registry.IntegreradeEnheterRegistry;
import se.inera.webcert.integration.registry.dto.IntegreradEnhetEntry;
import se.inera.webcert.integration.validator.CreateDraftCertificateValidator;
import se.inera.webcert.integration.validator.ResultValidator;
import se.inera.webcert.persistence.utkast.model.Utkast;
import se.inera.webcert.persistence.utkast.model.UtkastStatus;
import se.inera.webcert.persistence.utkast.model.VardpersonReferens;
import se.inera.webcert.service.dto.Vardenhet;
import se.inera.webcert.service.dto.Vardgivare;
import se.inera.webcert.service.utkast.UtkastService;
import se.inera.webcert.service.utkast.dto.CreateNewDraftRequest;
import se.riv.clinicalprocess.healthcond.certificate.createdraftcertificateresponder.v1.CreateDraftCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.createdraftcertificateresponder.v1.CreateDraftCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.createdraftcertificateresponder.v1.Enhet;
import se.riv.clinicalprocess.healthcond.certificate.createdraftcertificateresponder.v1.HosPersonal;
import se.riv.clinicalprocess.healthcond.certificate.createdraftcertificateresponder.v1.Patient;
import se.riv.clinicalprocess.healthcond.certificate.createdraftcertificateresponder.v1.Utlatande;
import se.riv.clinicalprocess.healthcond.certificate.types.v1.HsaId;
import se.riv.clinicalprocess.healthcond.certificate.types.v1.PersonId;
import se.riv.clinicalprocess.healthcond.certificate.types.v1.TypAvUtlatande;
import se.riv.clinicalprocess.healthcond.certificate.v1.ResultCodeType;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CreateDraftCertificateResponderImplTest {

    private static final String LOGICAL_ADDR = "1234567890";

    private static final String USER_HSAID      = "SE1234567890";
    private static final String UNIT_HSAID      = "SE0987654321";
    private static final String CAREGIVER_HSAID = "SE0000112233";

    private static final String UTKAST_ID      = "abc123";
    private static final String UTKAST_VERSION = "1";
    private static final String UTKAST_TYPE    = "fk7263";
    private static final String UTKAST_JSON    = "A bit of text representing json";


    @Mock
    UtkastService mockUtkastService;

    @Mock
    HsaPersonService mockHsaPersonService;

    @Mock
    CreateNewDraftRequestBuilder mockRequestBuilder;

    @Mock
    CreateDraftCertificateValidator mockValidator;

    @Mock
    IntegreradeEnheterRegistry mockIntegreradeEnheterService;

    @InjectMocks
    CreateDraftCertificateResponderImpl responder;

    /**
     * When a new certificate draft is being created the caller
     * should get a success response returned and any stakeholder
     * should be notified with a notification message:
     */
    @Test
    public void whenNewCertificateDraftSuccessResponse() {

        // Given
        ResultValidator resultsValidator = new ResultValidator();
        List<MiuInformationType> miuList = Arrays.asList(createMIU(USER_HSAID, UNIT_HSAID, LocalDateTime.now().plusYears(2)));
        Vardgivare vardgivare = createVardgivare();
        Vardenhet  vardenhet = createVardenhet(vardgivare);
        CreateNewDraftRequest draftRequest = createCreateNewDraftRequest(vardenhet);
        CreateDraftCertificateType certificateType = createCertificateType();

        VardpersonReferens vardperson = createVardpersonReferens(
                certificateType.getUtlatande().getSkapadAv().getPersonalId().getRoot(),
                certificateType.getUtlatande().getSkapadAv().getFullstandigtNamn());

        Utkast utkast = createUtkast(UTKAST_ID, Long.parseLong(UTKAST_VERSION), UTKAST_TYPE, UtkastStatus.DRAFT_INCOMPLETE, UTKAST_JSON, vardperson);

        //When
        when(mockValidator.validate(any(Utlatande.class))).thenReturn(resultsValidator);
        when(mockHsaPersonService.checkIfPersonHasMIUsOnUnit(USER_HSAID, UNIT_HSAID)).thenReturn(miuList);
        when(mockRequestBuilder.buildCreateNewDraftRequest(any(Utlatande.class), any(MiuInformationType.class))).thenReturn(draftRequest);
        when(mockUtkastService.createNewDraft(any(CreateNewDraftRequest.class))).thenReturn(utkast);
        when(mockIntegreradeEnheterService.addIfNotExistsIntegreradEnhet(any(IntegreradEnhetEntry.class))).thenReturn(Boolean.TRUE);

        //Then
        CreateDraftCertificateResponseType response = responder.createDraftCertificate(LOGICAL_ADDR, certificateType);

        verify(mockUtkastService).createNewDraft(any(CreateNewDraftRequest.class));

        // Assert response content
        assertNotNull(response);
        assertEquals(response.getResult().getResultCode(), ResultCodeType.OK);
        assertEquals(response.getUtlatandeId().getExtension(), UTKAST_ID);
    }

    private VardpersonReferens createVardpersonReferens(String hsaId, String name) {
        VardpersonReferens vardperson = new VardpersonReferens();
        vardperson.setHsaId(hsaId);
        vardperson.setNamn(name);
        return vardperson;
    }

    private CreateNewDraftRequest createCreateNewDraftRequest(Vardenhet vardenhet) {
        CreateNewDraftRequest draftRequest = new CreateNewDraftRequest();
        draftRequest.setIntygId(UTKAST_ID);
        draftRequest.setVardenhet(vardenhet);
        return draftRequest;
    }

    private Vardenhet createVardenhet(Vardgivare vardgivare) {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setHsaId("SE1234567890-1A01");
        vardenhet.setNamn("Vardenheten");
        vardenhet.setVardgivare(vardgivare);
        return vardenhet;
    }

    private Vardgivare createVardgivare() {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setHsaId("SE1234567890-2B01");
        vardgivare.setNamn("Vardgivaren");
        return vardgivare;
    }

    private CreateDraftCertificateType createCertificateType() {

        // Type
        TypAvUtlatande utlTyp = new TypAvUtlatande();
        utlTyp.setCode("fk7263");

        // HoSPerson
        HsaId userHsaId = new HsaId();
        userHsaId.setExtension(USER_HSAID);
        userHsaId.setRoot("USERHSAID");

        HsaId unitHsaId = new HsaId();
        unitHsaId.setExtension(UNIT_HSAID);
        unitHsaId.setRoot("UNITHSAID");

        Enhet hosEnhet = new Enhet();
        hosEnhet.setEnhetsId(unitHsaId);

        HosPersonal hosPerson = new HosPersonal();
        hosPerson.setFullstandigtNamn("Abel Baker");
        hosPerson.setPersonalId(userHsaId);
        hosPerson.setEnhet(hosEnhet);

        // Patient
        PersonId personId = new PersonId();
        personId.setRoot("PERSNR");
        personId.setExtension("19121212-1212");

        Patient patType = new Patient();
        patType.setPersonId(personId);
        patType.getFornamn().add("Adam");
        patType.getFornamn().add("Bertil");
        patType.getMellannamn().add("Cesarsson");
        patType.getMellannamn().add("Davidsson");
        patType.setEfternamn("Eriksson");

        Utlatande utlatande = new Utlatande();
        utlatande.setTypAvUtlatande(utlTyp);
        utlatande.setSkapadAv(hosPerson);
        utlatande.setPatient(patType);

        CreateDraftCertificateType certificateType = new CreateDraftCertificateType();
        certificateType.setUtlatande(utlatande);

        return certificateType;
    }

    private MiuInformationType createMIU(String personHsaId, String unitHsaId, LocalDateTime miuEndDate) {

        MiuInformationType miu = new MiuInformationType();
        miu.setCareGiver(CAREGIVER_HSAID);
        miu.setCareGiverName("Landstinget");
        miu.setCareUnitName("Sjukhuset");
        miu.setCareUnitHsaIdentity(unitHsaId);
        miu.setCareUnitEndDate(miuEndDate);
        miu.setHsaIdentityPerson(personHsaId);

        return miu;
    }

    private Utkast createUtkast(String intygId, long version, String type, UtkastStatus status, String model, VardpersonReferens vardperson) {

        Utkast utkast = new Utkast();
        utkast.setIntygsId(intygId);
        utkast.setVersion(version);
        utkast.setIntygsTyp(type);
        utkast.setStatus(status);
        utkast.setModel(model);
        utkast.setSkapadAv(vardperson);
        utkast.setSenastSparadAv(vardperson);

        return utkast;
    }

}
