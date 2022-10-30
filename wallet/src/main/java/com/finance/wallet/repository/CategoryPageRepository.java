package com.finance.wallet.repository;

import com.finance.wallet.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoryPageRepository extends PagingAndSortingRepository<Category, Long> {
   Page<Category> findAllByState(String state, Pageable pageable);
}
