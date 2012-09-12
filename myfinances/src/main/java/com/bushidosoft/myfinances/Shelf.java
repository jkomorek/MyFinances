package com.bushidosoft.myfinances;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import net.sf.ofx4j.client.BankAccount;
import net.sf.ofx4j.client.FinancialInstitution;
import net.sf.ofx4j.client.FinancialInstitutionService;
import net.sf.ofx4j.client.impl.BaseFinancialInstitutionData;
import net.sf.ofx4j.client.impl.FinancialInstitutionServiceImpl;
import net.sf.ofx4j.domain.data.banking.AccountType;
import net.sf.ofx4j.domain.data.banking.BankAccountDetails;


/**
 * This class just holds scrap code which I don't want to throw away or create clutter.
 * 
 * @author jkomorek
 *
 */
public class Shelf {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		get_BankOfAmerica_OFX();
	}
	
	
	private static void get_BankOfAmerica_OFX() throws Exception {
		BaseFinancialInstitutionData data = new BaseFinancialInstitutionData();
		//data.setBrokerId("053904483");
		data.setFinancialInstitutionId("6812");
		//data.setId(id);
		data.setName("Bank of America, (All states except CA, WA, & ID)");
		data.setOFXURL(new URL("https://ofx.bankofamerica.com/cgi-forte/fortecgi?servicename=ofx_2-3&pagename=ofx"));
		data.setOrganization("HAN");
		
		FinancialInstitutionService service = new FinancialInstitutionServiceImpl();
		FinancialInstitution fi = service.getFinancialInstitution(data);
		
		//get a reference to a specific bank account at your FI
		BankAccountDetails bankAccountDetails = new BankAccountDetails();
		bankAccountDetails.setAccountNumber("223006247233");
		bankAccountDetails.setRoutingNumber("053904483");
		bankAccountDetails.setAccountType(AccountType.CHECKING);
		
		BankAccount bankAccount = fi.loadBankAccount(bankAccountDetails, "jonathankomorek", "G41nn3ss4@ll");
		
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.add(Calendar.DAY_OF_YEAR, -30);
		Date fromDate = fromCalendar.getTime();
		
		bankAccount.readStatement(fromDate, new Date());		
	}


}
