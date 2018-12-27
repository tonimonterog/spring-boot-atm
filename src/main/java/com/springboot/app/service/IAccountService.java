package com.springboot.app.service;

import com.springboot.app.models.entity.Account;

public interface IAccountService {

	public Account checkBalance(Long number);
	
	public Object withdraw(Long number, Integer ammount);
}
