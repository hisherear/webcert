package se.inera.certificate.mc2wc.batch.job;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import se.inera.certificate.mc2wc.batch.writer.MockMigrationRecieverBean;
import se.inera.certificate.mc2wc.dbunit.AbstractDbUnitSpringTest;
import se.inera.certificate.mc2wc.dbunit.CustomFlatXmlDataSetLoader;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;

@DbUnitConfiguration(databaseConnection = "medcertDataSource", dataSetLoader = CustomFlatXmlDataSetLoader.class)
@DatabaseSetup({"/data/certificate_dataset_25.xml"})
public class MigrationJobTest extends AbstractDbUnitSpringTest {

    private Logger logger = LoggerFactory.getLogger(MigrationJobTest.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("migrationJob")
    private Job migrationJob;
    
    @Autowired
    private MockMigrationRecieverBean recieverBean;

    @Test
    public void testRunMigrationJob() throws Exception {

        JobParametersBuilder builder = new JobParametersBuilder();
        builder.addString("sender", "SLL");
        final JobExecution execution = jobLauncher.run(migrationJob, builder.toJobParameters());

        await().atMost(10, TimeUnit.SECONDS).until(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                logger.debug("exitStatus: {}", execution.getExitStatus());
                return execution.getExitStatus().equals(ExitStatus.COMPLETED);
            }
        });

        assertEquals(13, recieverBean.getMessages().size());
    }
}