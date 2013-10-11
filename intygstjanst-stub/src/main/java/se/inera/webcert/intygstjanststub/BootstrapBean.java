package se.inera.webcert.intygstjanststub;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import se.inera.ifv.insuranceprocess.healthreporting.getcertificateforcareresponder.v1.GetCertificateForCareResponseType;

public class BootstrapBean {
    private static final Logger LOG = LoggerFactory.getLogger(BootstrapBean.class);

    @Autowired
    private IntygStore intygStore;

    private Unmarshaller unmarshaller;
    private JAXBContext jaxbContext;

    @PostConstruct
    public void initData() {

        try {
            LOG.debug("Intygstjanst Stub : initializing intyg data...");
            jaxbContext = JAXBContext.newInstance(GetCertificateForCareResponseType.class);
            unmarshaller = jaxbContext.createUnmarshaller();

            List<Resource> files = getResourceListing("bootstrap-intyg/*.xml");
            for (Resource res : files) {
                addIntyg(res);
            }

        } catch (JAXBException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not bootstrap intygsdata for intygstjanststub", e);
        }

    }

    private List<Resource> getResourceListing(String classpathResourcePath) {
        try {
            PathMatchingResourcePatternResolver r = new PathMatchingResourcePatternResolver();
            return Arrays.asList(r.getResources(classpathResourcePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addIntyg(Resource res) throws JAXBException, IOException {

        GetCertificateForCareResponseType response = unmarshaller.unmarshal(new StreamSource(res.getInputStream()),
                GetCertificateForCareResponseType.class).getValue();

        intygStore.addIntyg(response);

    }

}
