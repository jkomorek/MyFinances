package com.bushidosoft.myfinances.dal;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bushidosoft.myfinances.entities.Account;
import com.bushidosoft.myfinances.entities.Transaction;

@Repository
public class TransactionRepository extends AbstractRepository<Transaction> {
	
	@SuppressWarnings("unchecked")
	public List<Transaction> listTransactions(Account account) {
		return (List<Transaction>)getSessionFactory().getCurrentSession().createCriteria(Transaction.class)
					.add(Restrictions.eq("account", account.id))
					.list();
	}

}
