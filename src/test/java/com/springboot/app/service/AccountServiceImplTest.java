package com.springboot.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

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
public class AccountServiceImplTest {
	
	@TestConfiguration
    static class AccountServiceImplTestContextConfiguration {
  
        @Bean
        public IAccountService accountService() {
            return new AccountServiceImpl();
        }
    }
	
	@MockBean
	private IAccountDao accountDao;
	
	@Autowired
	private IAccountService accountService;

	@Before
	public void setUp() {
		Account account = new Account();
		account.setId(1l);
		account.setBalance(new BigDecimal(2738.59));
		account.setNumber(01001l);
		
	    when(accountDao.findByNumber(account.getNumber())).thenReturn(account);
	}
	
	@Test
	public void whenValidNumber_thenBalanceShouldBeFound() {
		Long number = 01001l;
		Account account = accountService.checkBalance(number);
		
		assertThat(account.getBalance()).isEqualTo(new BigDecimal(2738.59));
	}
	
	@Test
	public void whenValidNumber_thenWithdrawShouldBeDone() {
		Long number = 01001l;
		Account account = (Account) accountService.withdraw(number, 240);
		
		assertThat(account.getBalance()).isEqualTo(new BigDecimal(2738.59).subtract(new BigDecimal(240)));
	}
}
