package org.khomenko.project.order.generator.init;

import org.khomenko.project.core.data.repositories.CustomerRepository;
import org.khomenko.project.core.data.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseIniterImpl implements DatabaseIniter {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void init() {

    }
}
