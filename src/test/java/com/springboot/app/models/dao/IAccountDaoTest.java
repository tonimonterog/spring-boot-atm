package com.springboot.app.models.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.springboot.app.models.entity.Account;

@RunWith(SpringRunner.class)
@DataJpaTest
public class IAccountDaoTest {
	@Autowired
    private TestEntityManager entityManager;
 
    @Autowired
    private IAccountDao accountDao;
    
    @Test
    public void whenFindByNumber_thenReturnAccount() {
        // given
        Account account = new Account();
        account.setNumber(1004l);
        account.setBalance(new BigDecimal(21300.99));
        entityManager.persist(account);
        entityManager.flush();
     
        // when
        Account found = accountDao.findByNumber(account.getNumber());
     
        // then
        assertThat(found.getId()).isEqualTo(account.getId());
        assertThat(found.getBalance()).isEqualTo(account.getBalance());
    }
}
