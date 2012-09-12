package com.bushidosoft.myfinances.bankofamerica;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bushidosoft.myfinances.entities.Account;
import com.bushidosoft.myfinances.entities.BankLogin;
import com.bushidosoft.myfinances.entities.ChallengeQuestion;
import com.bushidosoft.myfinances.entities.Transaction;
import com.bushidosoft.myfinances.entities.Transaction.TransactionType;
import com.bushidosoft.myfinances.services.ChallengeQuestionService;
import com.bushidosoft.myfinances.services.TransactionService;

@Service
public class BOAScraper {

	private static final String BOA_Login_Page = "https://www.bankofamerica.com";
	private static DateFormat dateFormat_mmddyyyy = new SimpleDateFormat("MM/dd/yyyy");

	private static Logger logger = Logger.getLogger(BOAScraper.class);

	@Autowired(required=true)
	private TransactionService transactionService;
	
	@Autowired(required=true)
	private ChallengeQuestionService challengeQuestionService;

	/*
	 * Main execution function.
	 */
	public void scrape(WebDriver webDriver, BankLogin bankLogin, List<Account> accounts) {
		logger.debug("Started Scraping");
		this.login(webDriver, bankLogin);
		this.answerChallengeQuestion(webDriver, bankLogin);
		this.submitPassword(webDriver, bankLogin);
		
		for (Account account : accounts) {
			logger.debug("Retrieving transactions for account ["+account.getName()+"]");
			scrapeTransactions(webDriver, bankLogin, account);
		}
		
		logger.debug("Finished Scraping");
	}
	
	
	private void scrapeTransactions(WebDriver webDriver, BankLogin bankLogin, Account account) {
		WebElement webElement;
		
		webElement = webDriver.findElement(By.id("Accounts_topnav"));
		webElement.click();
		
		webElement = webDriver.findElement(By.id(account.getName()));
		webElement.click();
		
		// BOA transactions go back 18 months; however, their search only goes back 12 months. This date is the farthest back that we can search.
		Calendar furthestBackWeCanSearch = Calendar.getInstance();
		furthestBackWeCanSearch.add(Calendar.MONTH, -12);
		
/*		if( account.lastKnownTransaction != null && account.lastKnownTransaction.postedDate.after(furthestBackWeCanSearch.getTime())) {
			webElement = webDriver.findElement(By.name("More_options"));
			webElement.click();
			
			webElement = webDriver.findElement(By.id("search-select-timeframe-cs"));
			webElement.click();
			
			webElement = webDriver.findElement(By.id("search-select-timeframe-cs-menu-item2"));
			webElement.click();

			String fromDateStr = (account.lastKnownTransaction == null) ? "01/01/2000" : dateFormat_mmddyyyy.format(account.lastKnownTransaction.getPostedDate());
			webElement = webDriver.findElement(By.id("search-calendar-from"));
			webElement.sendKeys(fromDateStr);
	
			Calendar tomorrow = Calendar.getInstance();
			tomorrow.add(Calendar.DAY_OF_YEAR, 1);
			String toDateStr = dateFormat_mmddyyyy.format( tomorrow.getTime() );
			webElement = webDriver.findElement(By.id("search-calendar-to"));
			webElement.sendKeys(toDateStr);
	
			webElement = webDriver.findElement(By.name("search_button"));
			webElement.click();
		}*/
		
		webElement = webDriver.findElement(By.name("oldest_trans_nav_top"));
		webElement.click();
		
		boolean thereAreMoreTransactions = true;
		while(thereAreMoreTransactions) {
			scrapeTransactionsFromPage(webDriver, account);
			try {
				logger.debug("Clicking link for next page of transactions");
				webElement = webDriver.findElement(By.name("next_trans_nav_top"));
				webElement.click();
			} catch (NoSuchElementException e) {
					logger.debug("No more transaction pages");
					thereAreMoreTransactions = false;
				}
		}
		
		
	}

