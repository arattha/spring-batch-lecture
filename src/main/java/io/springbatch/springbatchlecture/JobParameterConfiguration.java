package io.springbatch.springbatchlecture;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class JobParameterConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job BatchJob() {
        return this.jobBuilderFactory.get("Job")
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {


                        //StepContribution , ChunkContext 둘은 차이점을 가짐.
                        //해당 타입 반환     | Map 반환
                        //
                        //만약 Parameters 가 수정된다면 StepContribution 내에서는 확인 가능하지만, ChunkContext에서는 확인 불가능할 수 있다.

                        //StepContribution -> StepExecution -> JobExecution 내에 JobParameters 존재하여 해당 내부에서 참조 가능
                        JobParameters jobParameters = contribution.getStepExecution().getJobParameters();
                        String name = jobParameters.getString("name");
                        long seq = jobParameters.getLong("seq");
                        Date date = jobParameters.getDate("date");
                        double dob = jobParameters.getDouble("age");
                        //해당 타입의 객체 반환, 집어넣은 방식과 유사

                        System.out.println("===========================");
                        System.out.println("name:" + name);
                        System.out.println("seq: " + seq);
                        System.out.println("date: " + date);
                        System.out.println("date: " + dob);
                        System.out.println("===========================");

                        //ChunkContext -> StepContext -> StepExecution -> JobExecution 내에 JobParameters 존재하여 해당 내부에서 참조 가능
                        Map<String, Object> jobParameters2 = chunkContext.getStepContext().getJobParameters();
                        //Map으로 반환

                        System.out.println("MAP : " + jobParameters2.toString());

                        String name2 = (String)jobParameters2.get("name");
                        long seq2 = (long)jobParameters2.get("seq");

                        System.out.println("step1 has executed");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }
    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step1 has executed");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }
}
