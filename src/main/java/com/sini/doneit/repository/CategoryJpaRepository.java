package com.sini.doneit.repository;

import com.sini.doneit.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<Category,Long> {
}
