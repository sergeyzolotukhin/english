package ua.in.sz.english.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminService {
    private final JobLauncher jobLauncher;
    private final Job bookSentenceIndexing;

    public AdminService(JobLauncher jobLauncher, Job bookSentenceIndexing) {
        this.jobLauncher = jobLauncher;
        this.bookSentenceIndexing = bookSentenceIndexing;
    }

    @Async
    public void indexBook() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(bookSentenceIndexing, params);
        } catch (JobExecutionException e) {
            log.error("Can't processing book", e);
        }
    }
}
