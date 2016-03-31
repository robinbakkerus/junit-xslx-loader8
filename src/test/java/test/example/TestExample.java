package test.example;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import flca.xlsx.util.Xlsx;
import flca.xlsx.util.XlsxConfig;
import flca.xlsx.util.XlsxConfigValues;
import flca.xlsx.util.XlsxDataWriter;
import test.example.data.House;
import test.example.data.Job;
import test.example.data.Mortgage;
import test.example.data.MortgageProductType;
import test.example.data.Person;
import test.example.service.MortgageServiceImpl;

public class TestExample {

	private static final String FILENAME = "/testdata/example.xlsx";
	private static final String COMMON_CONFIG = "/config.xlsx";
	private static final String MY_ALIASES = "/testdata/example-aliases.xlsx";
	
	/*
	 * In this example we provide our own mapper to convert a stringvalue to the Salary object
	 * Here in the setup we register this mapper.
	 * This mapper looks like this:
	 
	 public class ExampleConvertUtils extends ConvertUtilsImpl implements ConvertUtils {

	 @Override
	 public boolean canConvert(Class<?> aClass) {
		return Salary.class.equals(aClass);
 	 } 

	 @Override
	 public Object convert(Class<?> aClass, String aValue) throws XlsxSetValueException {
	    if (aClass.equals(Salary.class)) {
	      return Salary.withAmount(new BigDecimal(aValue));
	    } else {
	      throw new XlsxSetValueException("Can not convert " + aClass.getName(), aValue, null);
	    }  
	 }
	 */
	@BeforeClass
	public static void setup() {
		XlsxConfig.readFromXlsx(COMMON_CONFIG);
	}
	
	/*
	* @Test We only have to this once, to create an empty excel template !
	*/
	@Ignore
	public void testMakeTemplateExcel() {
		XlsxDataWriter.writeXlsxFile("/tmp/example.xlsx",  
				Person.class, Person.class, House.class, Job.class, MortgageProductType.class, TestCase.class );
		XlsxDataWriter.writeXlsxFile("/tmp/config.xlsx",  
				XlsxConfigValues.class, SimpleDateFormat.class );
	}

	/*
	 * Here we the test different scenario's for the MortgageService. The service is called like ths:
	 * Mortgage r = new MortgageServiceImpl().calculate(aPerson, aHouse, aProductType);
	 * We loop through all the excel sheets, and all the nr's that belong to TestCase.class 
	 * and call the methode testTestcase with the sheet and nr.
	 */
	@Test
	public void testCalcMortgage() {
		Xlsx xls = new Xlsx(FILENAME, MY_ALIASES);
		for (byte sheet=0; sheet<xls.sheetCount(); sheet++) {
			for (Integer nr : xls.getAllNrs(TestCase.class, sheet)) {
				testTestcase(xls, sheet, nr);
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
		Person testPerson = testcase.getPerson();
		House testHouse = testcase.getHouse();
		MortgageProductType type = testcase.getProdtyp();
		Mortgage r = new MortgageServiceImpl().calculate(testPerson, testHouse, type);
		assertEquals(r.getAmount().doubleValue(),testcase.getAmount(), 1.0d);
		assertEquals(r.getnYears(),testcase.getNyears(), 1.0d);
		assertEquals(r.getSalaryIncomeRatio().doubleValue(),testcase.getIncomeRatio(), 1.0d);
	}

}
