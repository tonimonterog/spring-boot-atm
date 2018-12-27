package com.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.springboot.app.models.entity.Account;

public interface IAccountDao extends CrudRepository<Account, Long> {
	
	@Query("select a from Account a where a.number = ?1")
	public Account findByNumber(Long number);

}
