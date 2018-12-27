package com.springboot.app.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.app.models.dao.IAccountDao;
import com.springboot.app.models.entity.Account;

@Service
@Qualifier("account")
public class AccountServiceImpl implements IAccountService {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IAccountDao accountDao;

	@Override
	@Transactional(readOnly=true)
	public Account checkBalance(Long number) {
		return accountDao.findByNumber(number);
	}

	@Override
	@Transactional
	public Object withdraw(Long number, Integer ammount) {
		
		Account account = accountDao.findByNumber(number);
		log.info("Account before withdraw: "+account);
		
		account.setBalance(account.getBalance().subtract(new BigDecimal(ammount)));
		
		accountDao.save(account);
		log.info("Account after withdraw: "+account);
		
		return account;
	}

}
