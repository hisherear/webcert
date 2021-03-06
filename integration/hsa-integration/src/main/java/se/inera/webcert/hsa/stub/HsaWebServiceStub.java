package se.inera.webcert.hsa.stub;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Collections2;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.ifv.hsaws.v3.HsaWsFault;
import se.inera.ifv.hsaws.v3.HsaWsResponderInterface;
import se.inera.ifv.hsawsresponder.v3.AddressType;
import se.inera.ifv.hsawsresponder.v3.AttributeValueListType;
import se.inera.ifv.hsawsresponder.v3.AttributeValuePairType;
import se.inera.ifv.hsawsresponder.v3.CareUnitType;
import se.inera.ifv.hsawsresponder.v3.GetCareUnitListResponseType;
import se.inera.ifv.hsawsresponder.v3.GetCareUnitMembersResponseType;
import se.inera.ifv.hsawsresponder.v3.GetCareUnitResponseType;
import se.inera.ifv.hsawsresponder.v3.GetHospPersonResponseType;
import se.inera.ifv.hsawsresponder.v3.GetHospPersonType;
import se.inera.ifv.hsawsresponder.v3.GetHsaPersonHsaUserType;
import se.inera.ifv.hsawsresponder.v3.GetHsaPersonHsaUserType.HsaTitles;
import se.inera.ifv.hsawsresponder.v3.GetHsaPersonHsaUserType.SpecialityCodes;
import se.inera.ifv.hsawsresponder.v3.GetHsaPersonHsaUserType.SpecialityNames;
import se.inera.ifv.hsawsresponder.v3.GetHsaPersonResponseType;
import se.inera.ifv.hsawsresponder.v3.GetHsaPersonResponseType.UserInformations;
import se.inera.ifv.hsawsresponder.v3.GetHsaPersonType;
import se.inera.ifv.hsawsresponder.v3.GetHsaUnitResponseType;
import se.inera.ifv.hsawsresponder.v3.GetInformationListResponseType;
import se.inera.ifv.hsawsresponder.v3.GetInformationListType;
import se.inera.ifv.hsawsresponder.v3.GetMiuForPersonResponseType;
import se.inera.ifv.hsawsresponder.v3.GetMiuForPersonType;
import se.inera.ifv.hsawsresponder.v3.GetPriceUnitsForAuthResponseType;
import se.inera.ifv.hsawsresponder.v3.GetPriceUnitsForAuthType;
import se.inera.ifv.hsawsresponder.v3.HsawsSimpleLookupResponseType;
import se.inera.ifv.hsawsresponder.v3.HsawsSimpleLookupType;
import se.inera.ifv.hsawsresponder.v3.IsAuthorizedToSystemResponseType;
import se.inera.ifv.hsawsresponder.v3.IsAuthorizedToSystemType;
import se.inera.ifv.hsawsresponder.v3.LookupHsaObjectType;
import se.inera.ifv.hsawsresponder.v3.MiuInformationType;
import se.inera.ifv.hsawsresponder.v3.PingResponseType;
import se.inera.ifv.hsawsresponder.v3.PingType;
import se.inera.ifv.hsawsresponder.v3.VpwGetPublicUnitsResponseType;
import se.inera.ifv.hsawsresponder.v3.VpwGetPublicUnitsType;
import se.inera.webcert.hsa.model.AbstractVardenhet;
import se.inera.webcert.hsa.model.Mottagning;
import se.inera.webcert.hsa.model.Vardenhet;
import se.inera.webcert.hsa.model.Vardgivare;

import com.google.common.base.Function;

/**
 * @author johannesc
 */
public class HsaWebServiceStub implements HsaWsResponderInterface {

    @Autowired
    private HsaServiceStub hsaService;

