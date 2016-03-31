package test.example;

import test.example.data.House;
import test.example.data.MortgageProductType;
import test.example.data.Person;

public class TestCase {
	// objects needed to test the service
	private Person person;
	private House house;
	private MortgageProductType prodtyp;
	
	// these are the expected values.
	private double amount;
	private int nyears;
	private double incomeRatio;
	
	
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public House getHouse() {
		return house;
	}
	public void setHouse(House house) {
		this.house = house;
	}
	public MortgageProductType getProdtyp() {
		return prodtyp;
	}
	public void setProdtyp(MortgageProductType prodtyp) {
		this.prodtyp = prodtyp;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getNyears() {
		return nyears;
	}
	public void setNyears(int nyears) {
		this.nyears = nyears;
	}
	public double getIncomeRatio() {
		return incomeRatio;
	}
	public void setIncomeRatio(double incomeRatio) {
		this.incomeRatio = incomeRatio;
	}
}