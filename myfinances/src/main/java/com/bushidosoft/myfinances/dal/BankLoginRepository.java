package com.bushidosoft.myfinances.dal;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bushidosoft.myfinances.entities.Bank;
import com.bushidosoft.myfinances.entities.BankLogin;
import com.bushidosoft.myfinances.entities.User;

@Repository
public class BankLoginRepository extends AbstractRepository<BankLogin> {
	
	@SuppressWarnings("unchecked")
	@Transactional
	public BankLogin findBankLogin(Bank bank, User user) {
		if (bank == null) return null; 
		if (user == null) return null; 
			
		List<BankLogin> bankLogins = this.getSessionFactory().getCurrentSession().createCriteria(BankLogin.class)
			.add( Restrictions.eq("bank.id", bank.getId()) )
			.add( Restrictions.eq("user.id", user.getId()) )
			.list();
	
		switch(bankLogins.size()) {
			case 0: return null;
			case 1: return bankLogins.get(0);
			default: throw new RuntimeException("Expected 1 BankLogin but found ["+bankLogins.size()+"] for Bank ["+bank.getId()+"] and User ["+user.getId()+"]");
		}
	}

}
