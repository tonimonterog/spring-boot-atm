package com.springboot.app.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.springboot.app.models.dao.IAccountDao;
import com.springboot.app.models.entity.Account;

@RunWith(SpringRunner.class)
public class ATMServiceImplTest {
	
	@TestConfiguration
    static class ATMServiceImplTestContextConfiguration {
  
        @Bean
        public IAccountService accountService() {
            return new ATMServiceImpl();
        }
    }
	
	@MockBean
	private IAccountDao accountDao;
	
	@Autowired
	private ATMServiceImpl accountService;
	
	@Before
	public void setUp() {
		Account account = new Account();
		account.setId(1l);
		account.setBalance(new BigDecimal(2738.59));
		account.setNumber(01001l);
		
		accountService.replenish(50, 25, 10, 5);
		
	    when(accountDao.findByNumber(account.getNumber())).thenReturn(account);
	}
	
	@Test
	public void whenValidNumberAndValidWithdraw_thenWithdrawShouldBeDone() {
		Long number = 01001l;
		
		Map<Integer, Integer> res = (Map<Integer, Integer>) accountService.withdraw(number, 175);
		
		assertEquals(new Integer(3), res.get(50));
		assertEquals(new Integer(1), res.get(20));
		assertEquals(new Integer(1), res.get(5));
		
	}
	
	@Test
	public void whenValidNumberAndValidWithdrawAtLeastOne5_thenWithdrawShouldBeDoneAndAtLeastOne5() {
		Long number = 01001l;
		
		Map<Integer, Integer> res = (Map<Integer, Integer>) accountService.withdraw(number, 110);
		
		assertEquals(new Integer(2), res.get(50));
		assertEquals(new Integer(2), res.get(5));
		
	}
	
	@Test
	public void whenValidNumberAndInvalidWithdraw_thenWithdrawShouldNotBeDone() {
		Long number = 01001l;
		
		accountService.replenish(1, 1, 1, 1);
		
		Map<Integer, Integer> res = (Map<Integer, Integer>) accountService.withdraw(number, 205);
		
		assertNull(res);
		
	}
	
}