    @Override
    public GetHsaUnitResponseType getHsaUnit(AttributedURIType logicalAddress, AttributedURIType id,
            LookupHsaObjectType parameters) throws HsaWsFault {
        if (parameters.getHsaIdentity().endsWith("-finns-ej")) {
            return null;
        }

        GetHsaUnitResponseType response = new GetHsaUnitResponseType();

        Vardenhet enhet = hsaService.getVardenhet(parameters.getHsaIdentity());
        if (enhet != null) {
            response.setHsaIdentity(enhet.getId());
            response.setName(enhet.getNamn());
            response.setStartDate(enhet.getStart());
            response.setEndDate(enhet.getEnd());
            updateWithContactInformation(response, enhet);
            return response;
        }

        Mottagning mottagning = hsaService.getMottagning(parameters.getHsaIdentity());
        if (mottagning != null) {
            response.setHsaIdentity(mottagning.getId());
            response.setName(mottagning.getNamn());
            response.setStartDate(mottagning.getStart());
            response.setEndDate(mottagning.getEnd());
            updateWithContactInformation(response, mottagning);
            return response;
        }
        return response;

    }

    private void updateWithContactInformation(GetHsaUnitResponseType response, AbstractVardenhet enhet) {
        response.setEmail(enhet.getEpost());
        if (enhet.getTelefonnummer() != null) {
            GetHsaUnitResponseType.TelephoneNumbers telephoneNumbers = new GetHsaUnitResponseType.TelephoneNumbers();
            telephoneNumbers.getTelephoneNumber().add(enhet.getTelefonnummer());
            response.setTelephoneNumbers(telephoneNumbers);
        }
        if (enhet.getPostadress() != null) {
            AddressType address = new AddressType();
            address.getAddressLine().add(enhet.getPostadress());
            response.setPostalAddress(address);
        }
        if (enhet.getPostnummer() != null) {
            response.getPostalAddress().getAddressLine().add(enhet.getPostnummer() + " " + enhet.getPostort());
        }
    }

    /**
     * Method used to get miuRights for a HoS Person.
     */
    @Override
    public GetMiuForPersonResponseType getMiuForPerson(AttributedURIType logicalAddress, AttributedURIType id,
            GetMiuForPersonType parameters) throws HsaWsFault {
        GetMiuForPersonResponseType response = new GetMiuForPersonResponseType();

        for (Medarbetaruppdrag medarbetaruppdrag : hsaService.getMedarbetaruppdrag()) {
            if (medarbetaruppdrag.getHsaId().equals(parameters.getHsaIdentity())) {
                response.getMiuInformation().addAll(
                        miuInformationTypesForEnhetsIds(medarbetaruppdrag));
            }
        }
        return response;
    }

    private List<MiuInformationType> miuInformationTypesForEnhetsIds(Medarbetaruppdrag medarbetaruppdrag) {
        List<MiuInformationType> informationTypes = new ArrayList<>();

        for (Vardgivare vardgivare : hsaService.getVardgivare()) {
            for (Vardenhet enhet : vardgivare.getVardenheter()) {
                for (Medarbetaruppdrag.Uppdrag uppdrag : medarbetaruppdrag.getUppdrag()) {
                    if (uppdrag.getEnhet().equals(enhet.getId())) {
                        for (String andamal : uppdrag.getAndamal()) {
                            MiuInformationType miuInfo = new MiuInformationType();
                            miuInfo.setHsaIdentity(medarbetaruppdrag.getHsaId());
                            miuInfo.setMiuPurpose(andamal);
                            miuInfo.setCareUnitHsaIdentity(enhet.getId());
                            miuInfo.setCareUnitName(enhet.getNamn());
                            miuInfo.setCareGiver(vardgivare.getId());
                            miuInfo.setCareGiverName(vardgivare.getNamn());
                            informationTypes.add(miuInfo);
                        }
                    }
                }
            }
        }
        for (Medarbetaruppdrag.Uppdrag uppdrag : medarbetaruppdrag.getUppdrag()) {
            if (uppdrag.getEnhet().endsWith("-finns-ej")) {
                for (String andamal : uppdrag.getAndamal()) {
                    MiuInformationType miuInfo = new MiuInformationType();
                    miuInfo.setHsaIdentity(medarbetaruppdrag.getHsaId());
                    miuInfo.setMiuPurpose(andamal);
                    miuInfo.setCareUnitHsaIdentity(uppdrag.getEnhet());
                    miuInfo.setCareUnitName("Enhet som inte finns");
                    miuInfo.setCareGiver(uppdrag.getVardgivare());
                    informationTypes.add(miuInfo);
                }
            }
        }
        return informationTypes;
    }

