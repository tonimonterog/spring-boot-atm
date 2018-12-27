package com.springboot.app.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Qualifier("atm")
public class ATMServiceImpl extends AccountServiceImpl {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private Map<Integer, Integer> currencyAmmount = new HashMap<Integer, Integer>();
	
	private static final int[] currencies = { 50, 20, 10, 5 };
	
	@PostConstruct
    public void init() {
		replenish(50, 25, 10, 5);
		//replenish(4, 1, 10, 2);
	}

	@Transactional
	public Map<Integer, Integer> withdraw(Long number, Integer ammount) {
		log.info("currencyAmmount before withdraw: "+currencyAmmount.toString());
		
		int[] currencieCounter = findBestDisburseAtLeastOne5(ammount);
		
		if(currencieCounter == null) {
			log.error("Not possible to disburse "+ammount +" with these ammount of currencies : " +currencyAmmount.toString());
			return null;
		}
		
		
		super.withdraw(number, ammount);
		
		Map<Integer, Integer> res =  new HashMap<Integer, Integer>();
		
		for (int i = 0; i < currencieCounter.length; i++) { 
			if (currencieCounter[i] != 0) {
				currencyAmmount.put(currencies[i], currencyAmmount.get(currencies[i]) - currencieCounter[i]);
				res.put(currencies[i], currencieCounter[i]);
            } 
		}
		
		log.info("currencyAmmount after withdraw: "+currencyAmmount.toString());
		
		return res;
		
	}
	
	@Transactional
	public void replenish(Integer ammountOf5, Integer ammountOf10, Integer ammountOf20, Integer ammountOf50) {
		currencyAmmount.put(5, ammountOf5);
		currencyAmmount.put(10, ammountOf10);
		currencyAmmount.put(20, ammountOf20);
		currencyAmmount.put(50, ammountOf50);
	}
	
	
	/**
	 * 
	 * To disburse at least one 5 currency it checks if it ends with 0 and has at least 2 remaining of 5
	 * 	Then it tries to solve it with this conditions
	 * If not tries to solve it without the 5 condition
	 * 
	 */
	private int[] findBestDisburseAtLeastOne5(int ammount) {
		if(ammount % 10 == 0 && getCurrenciesRemaining(5) >= 2) {
			currencyAmmount.put(5, currencyAmmount.get(5) - 2);
			int[] res = findBestDisburse(ammount-10);
			if(res != null) {
				res[currencies.length-1] = res[currencies.length-1] + 2;
				currencyAmmount.put(5, currencyAmmount.get(5) + 2);
				
				return res;
			} else {
				currencyAmmount.put(5, currencyAmmount.get(5) + 2);
			}
		}
		
		int[] res = findBestDisburse(ammount);
		
		return res;
	}
	
	/**
	 * 
	 * The algorithm disburse the smallest number of notes checking if there are enough currencies 
	 * 
	 */
	private int[] findBestDisburse(int ammount) {
        int[] currencieCounter = new int[currencies.length]; 
       
        // count notes using Greedy approach 
        for (int i = 0; i < currencies.length; i++) { 
            if (ammount >= currencies[i]) {
            	int quantity = (ammount / currencies[i] <= getCurrenciesRemaining(currencies[i]) ? ammount / currencies[i] : getCurrenciesRemaining(currencies[i])); //To check if we have enough remaining currencies, if not take the maximum possible
                currencieCounter[i] = quantity;
                ammount = ammount - currencieCounter[i] * currencies[i];
            }
        }
        if(ammount != 0) {
        	return null;
        }
        
        return currencieCounter;
	}
	
	private int getCurrenciesRemaining(Integer currency) {
		return currencyAmmount.get(currency);
	}
	
}
