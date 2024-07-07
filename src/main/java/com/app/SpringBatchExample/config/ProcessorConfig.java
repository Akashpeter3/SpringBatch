package com.app.SpringBatchExample.config;

import com.app.SpringBatchExample.entity.Customer;
import org.springframework.batch.item.ItemProcessor;

public class ProcessorConfig implements ItemProcessor<Customer,Customer> {

    @Override
    public Customer process(Customer customer) throws Exception {
        return customer;
    }
}