    /**
     * Returns work place code.
     */
    @Override
    public HsawsSimpleLookupResponseType hsawsSimpleLookup(AttributedURIType logicalAddress, AttributedURIType id,
            HsawsSimpleLookupType parameters) throws HsaWsFault {

        HsawsSimpleLookupResponseType response = new HsawsSimpleLookupResponseType();

        String attribute = parameters.getLookup().getSearchAttribute();
        String enhetsId = parameters.getLookup().getValue();
        if (attribute.equals("hsaIdentity")) {
            AttributeValueListType result = createAttributeValueListForEnhet(enhetsId);
            if (result != null) {
                response.getResponseValues().add(result);
            }
        } else if (attribute.equals("unitPrescriptionCode")) {
            AttributeValueListType result = createAttributeValueListForEnhet(parameters.getLookup().getValue());
            if (result != null) {
                response.getResponseValues().add(result);
            }
        }
        return response;
    }

    private AttributeValueListType createAttributeValueListForEnhet(String enhetsId) {
        Vardenhet vardenhet = hsaService.getVardenhet(enhetsId);

        if (vardenhet == null) {
            return null;
        }

        AttributeValueListType attributeList = new AttributeValueListType();
        attributeList.setDN(enhetsId);

        AttributeValuePairType identityValue = new AttributeValuePairType();
        identityValue.setAttribute("hsaIdentity");
        identityValue.getValue().add(enhetsId);
        attributeList.getResponse().add(identityValue);

        Collection<String> mottagningsId = Collections2.transform(vardenhet.getMottagningar(),
                new Function<Mottagning, String>() {
                    public String apply(Mottagning mottagning) {
                        return mottagning.getId();
                    }
                }
                );

        AttributeValuePairType membersAttribute = new AttributeValuePairType();
        membersAttribute.setAttribute("hsaHealthCareUnitMember");
        membersAttribute.getValue().addAll(mottagningsId);
        attributeList.getResponse().add(membersAttribute);

        AttributeValuePairType arbetsplatskod = new AttributeValuePairType();
        arbetsplatskod.setAttribute("unitPrescriptionCode");
        arbetsplatskod.getValue().add(vardenhet.getArbetsplatskod());
        attributeList.getResponse().add(arbetsplatskod);

        return attributeList;
    }

