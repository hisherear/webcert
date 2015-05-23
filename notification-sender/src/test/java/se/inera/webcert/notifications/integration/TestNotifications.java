package se.inera.webcert.notifications.integration;

import static com.jayway.awaitility.Awaitility.await;
import static se.inera.webcert.notifications.stub.CertificateStatusUpdateForCareResponderStub.FALLERAT_MEDDELANDE;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.camel.CamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.support.api.notification.FragorOchSvar;
import se.inera.certificate.modules.support.api.notification.HandelseType;
import se.inera.certificate.modules.support.api.notification.NotificationMessage;
import se.inera.webcert.notifications.stub.CertificateStatusUpdateForCareResponderStub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/test-notification-sender-config.xml", "/spring/integration-test-properties-context.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class TestNotifications {
    private static final Logger LOG = LoggerFactory.getLogger(TestNotifications.class);

    private static final int SECONDS_TO_WAIT = 10;

    private static final String INTYG_JSON = "{\"id\":\"1234\",\"typ\":\"fk7263\"}";

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private Queue queue;

    @Autowired
    private CertificateStatusUpdateForCareResponderStub certificateStatusUpdateForCareResponderStub;

    ObjectMapper objectMapper = new CustomObjectMapper();

    @Before
    public void resetStub() {
        this.certificateStatusUpdateForCareResponderStub.reset();
    }

    @Test
    public void ensureStubReceivedAllMessages() throws Exception {
        NotificationMessage notificationMessage1 = createNotificationMessage("intyg1", HandelseType.INTYGSUTKAST_SKAPAT);
        NotificationMessage notificationMessage2 = createNotificationMessage("intyg2", HandelseType.INTYGSUTKAST_ANDRAT);
        NotificationMessage notificationMessage3 = createNotificationMessage("intyg3", HandelseType.INTYGSUTKAST_SIGNERAT);

        sendMessage(notificationMessage1);
        sendMessage(notificationMessage2);
        sendMessage(notificationMessage3);

        await().atMost(SECONDS_TO_WAIT, TimeUnit.SECONDS).until(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                int numberOfReceivedMessages = certificateStatusUpdateForCareResponderStub.getNumberOfReceivedMessages();
                System.out.println("numberOfReceivedMessages: " + numberOfReceivedMessages);
                return (numberOfReceivedMessages == 3);
            }
        });
    }

    @Test
    public void ensureMessagesAreResentAndDoNotBlockEachOther() throws Exception {
        final String intygsId1 = FALLERAT_MEDDELANDE + "-2";
        final String intygsId2 = "korrekt-meddelande-1";
        NotificationMessage notificationMessage1 = createNotificationMessage(intygsId1, HandelseType.INTYGSUTKAST_SKAPAT);
        NotificationMessage notificationMessage2 = createNotificationMessage(intygsId1, HandelseType.INTYGSUTKAST_ANDRAT);

        sendMessage(notificationMessage1);
        LOG.info("Message 1 sent");
        sendMessage(notificationMessage2);
        LOG.info("Message 2 sent");

        await().atMost(SECONDS_TO_WAIT, TimeUnit.SECONDS).until(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                int numberOfSuccessfulMessages = certificateStatusUpdateForCareResponderStub.getNumberOfSentMessages();
                if (numberOfSuccessfulMessages == 2) {
                    List<String> utlatandeIds = certificateStatusUpdateForCareResponderStub.getIntygsIdsInOrder();
                    return (utlatandeIds.size() == 2 &&
                            utlatandeIds.get(0) == intygsId2 &&
                            utlatandeIds.get(1) == intygsId1);
                }
                return false;
            }
        });
    }

    private NotificationMessage createNotificationMessage(String intygsId1, HandelseType handelseType) {
        return new NotificationMessage(intygsId1, "FK7263", new LocalDateTime(),
                handelseType, "address2", INTYG_JSON, new FragorOchSvar(0, 0, 0, 0));
    }

    private String notificationMessageToJson(NotificationMessage notificationMessage) throws Exception {
        return objectMapper.writeValueAsString(notificationMessage);
    }

    private void sendMessage(final NotificationMessage message) throws Exception {
        jmsTemplate.send(queue, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                try {
                    return session.createTextMessage(notificationMessageToJson(message));
                } catch (Exception e) {
                    throw Throwables.propagate(e);
                }
            }
        });
    }

}
