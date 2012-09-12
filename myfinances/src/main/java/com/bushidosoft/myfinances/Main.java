package com.bushidosoft.myfinances;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bushidosoft.myfinances.bankofamerica.BOAScraper;
import com.bushidosoft.myfinances.bankofamerica.State;
import com.bushidosoft.myfinances.dal.AccountRepository;
import com.bushidosoft.myfinances.dal.BankLoginRepository;
import com.bushidosoft.myfinances.dal.BankRepository;
import com.bushidosoft.myfinances.dal.UserRepository;
import com.bushidosoft.myfinances.entities.Account;
import com.bushidosoft.myfinances.entities.Bank;
import com.bushidosoft.myfinances.entities.BankLogin;
import com.bushidosoft.myfinances.entities.ChallengeQuestion;
import com.bushidosoft.myfinances.entities.User;

public class Main {

	private static Logger logger = Logger.getLogger(Main.class);
	private static ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml" });

	private static BankRepository bankRepository = ctx.getBean(BankRepository.class);
	private static UserRepository userRepository = ctx.getBean(UserRepository.class);

	public static void main(String[] args) throws Exception {
		logger.debug("Starting");

		String bankName = "Bank Of America";
		String userId = "Jonathan.Komorek@gmail.com";
		
		Bank bank = bankRepository.findByAttribute("name", bankName);
		User user = userRepository.findByAttribute("userName", userId);

		BankLogin bankLogin = loadBankLogin(bank, user);
		List<Account> accounts = loadAccounts(bank, user);
		
		WebDriver webDriver = new FirefoxDriver();		
		
		webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

		BOAScraper scraper = ctx.getBean(BOAScraper.class);
		scraper.scrape(webDriver, bankLogin, accounts);
		logger.debug("Complete");
		
		webDriver.close();
		System.exit(0);
	}

	private static BankLogin loadBankLogin(Bank bank, User user) {
		
		BankLoginRepository bankLoginRepository = ctx.getBean(BankLoginRepository.class);
		return bankLoginRepository.findBankLogin(bank, user);
	}

	private static List<Account> loadAccounts(Bank bank, User user) {
		AccountRepository accountRepository = ctx.getBean(AccountRepository.class);
		return accountRepository.findAccountsBy(bank, user);
	}

	@SuppressWarnings("unused")
	private static List<Account> mockAccounts() {
		List<Account> accounts = new ArrayList<Account>();
		
		Account account;
		
		account = new Account();
		account.setName("Joint Account");
		accounts.add(account);
		
		account = new Account();
		account.setName("Personal Account");
		accounts.add(account);

		return accounts;
	}

	/**
	 * Initializes the BankOfAmeria profile for this user. 
	 */
	@SuppressWarnings("unused")
	private static BankLogin mockBankLogin() {
		
		BankLogin bankLogin = new BankLogin();
		bankLogin.setUserName("jonathankomorek");
		bankLogin.setPassword("G41nn3ss4@ll");
		bankLogin.setState(State.SouthCarolina);

		Set<ChallengeQuestion> challengeQuestions = new HashSet<ChallengeQuestion>();
		ChallengeQuestion challengeQuestion;
		
		challengeQuestion = new ChallengeQuestion();
		challengeQuestion.setQuestion("What is your father's middle name?");
		challengeQuestion.setAnswer("Lee");
		challengeQuestions.add(challengeQuestion);
		
		challengeQuestion = new ChallengeQuestion();
		challengeQuestion.setQuestion("With what company did you hold your first job?");
		challengeQuestion.setAnswer("Bach's Deli");
		challengeQuestions.add(challengeQuestion);
		
		challengeQuestion = new ChallengeQuestion();
		challengeQuestion.setQuestion("In what city were you living at age 16?");
		challengeQuestion.setAnswer("East Aurora");
		challengeQuestions.add(challengeQuestion);
		
		//bankLogin.setChallengeQuestions(challengeQuestions);
		
		return bankLogin;
	}

}
