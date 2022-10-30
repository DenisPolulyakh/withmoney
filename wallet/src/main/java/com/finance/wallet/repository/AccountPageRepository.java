package com.finance.wallet.repository;

import com.finance.wallet.model.Account;
import com.finance.wallet.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AccountPageRepository extends PagingAndSortingRepository<Account, Long> {
   Page<Account> findAllByState(String state, Pageable pageable);
}
