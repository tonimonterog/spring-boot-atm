package com.springboot.app.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.app.models.entity.Account;
import com.springboot.app.service.IAccountService;

@RunWith(SpringRunner.class)
@WebMvcTest(AtmController.class)
public class AtmControllerTest {

	@Autowired
    private MockMvc mvc;
 
    @MockBean
    @Qualifier("atm")
    private IAccountService accountService;
    
    @Test
    public void givenAccountNumber_whenGetBalance_thenReturnBalance() throws Exception {
    	Account account = new Account();
		account.setId(1l);
		account.setBalance(new BigDecimal(2738.59));
		account.setNumber(01001l);
		
		when(accountService.checkBalance(Mockito.anyLong())).thenReturn(account);
		
		MvcResult result = mvc.perform(get("/balance?accountNumber=01001").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(result.getResponse().getContentAsString());
		
		assertThat(node.get("resp").asText()).isEqualTo("Price in GBP : £2,738.59");

    }
    
    @Test
    public void givenWrongAccountNumber_whenGetBalance_thenReturnError() throws Exception {
    	Account account = new Account();
		account.setId(1l);
		account.setBalance(new BigDecimal(2738.59));
		account.setNumber(01001l);
		
		when(accountService.checkBalance(01001l)).thenReturn(account);
		
		MvcResult result = mvc.perform(get("/balance?accountNumber=01004").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		
		assertThat(result.getResponse().getContentAsString(), containsString("The account requested does not exist"));

    }
    
    @Test
    public void givenAccountNumber_whenWithdraw_thenReturnDisburse() throws Exception {
    	Account account = new Account();
		account.setId(1l);
		account.setBalance(new BigDecimal(2738.59));
		account.setNumber(01001l);
		
		Map<Integer, Integer> res =  new HashMap<Integer, Integer>();
		res.put(50, 2);
		res.put(20, 3);
		res.put(10, 1);
		res.put(5, 1);
		
		when(accountService.checkBalance(Mockito.anyLong())).thenReturn(account);
		when(accountService.withdraw(Mockito.anyLong(), Mockito.anyInt())).thenReturn(res);
		
		MvcResult result = mvc.perform(get("/withdraw?accountNumber=01001&ammount=175").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(result.getResponse().getContentAsString());
		
		assertThat(node.get("resp").findValuesAsText("50").get(0)).isEqualTo("2");
		assertThat(node.get("resp").findValuesAsText("20").get(0)).isEqualTo("3");
		assertThat(node.get("resp").findValuesAsText("10").get(0)).isEqualTo("1");
		assertThat(node.get("resp").findValuesAsText("5").get(0)).isEqualTo("1");
    }
    
    @Test
    public void givenAccountNumber_whenWithdrawOutOfRange_thenReturnError() throws Exception {
    	Account account = new Account();
		account.setId(1l);
		account.setBalance(new BigDecimal(2738.59));
		account.setNumber(01001l);
		
		when(accountService.checkBalance(Mockito.anyLong())).thenReturn(account);
		when(accountService.withdraw(Mockito.anyLong(), Mockito.anyInt())).thenReturn(null);
		
		MvcResult result = mvc.perform(get("/withdraw?accountNumber=01001&ammount=15").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		
		assertThat(result.getResponse().getContentAsString(), containsString("Withdrawals should be between 20 and 250"));
    }
    
    @Test
    public void givenAccountNumber_whenWithdrawInvalidNumber_thenReturnError() throws Exception {
    	Account account = new Account();
		account.setId(1l);
		account.setBalance(new BigDecimal(2738.59));
		account.setNumber(01001l);
		
		when(accountService.checkBalance(Mockito.anyLong())).thenReturn(account);
		when(accountService.withdraw(Mockito.anyLong(), Mockito.anyInt())).thenReturn(null);
		
		MvcResult result = mvc.perform(get("/withdraw?accountNumber=01001&ammount=17").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		
		assertThat(result.getResponse().getContentAsString(), containsString("It must be a multiple of 5"));
    }
    
    @Test
    public void givenAccountNumber_whenWithdrawInvalidAccountNumber_thenReturnError() throws Exception {
    	Account account = new Account();
		account.setId(1l);
		account.setBalance(new BigDecimal(2738.59));
		account.setNumber(01001l);
		
		when(accountService.checkBalance(Mockito.anyLong())).thenReturn(null);
		when(accountService.withdraw(Mockito.anyLong(), Mockito.anyInt())).thenReturn(null);
		
		MvcResult result = mvc.perform(get("/withdraw?accountNumber=01001&ammount=175").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		
		assertThat(result.getResponse().getContentAsString(), containsString("The account requested does not exist"));
    }
    
    @Test
    public void givenAccountNumber_whenWithdrawInvalidWithdraw_thenReturnError() throws Exception {
    	Account account = new Account();
		account.setId(1l);
		account.setBalance(new BigDecimal(2738.59));
		account.setNumber(01001l);
		
		when(accountService.checkBalance(Mockito.anyLong())).thenReturn(account);
		when(accountService.withdraw(Mockito.anyLong(), Mockito.anyInt())).thenReturn(null);
		
		MvcResult result = mvc.perform(get("/withdraw?accountNumber=01001&ammount=175").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		
		assertThat(result.getResponse().getContentAsString(), containsString("Not possible to disburse 175 at this moment, try another ammount"));
    }
    
    @Test
    public void givenAccountNumber_whenWithdrawInvalidFounds_thenReturnError() throws Exception {
    	Account account = new Account();
		account.setId(1l);
		account.setBalance(new BigDecimal(50));
		account.setNumber(01001l);
		
		when(accountService.checkBalance(Mockito.anyLong())).thenReturn(account);
		when(accountService.withdraw(Mockito.anyLong(), Mockito.anyInt())).thenReturn(null);
		
		MvcResult result = mvc.perform(get("/withdraw?accountNumber=01001&ammount=175").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		
		assertThat(result.getResponse().getContentAsString(), containsString("You do not have enough funds to carry out the operation"));
    }

}
