package test.example;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import flca.xlsx.util.Xlsx;
import flca.xlsx.util.XlsxConfig;
import flca.xlsx.util.XlsxDataWriter;
import test.example.data.House;
import test.example.data.Job;
import test.example.data.Mortgage;
import test.example.data.MortgageProductType;
import test.example.data.Person;
import test.example.service.MortgageServiceImpl;

public class TestExample {

	private static final String FILENAME = "/testdata/example.xlsx";
	
	/*
	 * In this example we provide our own mapper to convert a stringvalue to the Salary object
	 * Here in the setup we register this mapper.
	 * This mapper looks like this:
	 
	 public class ExampleConvertUtils extends ConvertUtilsImpl implements ConvertUtils {

	 @Override
	 public boolean canConvert(Class<?> aClass) {
		return super.canConvert(aClass) || Salary.class.equals(aClass);
 	 } 

	 @Override
	 public Object convert(Class<?> aClass, String aValue) throws XlsxSetValueException {
	    if (aClass.equals(Salary.class)) {
	      return Salary.withAmount(new BigDecimal(aValue));
	    } else {
	      return super.convert(aClass, aValue);
	    }  
	 }
	 */
	@BeforeClass
	public static void setup() {
		XlsxConfig.sConvertUtils = new ExampleConvertUtils();
	}
	
	/*
	* @Test We only have to this once, to create an empty excel template !
	*/
	@Test
	public void testMakeTemplateExcel() {
		XlsxDataWriter.writeXlsxFile("/tmp/example.xlsx",  
				Person.class, Person.class, House.class, Job.class, MortgageProductType.class, TestCase.class );
	}

	/*
	 * Here we the test different scenario's for the MortgageService. The service is called like ths:
	 * Mortgage result = new MortgageServiceImpl().calculate(aPerson, aHouse, aProductType);
	 * We loop through all the excel sheets, and all the nr's that belong to TestCase.class 
	 * and call the methode testTestcase with the sheet and nr.
	 */
	@Test
	public void testCalcMortgage() {
		Xlsx xls = new Xlsx(FILENAME);
		for (byte sheet=0; sheet<xls.sheetCount(); sheet++) {
			for (Integer nr : xls.getAllNrs(TestCase.class, sheet)) {
				testTestcase(xls, sheet, nr);
			}
		}
	}
	
	/*
	 * With the given sheet and nr we create all the objects (Person, House, ProductType) to execute the mortgage, 
	 * and in addition we also create to dedicated class just for this junit test:  Testcase, that contains the expected result values.
	 * Alternatively we could have put extra properties in TestCase (like Person person, House house etc), than only make the TestCase object,
	 * and grap the object needed for the service from this TestCase instance.
	 * Than we run the mortgage service and finally the result with expected results.
	 */
	private void testTestcase(Xlsx xls, byte sheet, int nr) {
		System.out.println("testing " + sheet + "/" + nr);
		TestCase testcase = (TestCase) xls.make(TestCase.class, sheet, nr);
		Person testPerson = (Person) xls.make(Person.class, sheet, nr);
		House testHouse = (House) xls.make(House.class, sheet, nr);
		MortgageProductType type = (MortgageProductType) xls.make(MortgageProductType.class, sheet, nr);
		Mortgage result = new MortgageServiceImpl().calculate(testPerson, testHouse, type);
		assertEquals(result.getAmount().doubleValue(),testcase.getAmount(), 1.0d);
		assertEquals(result.getnYears(),testcase.getNyears(), 1.0d);
		assertEquals(result.getIncomeRatio().doubleValue(),testcase.getIncomeRatio(), 1.0d);
	}

}
