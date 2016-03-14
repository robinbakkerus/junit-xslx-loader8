package flca.xlsx.util;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;

import data.Bar;
import data.Foo;
import flca.xlsx.util.Xlsx;
import flca.xlsx.util.XlsxDataUtilsException;

public class TestUnhappyFlows {

	private static final byte SHEET1 = 0;
	private static final String FILENAME = "/testdata/test-errors.xlsx";
	
	@Test (expected=XlsxDataUtilsException.class)
	public void testUnknownFile() throws Exception {
		Xlsx xls = new Xlsx("unknown-file");
		xls.sheetCount();
	}

	@Test (expected=XlsxDataUtilsException.class)
	public void testUnknownClass() throws Exception {
		Xlsx xls = new Xlsx(FILENAME);
		xls.make(DateTime.class, SHEET1,  1);
	}

	@Test (expected=XlsxDataUtilsException.class)
	public void testUnknownNr() throws Exception {
		Xlsx xls = new Xlsx(FILENAME);
		Object obj = xls.make(Date.class, SHEET1,  2);
		System.out.println(obj);
	}

	@Test (expected=XlsxDataUtilsException.class)
	public void tesBadData() throws Exception {
		Xlsx xls = new Xlsx(FILENAME);
		Object obj = xls.make(Date.class, SHEET1,  1);
		System.out.println(obj);
	}
	
	@Test (expected=XlsxDataUtilsException.class)
	public void tesNoSuchNestedNr() throws Exception {
		Xlsx xls = new Xlsx(FILENAME);
		Object obj = xls.make(Foo.class, SHEET1,  1);
		System.out.println(obj);
	}
	
	@Test (expected=XlsxDataUtilsException.class)
	public void tesNoSuchProperty() throws Exception {
		Xlsx xls = new Xlsx(FILENAME);
		Object obj = xls.make(Bar.class, SHEET1,  1);
		System.out.println(obj);
	}
	
}
