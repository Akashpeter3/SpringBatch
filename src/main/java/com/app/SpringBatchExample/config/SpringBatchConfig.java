package com.app.SpringBatchExample.config;

import com.app.SpringBatchExample.entity.Customer;
import com.app.SpringBatchExample.repository.CustomerRepo;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Repository;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringBatchConfig {


    private JobBuilderFactory jobBuilderFactory;


    private StepBuilderFactory stepBuilderFactory;


    private CustomerRepo customerRepo;

    @Bean
    public FlatFileItemReader<Customer>flatFileItemReader(){

        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/sample_data.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return  itemReader;
    }

    private LineMapper<Customer> lineMapper() {
        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id","name","age","email");

        BeanWrapperFieldSetMapper<Customer>fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;


    }

    @Bean
    public  ProcessorConfig processorConfig(){
        return new ProcessorConfig();
    }


    @Bean
    public RepositoryItemWriter<Customer>writer(){
        RepositoryItemWriter<Customer> writer = new RepositoryItemWriter<>();
        writer.setRepository(customerRepo);
        writer.setMethodName("save");
        return writer;

    }

    @Bean
    public Step step1(){
    return  stepBuilderFactory.get("csv-step").<Customer,Customer>chunk(10)
                .reader(flatFileItemReader())
                .processor(processorConfig())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .build();

    }

    @Bean
    public Job job(){
       return jobBuilderFactory.get("importCustomerJob")
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor(){
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }



}
