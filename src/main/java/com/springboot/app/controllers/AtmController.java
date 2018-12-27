package com.springboot.app.controllers;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.springboot.app.controllers.entity.JSONResponse;
import com.springboot.app.models.entity.Account;
import com.springboot.app.service.IAccountService;

@RestController
public class AtmController {
	
	@Autowired
	@Qualifier("atm")
	private IAccountService accountService;
	
	@GetMapping(value="/balance",  produces = "application/json")
	public ResponseEntity<Object> getBalance(@RequestParam Long accountNumber) {
		Gson gson = new Gson();
		JSONResponse jResponse = new JSONResponse();
		
		Account account = accountService.checkBalance(accountNumber);
		
		if(account == null) {
			jResponse.setStatus("error");
			jResponse.addError(new Error("The account requested does not exist"));
			return new ResponseEntity<Object>(gson.toJson(jResponse), HttpStatus.OK);
		}
		
		NumberFormat GBP = NumberFormat.getCurrencyInstance(Locale.UK);
		
		jResponse.setStatus("ok");
		jResponse.setResp("Price in GBP : " + GBP.format(account.getBalance()));
		
		return new ResponseEntity<Object>(gson.toJson(jResponse), HttpStatus.OK);
	}
	
	@GetMapping(value="/withdraw", produces = "application/json")
	public ResponseEntity<Object> withdraw(@RequestParam Long accountNumber, 
			@RequestParam Integer ammount) {
		
		Gson gson = new Gson();
		JSONResponse jResponse = new JSONResponse();
		
		if(ammount < 20 || ammount > 250) {
			jResponse.addError(new Error("Withdrawals should be between 20 and 250"));
		}
		
		if(ammount % 5 != 0) {
			jResponse.addError(new Error("It must be a multiple of 5"));
		}
		
		Account account = accountService.checkBalance(accountNumber);
		if(account == null) {
			jResponse.addError(new Error("The account requested does not exist"));
		}
		else if(account.getBalance().compareTo(new BigDecimal(ammount)) == -1) {
			jResponse.addError(new Error("You do not have enough funds to carry out the operation"));
		}
		
		if(jResponse.getErrors().size() > 0) {
			jResponse.setStatus("error");
			return new ResponseEntity<Object>(gson.toJson(jResponse), HttpStatus.OK);
		}
		
		
		Object body = accountService.withdraw(accountNumber, ammount);
		if(body == null) {
			jResponse.setStatus("error");
			jResponse.addError(new Error("Not possible to disburse " +ammount +" at this moment, try another ammount"));
			return new ResponseEntity<Object>(gson.toJson(jResponse), HttpStatus.OK);
		} else {
			jResponse.setStatus("ok");
			jResponse.setResp(body);
		}
		
		
		return new ResponseEntity<Object>(gson.toJson(jResponse), HttpStatus.OK);
	}
}
