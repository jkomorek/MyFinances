package com.bushidosoft.myfinances.dal;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bushidosoft.myfinances.entities.Account;
import com.bushidosoft.myfinances.entities.Bank;
import com.bushidosoft.myfinances.entities.User;

@Repository
public class AccountRepository extends AbstractRepository<Account> {
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Account> findAccountsBy(Bank bank, User user) {
		return this.getSessionFactory().getCurrentSession().createCriteria(Account.class)
			.add( Restrictions.eq("bank.id", bank.getId()) )
			.add( Restrictions.eq("user.id", user.getId()))
			.list();
	}
	
}