    /**
     * Method to retrieve data for a hsa unit.
     */
    @Override
    public GetCareUnitResponseType getCareUnit(AttributedURIType logicalAddress, AttributedURIType id,
            LookupHsaObjectType parameters) throws HsaWsFault {

        for (Vardgivare vardgivare : hsaService.getVardgivare()) {
            for (Vardenhet vardenhet : vardgivare.getVardenheter()) {
                if (vardenhet.getId().equals(parameters.getHsaIdentity())) {
                    GetCareUnitResponseType response = new GetCareUnitResponseType();
                    response.setCareUnitHsaIdentity(vardenhet.getId());
                    response.setCareGiver(vardgivare.getId());
                    return response;
                }
                for (Mottagning mottagning : vardenhet.getMottagningar()) {
                    if (mottagning.getId().equals(parameters.getHsaIdentity())) {
                        GetCareUnitResponseType response = new GetCareUnitResponseType();
                        response.setCareUnitHsaIdentity(vardenhet.getId());
                        response.setCareGiver(vardgivare.getId());
                        return response;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public IsAuthorizedToSystemResponseType isAuthorizedToSystem(AttributedURIType logicalAddress,
            AttributedURIType id, IsAuthorizedToSystemType parameters) throws HsaWsFault {
        return null;
    }

    @Override
    public VpwGetPublicUnitsResponseType vpwGetPublicUnits(AttributedURIType logicalAddress, AttributedURIType id,
            VpwGetPublicUnitsType parameters) throws HsaWsFault {
        return null;
    }

    @Override
    public GetCareUnitListResponseType getCareUnitList(AttributedURIType logicalAddress, AttributedURIType id,
            LookupHsaObjectType parameters) throws HsaWsFault {

        GetCareUnitListResponseType response = new GetCareUnitListResponseType();

        for (Vardgivare vardgivare : hsaService.getVardgivare()) {
            for (Vardenhet enhet : vardgivare.getVardenheter()) {
                if (enhet.getId().equals(parameters.getSearchBase())) {
                    response.setCareUnitGiverHsaIdentity(vardgivare.getId());
                    response.setCareUnitGiverName(vardgivare.getNamn());

                    CareUnitType careUnit = new CareUnitType();
                    careUnit.setHsaIdentity(enhet.getId());
                    careUnit.setCareUnitName(enhet.getNamn());
                    careUnit.setCareUnitStartDate(enhet.getStart());
                    careUnit.setCareUnitEndDate(enhet.getEnd());
                    response.setCareUnits(new GetCareUnitListResponseType.CareUnits());
                    response.getCareUnits().getCareUnit().add(careUnit);

                    return response;
                }
            }
        }
        return response;
    }

    @Override
    public GetPriceUnitsForAuthResponseType getPriceUnitsForAuth(AttributedURIType logicalAddress,
            AttributedURIType id, GetPriceUnitsForAuthType parameters) throws HsaWsFault {
        return null;
    }

    @Override
    public GetHsaPersonResponseType getHsaPerson(AttributedURIType logicalAddress, AttributedURIType id,
            GetHsaPersonType parameters) throws HsaWsFault {

        String hsaId = parameters.getHsaIdentity();

        HsaPerson hsaPerson = hsaService.getHsaPerson(hsaId);

        GetHsaPersonHsaUserType user = new GetHsaPersonHsaUserType();
        user.setSn(hsaPerson.getEfterNamn());
        user.setGivenName(hsaPerson.getForNamn());
        user.setHsaIdentity(hsaPerson.getHsaId());

        SpecialityCodes userSpecCodes = new SpecialityCodes();
        SpecialityNames userSpecNames = new SpecialityNames();

        for (HsaSpecialicering spec : hsaPerson.getSpecialiseringar()) {
            userSpecCodes.getSpecialityCode().add(spec.getKod());
            userSpecNames.getSpecialityName().add(spec.getNamn());
        }

        user.setSpecialityCodes(userSpecCodes);
        user.setSpecialityNames(userSpecNames);

        user.setTitle(hsaPerson.getTitel());

        HsaTitles userHsaTitles = new HsaTitles();

        userHsaTitles.getHsaTitle().addAll(hsaPerson.getLegitimeradeYrkesgrupper());

        user.setHsaTitles(userHsaTitles);

        UserInformations userInfos = new UserInformations();
        userInfos.getUserInformation().add(user);

        GetHsaPersonResponseType response = new GetHsaPersonResponseType();
        response.setUserInformations(userInfos);

        return response;
    }

    @Override
    public PingResponseType ping(AttributedURIType logicalAddress, AttributedURIType id, PingType parameters)
            throws HsaWsFault {
        PingResponseType result = new PingResponseType();
        result.setResponseTime(BigInteger.TEN);
        result.setMessage("Fake ping response.");
        return result;
    }

    @Override
    public GetCareUnitMembersResponseType getCareUnitMembers(AttributedURIType logicalAddress, AttributedURIType id,
            LookupHsaObjectType parameters) throws HsaWsFault {
        return null;
    }

    @Override
    public GetHospPersonResponseType getHospPerson(AttributedURIType logicalAddress, AttributedURIType id,
            GetHospPersonType parameters) throws HsaWsFault {
        return null;
    }

    @Override
    public GetInformationListResponseType getInformationList(AttributedURIType logicalAddress, AttributedURIType id,
            GetInformationListType parameters) throws HsaWsFault {
        return null;
    }
}
