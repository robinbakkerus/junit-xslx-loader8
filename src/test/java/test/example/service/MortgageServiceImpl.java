package test.example.service;

import test.example.data.House;
import test.example.data.Mortgage;
import test.example.data.MortgageProductType;
import test.example.data.Person;

public class MortgageServiceImpl implements MortgageService {

	/**
	 * This is the very first impl of this service!
	 */
	public Mortgage calculate(Person client, House house, MortgageProductType type) {
		Mortgage result = new Mortgage();
		result.setAmount(300000d);
		result.setnYears(20);
		result.setIncomeRatio(client.totalIncome() * 0.35);
		return result;
	}
}
