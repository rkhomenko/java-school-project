package org.khomenko.project.core.data.repositories;

import org.khomenko.project.core.data.models.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
