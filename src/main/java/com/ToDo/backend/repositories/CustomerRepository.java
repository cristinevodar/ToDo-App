package com.ToDo.backend.repositories;

import com.ToDo.backend.data.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
