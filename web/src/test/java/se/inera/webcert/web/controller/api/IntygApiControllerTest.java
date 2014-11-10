package se.inera.webcert.web.controller.api;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.inera.webcert.hsa.model.WebCertUser;
import se.inera.webcert.persistence.intyg.model.Intyg;
import se.inera.webcert.persistence.intyg.model.IntygsStatus;
import se.inera.webcert.persistence.intyg.repository.IntygRepository;
import se.inera.webcert.service.intyg.IntygService;
import se.inera.webcert.service.intyg.dto.IntygItem;
import se.inera.webcert.test.TestIntygFactory;
import se.inera.webcert.web.controller.api.dto.ListIntygEntry;
import se.inera.webcert.web.service.WebCertUserService;

import javax.ws.rs.core.Response;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class IntygApiControllerTest {

    private static final String PNR = "19121212-1212";

    private static final String ENHET_ID = "ABC123";

    private static List<String> ENHET_IDS = Arrays.asList("ABC123", "DEF456");
    private static List<IntygsStatus> DRAFT_STATUSES = Arrays.asList(IntygsStatus.DRAFT_COMPLETE,
            IntygsStatus.DRAFT_INCOMPLETE);
    private static List<IntygsStatus> DRAFT_COMPLETE_STATUSES = Arrays.asList(IntygsStatus.DRAFT_COMPLETE);

    private static List<Intyg> intygDrafts = TestIntygFactory.createListWithIntygDrafts();

    private static List<IntygItem> intygSigned = TestIntygFactory.createListWithIntygItems();

    @Mock
    private WebCertUserService webCertUserService = mock(WebCertUserService.class);

    @Mock
    private IntygService intygService = mock(IntygService.class);

    @Mock
    private IntygRepository intygRepository = mock(IntygRepository.class);

    @InjectMocks
    private IntygApiController intygCtrl = new IntygApiController();

    @Before
    public void setupExpectations() {

        WebCertUser user = mock(WebCertUser.class);

        when(webCertUserService.getWebCertUser()).thenReturn(user);
        when(user.getIdsOfSelectedVardenhet()).thenReturn(ENHET_IDS);
        when(user.getValdVardenhet().getId()).thenReturn(ENHET_ID);
    }

    @Test
    public void testListIntyg() {

        // Mock call to Intygstjanst
        when(intygService.listIntyg(ENHET_IDS, PNR)).thenReturn(intygSigned);

        // Mock call to database
        when(intygRepository.findDraftsByPatientAndEnhetAndStatus(PNR, ENHET_IDS, DRAFT_STATUSES)).thenReturn(intygDrafts);

        Response response = intygCtrl.listDraftsAndIntygForPerson(PNR);

        List<ListIntygEntry> res = (List<ListIntygEntry>) response.getEntity();

        assertNotNull(res);
        assertEquals(4, res.size());
    }
}