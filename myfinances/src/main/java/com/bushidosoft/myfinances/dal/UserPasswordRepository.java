package com.bushidosoft.myfinances.dal; 

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bushidosoft.myfinances.entities.User;
import com.bushidosoft.myfinances.entities.UserPassword;

@Repository
public class UserPasswordRepository extends AbstractRepository<UserPassword> {
	
	
	/**
	 * Creates a UserPassword using the provided example.
	 * 
	 * Note that if the PasswordDate is not set, we default it to the current Date and Time.
	 * 
	 * @param userPassword
	 */
	@Override
	@Transactional
	public void create(UserPassword userPassword) {
		if (userPassword.getPasswordDate() == null) userPassword.setPasswordDate( new Date() );
		super.create(userPassword);
	}

	/**
	 * Retrieves the current password for the provided User or null if none exists.
	 * 
	 * @param user
	 * @return
	 */
	@Transactional
	public String findMostRecentPasswordForUser(User user) {
		Session session = this.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(UserPassword.class);
		
		List<UserPassword> userPasswords = 
				criteria.add(Restrictions.eq("user.id", user.getId()))
				.addOrder(Order.desc("passwordDate"))
				.list();
		
		if (userPasswords.size() == 0) return null;
		return userPasswords.get(0).getPassword();
	}

}
