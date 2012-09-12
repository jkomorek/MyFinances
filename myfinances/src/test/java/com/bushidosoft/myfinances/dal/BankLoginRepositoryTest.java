package com.bushidosoft.myfinances.dal;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bushidosoft.myfinances.bankofamerica.State;
import com.bushidosoft.myfinances.entities.Bank;
import com.bushidosoft.myfinances.entities.BankLogin;
import com.bushidosoft.myfinances.entities.ChallengeQuestion;
import com.bushidosoft.myfinances.entities.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class BankLoginRepositoryTest {
	
	@Autowired(required=true)
	BankLoginRepository bankLoginRepository;
	
	@Autowired(required=true)
	BankRepository bankRepository;
	
	@Autowired(required=true)
	UserRepository userRepository;
	

	@Test
	@Transactional
	public void smokeTest() {
		String UID = "smokeTest "+System.currentTimeMillis();
		Bank bank = new Bank();
		bank.setName(UID);
		bankRepository.create(bank);
		
		User user = new User();
		user.setUserName(UID);
		userRepository.create(user);
		
		Set<ChallengeQuestion> challengeQuestions = new HashSet<ChallengeQuestion>();
		ChallengeQuestion challengeQuestion;
		
		challengeQuestion = new ChallengeQuestion();
		challengeQuestion.setQuestion("1*1=?");
		challengeQuestion.setAnswer("1");
		challengeQuestions.add(challengeQuestion);
		
		challengeQuestion = new ChallengeQuestion();
		challengeQuestion.setQuestion("1+1=?");
		challengeQuestion.setAnswer("2");
		challengeQuestions.add(challengeQuestion);
		
		BankLogin bankLogin = new BankLogin();
		bankLogin.setBank(bank);
//		bankLogin.setChallengeQuestions(challengeQuestions);
		bankLogin.setPassword("password");
		bankLogin.setState(State.SouthCarolina);
		bankLogin.setUser(user);
		bankLogin.setUserName("username");
		bankLoginRepository.create(bankLogin);
		
//		Assert.assertNotSame(bankLogin.getId(), 0);
//		Assert.assertEquals(2, bankLogin.getChallengeQuestions().size());
//		Iterator<ChallengeQuestion> iterator = bankLogin.getChallengeQuestions().iterator();
//		
//		challengeQuestion = iterator.next();
//		if ( challengeQuestion.getQuestion().equals("1*1=?") ) Assert.assertEquals("1", challengeQuestion.getAnswer());	
//		else if ( challengeQuestion.getQuestion().equals("1+1=?") ) Assert.assertEquals("2", challengeQuestion.getAnswer());
//		else throw new RuntimeException("Unknown question from test ["+challengeQuestion.getQuestion()+"]");
//		
//		challengeQuestion = iterator.next();
//		if ( challengeQuestion.getQuestion().equals("1*1=?") ) Assert.assertEquals("1", challengeQuestion.getAnswer());	
//		else if ( challengeQuestion.getQuestion().equals("1+1=?") ) Assert.assertEquals("2", challengeQuestion.getAnswer());
//		else throw new RuntimeException("Unknown question from test ["+challengeQuestion.getQuestion()+"]");
//		
		bankLogin = bankLoginRepository.findBankLogin(bank, user);
//		Assert.assertEquals(2, bankLogin.getChallengeQuestions().size());
//		iterator = bankLogin.getChallengeQuestions().iterator();
//		
//		challengeQuestion = iterator.next();
//		if ( challengeQuestion.getQuestion().equals("1*1=?") ) Assert.assertEquals("1", challengeQuestion.getAnswer());	
//		else if ( challengeQuestion.getQuestion().equals("1+1=?") ) Assert.assertEquals("2", challengeQuestion.getAnswer());
//		else throw new RuntimeException("Unknown question from test ["+challengeQuestion.getQuestion()+"]");
//		
//		challengeQuestion = iterator.next();
//		if ( challengeQuestion.getQuestion().equals("1*1=?") ) Assert.assertEquals("1", challengeQuestion.getAnswer());	
//		else if ( challengeQuestion.getQuestion().equals("1+1=?") ) Assert.assertEquals("2", challengeQuestion.getAnswer());
//		else throw new RuntimeException("Unknown question from test ["+challengeQuestion.getQuestion()+"]");		
	}
		
/*	@Test
	public void testRemovingQuestionsFromBankLogin() {
		String UID = "smokeTest "+System.currentTimeMillis();
		Bank bank = new Bank();
		bank.setName(UID);
		bankRepository.create(bank);
		
		User user = new User();
		user.setUserName(UID);
		userRepository.create(user);
		
		Set<ChallengeQuestion> challengeQuestions = new HashSet<ChallengeQuestion>();
		ChallengeQuestion challengeQuestion;
		
		challengeQuestion = new ChallengeQuestion();
		challengeQuestion.setQuestion("1*1=?");
		challengeQuestion.setAnswer("1");
		challengeQuestions.add(challengeQuestion);
		
		challengeQuestion = new ChallengeQuestion();
		challengeQuestion.setQuestion("1+1=?");
		challengeQuestion.setAnswer("2");
		challengeQuestions.add(challengeQuestion);
		
		BankLogin bankLogin = new BankLogin();
		bankLogin.setBank(bank);
		bankLogin.setChallengeQuestions(challengeQuestions);
		bankLogin.setPassword("password");
		bankLogin.setState(State.SouthCarolina);
		bankLogin.setUser(user);
		bankLogin.setUserName("username");
		bankLoginRepository.create(bankLogin);
		
		Assert.assertNotSame(bankLogin.getId(), 0);
		Assert.assertEquals(2, bankLogin.getChallengeQuestions().size());
		
		bankLogin.getChallengeQuestions().removeAll( bankLogin.getChallengeQuestions() );
		Assert.assertEquals(0, bankLogin.getChallengeQuestions().size());
		bankLoginRepository.update(bankLogin);

		Assert.assertEquals(0, bankLogin.getChallengeQuestions().size());
	}*/

}
