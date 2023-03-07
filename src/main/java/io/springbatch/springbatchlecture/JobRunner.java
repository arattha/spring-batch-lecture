package io.springbatch.springbatchlecture;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JobRunner implements ApplicationRunner {
    //ApplicationRunner -> Boot 가 초기화 되고 완료되면 실행

    @Autowired
    private JobLauncher jobLauncher;//초기화된 후라 의존성 주입 가능

    @Autowired
    private Job job;//JobInstanceConf 의 Job 주입

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //기본적으로 SpringBoot 가 실행시키는 방식과 유사
        //이 경우 생성한 Job 이 Boot 가 실행하지 않도록 설정필요



        JobParameters jobParameters = new JobParametersBuilder()
                .addString("name", "user2")
                // A job instance already exists and is complete for parameters={name=user1}.
                // 2번 실행했을 때 나타나는 오류, 이미 있던 Instance 이기 때문
//               .addDate("reqDate", new Date())
                .toJobParameters();

        jobLauncher.run(job,jobParameters);
    }
}
