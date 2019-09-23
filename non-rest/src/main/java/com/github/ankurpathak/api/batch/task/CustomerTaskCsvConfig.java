package com.github.ankurpathak.api.batch.task;

import com.github.ankurpathak.api.batch.item.processor.DomainItemProcessor;
import com.github.ankurpathak.api.batch.item.processor.callback.IPostProcess;
import com.github.ankurpathak.api.batch.item.processor.callback.IPreProcess;
import com.github.ankurpathak.api.batch.item.processor.listener.DomainItemProcessListener;
import com.github.ankurpathak.api.batch.item.reader.DomainItemReader;
import com.github.ankurpathak.api.batch.item.reader.listener.DomainItemReadListener;
import com.github.ankurpathak.api.batch.item.writer.DomainItemWriter;
import com.github.ankurpathak.api.batch.item.writer.listener.DomainItemWriteListener;
import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.converter.CustomerConverters;
import com.github.ankurpathak.api.domain.converter.IToDomain;
import com.github.ankurpathak.api.domain.model.*;
import com.github.ankurpathak.api.exception.NotFoundException;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controllor.dto.CustomerDto;
import com.github.ankurpathak.api.service.ICustomerService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.Optional;

@Configuration
public class CustomerTaskCsvConfig extends AbstractDomainCsvTaskConfig<Customer, CustomerId, CustomerDto> {

    @Autowired
    private GenericApplicationContext applicationContext;
    @Autowired
    private ICustomerService customerService;

    public interface CustomerTask{
        String CSV_CUSTOMER_STEP = String.format("%s_STEP", Task.TaskType.CSV_CUSTOMER);
        String CSV_CUSTOMER_READER = String.format("%s_READER", Task.TaskType.CSV_CUSTOMER);
        String CSV_CUSTOMER_READ_LISTNER = String.format("%s_READ_LISTNER", Task.TaskType.CSV_CUSTOMER);
        String CSV_CUSTOMER_WRITER = String.format("%s_WRITER", Task.TaskType.CSV_CUSTOMER);
        String CSV_CUSTOMER_WRITE_LISTENER = String.format("%s_WRITE_LISTENER", Task.TaskType.CSV_CUSTOMER);
        String CSV_CUSTOMER_PROCESSOR = String.format("%s_PROCESSOR", Task.TaskType.CSV_CUSTOMER);
        String CSV_CUSTOMER_PROCESS_LISTENER = String.format("%s_PROCESS_LISTENER", Task.TaskType.CSV_CUSTOMER);
    }

    @PostConstruct
    public void init() throws Exception{
        DomainItemReader<CustomerDto, Customer, CustomerId> itemReader = itemReader();
        applicationContext.registerBean(CustomerTask.CSV_CUSTOMER_READER, DomainItemReader.class, () -> itemReader);
        DomainItemReadListener<CustomerDto, CustomerId, Customer> itemReadListener = itemReadListener();
        applicationContext.registerBean(CustomerTask.CSV_CUSTOMER_READ_LISTNER, DomainItemReadListener.class, () -> itemReadListener);
        DomainItemWriter<Customer, CustomerId> itemWriter = itemWriter();
        applicationContext.registerBean(CustomerTask.CSV_CUSTOMER_WRITER, DomainItemWriter.class, () -> itemWriter);
        DomainItemProcessor<CustomerDto, CustomerId, Customer> itemProcessor = itemProcessor(
                ((processor, dto) -> {
                    Optional<Business> business = TaskContextHolder.getContext()
                            .map(ITaskContext::getBusiness);
                    if(business.isPresent()){
                        User user = customerService.processUser(business.get(), dto);
                        dto.userId(user.getId());
                        dto.setBusinessId(business.get().getId());
                    }else {
                        throw new NotFoundException(TaskContextHolder.getContext().map(ITaskContext::getRequestedBusinessId).orElse(String.valueOf(BigInteger.ZERO)), Params.ID, "Business", ApiCode.BUSINESS_NOT_FOUND );
                    }
                }),
                IPostProcess.postProcess());
        applicationContext.registerBean(CustomerTask.CSV_CUSTOMER_PROCESSOR, DomainItemProcessor.class, () -> itemProcessor);
        DomainItemProcessListener<CustomerDto, CustomerId, Customer> itemProcessListener = itemProcessListener();
        applicationContext.registerBean(CustomerTask.CSV_CUSTOMER_PROCESS_LISTENER, DomainItemProcessListener.class, () -> itemProcessListener);
        applicationContext.registerBean(CustomerTask.CSV_CUSTOMER_PROCESS_LISTENER, DomainItemProcessListener.class, () -> itemProcessListener);
        DomainItemWriteListener<Customer, CustomerId, CustomerDto> itemWriteListener = itemWriteListener();
        applicationContext.registerBean(CustomerTask.CSV_CUSTOMER_WRITE_LISTENER, DomainItemWriteListener.class, () -> itemWriteListener);
        Step step = step(CustomerTask.CSV_CUSTOMER_STEP, itemReader, itemProcessor, itemWriter, itemReadListener, itemProcessListener, itemWriteListener);
        applicationContext.registerBean(CustomerTask.CSV_CUSTOMER_STEP, Step.class, () -> step);
        Job job = job(Task.TaskType.CSV_CUSTOMER, step);
        applicationContext.registerBean(Task.TaskType.CSV_CUSTOMER, Job.class, () -> job);
    }

    @Override
    protected Class<CustomerDto> getDtoType() {
        return CustomerDto.class;
    }

    @Override
    protected Class<Customer> getType() {
        return Customer.class;
    }

    @Override
    protected IToDomain<Customer, CustomerId, CustomerDto> getConverter() {
        return CustomerConverters.createOne;
    }
}