	private void scrapeTransactionsFromPage(WebDriver webDriver, Account account) {
		logger.debug("Scraping transactions from page");
		WebElement webElement = webDriver.findElement(By.className("transaction-records"));
		List<WebElement> records = webElement.findElements(By.className("record"));
		
		Transaction[] orderedTransactions = new Transaction[50];
		
		for (WebElement record : records) {
			if (record.getAttribute("class").contains("cleared")) {
				Transaction transaction = new Transaction();
				transaction.setAccount(account); 
				transaction.setAmount(Double.parseDouble( record.findElement(By.className("amount")).getText().replace(",", "") ));
				transaction.setDescription(record.findElement(By.className("transTitleForEditDesc")).getText());
				transaction.setExternalId(record.getAttribute("id").substring(8));
				transaction.setResultingBalance(Double.parseDouble( record.findElement(By.className("balance")).getText().replace(",", "")) );
				String date = record.findElement(By.cssSelector(".date-action > span")).getText();
				int rel = Integer.parseInt(record.findElement(By.cssSelector(".date-action")).getAttribute("rel"));
				
				try {
					transaction.setPostedDate(dateFormat_mmddyyyy.parse(date));
				} catch (ParseException e) {
					throw new RuntimeException("Failed to parse date ["+date+"]");
				}
				
				String type = record.findElement(By.className("type")).getText();
				if (type.endsWith("bank charge")) transaction.setType(TransactionType.BankCharge); 	
				else if (type.endsWith("billpay")) transaction.setType(TransactionType.BillPay); 	
				else if (type.endsWith("cards")) transaction.setType(TransactionType.Cards);
				else if (type.endsWith("check")) transaction.setType(TransactionType.Check); 	
				else if (type.endsWith("credit")) transaction.setType(TransactionType.Credit); 	
				else if (type.endsWith("deposit")) transaction.setType(TransactionType.Deposit); 	
				else if (type.endsWith("other payment")) transaction.setType(TransactionType.OtherPayment); 	
				else if (type.endsWith("transfer")) transaction.setType(TransactionType.Transfer); 	
				else if (type.endsWith("withdrawal")) transaction.setType(TransactionType.Withdrawal); 	
				else throw new RuntimeException("Cannot identify Transaction Type from text ["+type+"]");
				
				if(!transactionService.alreadyExists(transaction)) {
					orderedTransactions[rel-1] = transaction;
				}
			}
		}
		for(Transaction transaction: orderedTransactions) {
			if (transaction != null) {
				transactionService.createTransaction(transaction);
				logger.info(transaction.toString());
			}
		}

	}
	
	private void submitPassword(WebDriver webDriver, BankLogin bankLogin) {
		WebElement webElement;
		
		webElement = webDriver.findElement(By.id("tlpvt-passcode-input"));
		webElement.sendKeys(bankLogin.getPassword());
		
		webElement = webDriver.findElement(By.name("confirm-sitekey-submit"));
		webElement.click();
	}


	private void answerChallengeQuestion(WebDriver webDriver, BankLogin bankLogin) {
		WebElement webElement;
		String challengeQuestionLabel = findLabelFor(webDriver, "tlpvt-challenge-answer");
		String answer = null;
		for (ChallengeQuestion challengeQuestion: challengeQuestionService.findChallengeQuestions(bankLogin)) {
			if (challengeQuestionLabel.equals(challengeQuestion.getQuestion())) {
				answer = challengeQuestion.getAnswer();
				break;
			}
		}
		if (answer == null) throw new RuntimeException("Missing answer to Challenge Question ["+challengeQuestionLabel+"]");
		webElement = webDriver.findElement(By.id("tlpvt-challenge-answer"));
		webElement.sendKeys(answer);
		
		webElement = webDriver.findElement(By.name("enter-online-id-submit"));
		webElement.click();
	}


	
	private void login(WebDriver webDriver, BankLogin bankLogin) {
		WebElement element;
		webDriver.get(BOA_Login_Page);
		
		element = webDriver.findElement(By.id("id"));
		element.sendKeys(bankLogin.getUserName());
		
		element = webDriver.findElement(By.id("stateselect"));
		element.sendKeys(bankLogin.getState().toStringWithSpaces());

		element = webDriver.findElement(By.id("top-button"));
		element.click();
	}


	/**
	 * Find the text of the first Label with the For attribute equal to the provided 'labelFor' parameter. 
	 * 
	 * @param webDriver
	 * @param labelFor
	 * @return
	 */
	private static String findLabelFor(WebDriver webDriver, String labelFor) {
		return webDriver.findElement(By.xpath("//label[@for='" + labelFor + "']")).getText();
	}
	
}
