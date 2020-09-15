package org.khomenko.project.core.data.repositories;

import org.khomenko.project.core.data.models.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

}
