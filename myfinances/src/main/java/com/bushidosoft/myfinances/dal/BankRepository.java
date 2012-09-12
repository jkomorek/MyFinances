package com.bushidosoft.myfinances.dal;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bushidosoft.myfinances.entities.Bank;

@Repository
public class BankRepository extends AbstractRepository<Bank> {
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Bank findBankLogin(String name) {
		if (name == null) return null; 
			
		List<Bank> banks = this.getSessionFactory().getCurrentSession().createCriteria(Bank.class)
			.add( Restrictions.eq("name", name) )
			.list();
	
		switch(banks.size()) {
			case 0: return null;
			case 1: return banks.get(0);
			default: throw new RuntimeException("Expected 1 Bank but found ["+banks.size()+"] for name ["+name+"]");
		}
	}

}
