package test.example;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import flca.xlsx.util.Xlsx;
import test.example.data.House;
import test.example.data.Mortgage;
import test.example.data.MortgageProductType;
import test.example.data.Person;
import test.example.service.MortgageServiceImpl;

/**
 * this test is similar to TestExample except for the setup
 * @author robin
 *
 */
public class TestExample2 {

	private static final String FILENAME = "/testdata/example.xlsx";
	private static final String CONFIG_FILENAME = "/config.xlsx";
	private static Xlsx xlsx;
	
	@BeforeClass
	public static void setup() {
		xlsx = new Xlsx(FILENAME, CONFIG_FILENAME);
	}
	

	/*
	 * Here we the test different scenario's for the MortgageService. The service is called like ths:
	 * Mortgage r = new MortgageServiceImpl().calculate(aPerson, aHouse, aProductType);
	 * We loop through all the excel sheets, and all the nr's that belong to TestCase.class 
	 * and call the methode testTestcase with the sheet and nr.
	 */
	@Test
	public void testCalcMortgage() {
		for (byte sheet=0; sheet < xlsx.sheetCount(); sheet++) {
			for (Integer nr : xlsx.getAllNrs(TestCase.class, sheet)) {
				testTestcase(xlsx, sheet, nr);
			}
		}
	}
	
	/*
	 * With the given sheet and nr we create all the objects (Person, House, ProductType) to execute the mortgage, 
	 * and in addition we also create to dedicated class just for this junit test:  Testcase, that contains the expected r values.
	 * Alternatively we could have put extra properties in TestCase (like Person person, House house etc), than only make the TestCase object,
	 * and grap the object needed for the service from this TestCase instance.
	 * Than we run the mortgage service and finally the r with expected results.
	 */
	private void testTestcase(Xlsx xls, byte sheet, int nr) {
		System.out.println("testing " + sheet + "/" + nr);
		TestCase testcase = (TestCase) xls.make(TestCase.class, sheet, nr);
		Person testPerson = (Person) xls.make(Person.class, sheet, nr);
		House testHouse = (House) xls.make(House.class, sheet, nr);
		MortgageProductType type = (MortgageProductType) xls.make(MortgageProductType.class, sheet, nr);
		Mortgage r = new MortgageServiceImpl().calculate(testPerson, testHouse, type);
		assertEquals(r.getAmount().doubleValue(),testcase.getAmount(), 1.0d);
		assertEquals(r.getnYears(),testcase.getNyears(), 1.0d);
		assertEquals(r.getIncomeRatio().doubleValue(),testcase.getIncomeRatio(), 1.0d);
	}

}
