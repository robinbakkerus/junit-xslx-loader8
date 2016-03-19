package test.example.data;

import java.math.BigDecimal;

public class Salary {

	private BigDecimal amount;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public static Salary withAmount(BigDecimal aValue) {
		Salary r = new Salary();
		r.setAmount(aValue);
		return r;
	}
	
}
