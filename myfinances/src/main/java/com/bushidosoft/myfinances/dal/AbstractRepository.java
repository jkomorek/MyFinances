package com.bushidosoft.myfinances.dal;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


public abstract class AbstractRepository<T> {

	private static Logger logger = Logger.getLogger(AbstractRepository.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * Adds the provided entity to this repository.
	 * 
	 * @param account
	 */
	@Transactional
	public void create(T t) {
		Method getIdMethod = null;
		long id;
		try {
			getIdMethod = t.getClass().getMethod("getId", new Class<?>[]{});
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Cannot add entity ["+t+"] to repository ["+this.getClass().getName()+"] because entity does not have a getId() method", e);
		}
		
		try {
			id = (Long)getIdMethod.invoke(t, new Object[]{} );
		} catch(Exception e) {
			throw new RuntimeException("Failed to invoke getId() on entity ["+t+"]", e); 
		}

		if(id != 0) throw new RuntimeException("Cannot create ["+t+"] with the non-zero ID ["+id+"]. IDs are managed by the repository and should not be set.");
		sessionFactory.getCurrentSession().save(t);
	}

	@Transactional
	public void update(T t) {
		sessionFactory.getCurrentSession().update(t);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public T findByAttribute(String name, Object value) {
		Class<?> clazz = (Class<?>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		
		logger.debug("Find ["+clazz+"] by attribute ["+name+"] ["+value+"]");
		if (name == null || name.length() == 0) throw new RuntimeException("Attribute name is required, you passed in ["+name+"]");
		
		List<T> results = this.getSessionFactory().getCurrentSession().createCriteria(clazz)
			.add( Restrictions.eq(name, value) )
			.list();
	
		switch(results.size()) {
			case 0: return null;
			case 1: return results.get(0);
			default: throw new RuntimeException("Expected 1 Result but found ["+results.size()+"] for ["+name+"] ["+value+"]");
		}
	}

	
	@SuppressWarnings("unchecked")
	@Transactional
	public T findByAttributes(Map<String, Object> attributes) {
		Class<?> clazz = (Class<?>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		
		logger.debug("Find ["+clazz+"] by attributes");
		//if (name == null || name.length() == 0) throw new RuntimeException("Attribute name is required, you passed in ["+name+"]");
		
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(clazz);
		Iterator<String> keyIterator = attributes.keySet().iterator();
		while (keyIterator.hasNext()) {
			String attributeName = keyIterator.next();
			criteria.add( Restrictions.eq(attributeName, attributes.get(attributeName)) );
		}
				
		List<T> results = criteria.list();
	
		switch(results.size()) {
			case 0: return null;
			case 1: return results.get(0);
			default: throw new RuntimeException("Expected 1 Result but found ["+results.size()+"]");
		}
	}

	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<T> listByAttribute(String name, Object value) {
		Class<?> clazz = (Class<?>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		
		logger.debug("Find ["+clazz+"] by attribute ["+name+"] ["+value+"]");
		if (name == null || name.length() == 0) throw new RuntimeException("Attribute name is required, you passed in ["+name+"]");
		
		return this.getSessionFactory().getCurrentSession().createCriteria(clazz)
			.add( Restrictions.eq(name, value) )
			.list();
	}

	
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
