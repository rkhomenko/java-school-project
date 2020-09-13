package org.khomenko.project.order.generator;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.khomenko.project.core.models.Customer;

@RestController
public class HelloController {
    @RequestMapping("/")
    public String index() {
        Customer customer = new Customer();
        return "Hello Spring Boot";
    }
}
