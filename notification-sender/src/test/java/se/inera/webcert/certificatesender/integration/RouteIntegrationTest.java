package se.inera.webcert.certificatesender.integration;

import static com.jayway.awaitility.Awaitility.await;

import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.jms.*;

import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import se.inera.webcert.certificatesender.services.mock.MockSendCertificateServiceClientImpl;
import se.inera.webcert.common.Constants;

import com.google.common.base.Throwables;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration("/certificates/integration-test-certificate-sender-config.xml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RouteIntegrationTest {
    private static final Logger LOG = LoggerFactory.getLogger(RouteIntegrationTest.class);

    private static final int SECONDS_TO_WAIT = 10;

    private static final String INTYGS_ID_1 = "intygsId1";

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("certificateQueue")
    private Queue sendQueue;

    @Autowired
    private Queue dlq;

    @Autowired
    MockSendCertificateServiceClientImpl sendCertificateServiceClient;

    @Before
    public void resetStub() {
        sendCertificateServiceClient.reset();
    }

    @Test
    public void ensureStubReceivesAllMessages() throws Exception {
        sendMessage(INTYGS_ID_1, Constants.SEND_MESSAGE);
        sendMessage(INTYGS_ID_1, Constants.SEND_MESSAGE);
        sendMessage(INTYGS_ID_1, Constants.SEND_MESSAGE);

        await().atMost(SECONDS_TO_WAIT, TimeUnit.SECONDS).until(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                int numberOfReceivedMessages = sendCertificateServiceClient.getNumberOfReceivedMessages();
                System.out.println("numberOfReceivedMessages: " + numberOfReceivedMessages);
                return (numberOfReceivedMessages == 3);
            }
        });
    }

    @Test
    public void ensureStubReceivesAllMessagesAfterResend() throws Exception {
        sendMessage(MockSendCertificateServiceClientImpl.FALLERAT_MEDDELANDE + "2", Constants.SEND_MESSAGE);
        sendMessage(INTYGS_ID_1, Constants.SEND_MESSAGE);

        await().atMost(SECONDS_TO_WAIT, TimeUnit.SECONDS).until(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                int numberOfSentMessages = sendCertificateServiceClient.getNumberOfSentMessages();
                System.out.println("numberOfReceivedMessages: " + numberOfSentMessages);
                return (numberOfSentMessages == 2);
            }
        });
    }

    @Test
    public void ensureMessageEndsUpInDLQ() throws Exception {
        sendMessage(MockSendCertificateServiceClientImpl.FALLERAT_MEDDELANDE + "5", Constants.SEND_MESSAGE);

        await().atMost(SECONDS_TO_WAIT, TimeUnit.SECONDS).until(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                int numberOfDLQMessages = numberOfDLQMessages();
                return (numberOfDLQMessages == 1);
            }
        });
    }

    private void sendMessage(final String intygsId, final String messageType) throws Exception {
        jmsTemplate.send(sendQueue, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                try {
                    TextMessage textMessage = session.createTextMessage("body");
                    textMessage.setStringProperty(Constants.INTYGS_ID, intygsId);
                    textMessage.setStringProperty(Constants.MESSAGE_TYPE, messageType);
                    return textMessage;
                } catch (Exception e) {
                    throw Throwables.propagate(e);
                }
            }
        });
    }

    private int numberOfDLQMessages() throws Exception {
        Integer count = (Integer) jmsTemplate.browse(dlq, new BrowserCallback<Object>() {

            @Override
            public Object doInJms(Session session, QueueBrowser browser) throws JMSException {
                int counter = 0;
                Enumeration msgs = browser.getEnumeration();
                while (msgs.hasMoreElements()) {
                    msgs.nextElement();
                    counter++;
                }
                return counter;
            }
        });
        return count;
    }

}
