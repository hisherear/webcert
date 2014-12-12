package se.inera.webcert.persistence.intyg.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import se.inera.webcert.persistence.intyg.model.Intyg;
import se.inera.webcert.persistence.intyg.model.Signatur;
import se.inera.webcert.persistence.intyg.repository.util.IntygTestUtil;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:repository-context.xml" })
@ActiveProfiles({ "dev", "unit-testing" })
@Transactional
public class SignaturRepositoryTest {

    private static final String INTYG_ID = "abcd1234";

    @Autowired
    private SignaturRepository signaturRepository;

    @Autowired
    private IntygRepository intygRepository;

    @PersistenceContext
    private EntityManager em;

    @Before
    public void setup() {
        Intyg intyg = IntygTestUtil.buildIntyg(INTYG_ID);
        intygRepository.save(intyg);
    }

    @Test
    public void testPersistAndFindOne() {

        Signatur signatur = IntygTestUtil.buildSignatur(INTYG_ID, "Dr Dengroth", LocalDateTime.now());

        Signatur savedSignatur = signaturRepository.save(signatur);
        assertNotNull(savedSignatur);
        assertNotNull(savedSignatur.getIntygsId());

        String signaturId = savedSignatur.getIntygsId();

        Signatur foundSignatur = signaturRepository.findOne(signaturId);
        assertNotNull(foundSignatur);

    }

    @Test
    public void testFindByIntygsId() {

        Signatur signatur = IntygTestUtil.buildSignatur(INTYG_ID, "Dr Dengroth", LocalDateTime.now());
        signaturRepository.save(signatur);

        List<Signatur> signaturer = signaturRepository.findSignaturerForIntyg(INTYG_ID);

        assertThat(signaturer, hasSize(1));
    }

}
