package com.app.SpringBatchExample.config;

import com.app.SpringBatchExample.entity.Customer;
import org.apache.el.stream.Stream;
import org.springframework.batch.item.ItemProcessor;

public class ProcessorConfig implements ItemProcessor<Customer,Customer> {

    @Override
    public Customer process(Customer customer) throws Exception {
        if (customer.getAge()<40) {
            return customer;
        }
        return null;
    }
}
