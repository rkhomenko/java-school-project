package org.khomenko.project.core.data.repositories;

import org.khomenko.project.core.data.models.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
