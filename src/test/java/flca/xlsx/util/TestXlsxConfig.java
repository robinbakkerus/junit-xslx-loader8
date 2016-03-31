package flca.xlsx.util;

import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.junit.Test;

public class TestXlsxConfig {

	private final static String CONFIG_FILE = "/config.xlsx";
	private final static String ALIAS_FILE = "/aliases.xlsx";

	@Test
	public void test1() {
		SimpleDateFormat df1 = XlsxConfig.getDateFormats().get(0);
		SimpleDateFormat df2 = XlsxConfig.defaultDateFormat();
		assertTrue(df1.equals(df2));
	}

	@Test
	public void test2() throws XlsxSetValueException {
		SimpleDateFormat df1 = XlsxConfig.findDateFormat("02-02-2014");
		SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
		assertTrue(df1.equals(df2));
	}

	@Test
	public void test3() throws XlsxSetValueException {
		SimpleDateFormat df1 = XlsxConfig.findDateFormat("02-02-2014 12:15");
		SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		assertTrue(df1.equals(df2));
	}
	
	@Test
	public void test4() throws XlsxSetValueException {
		SimpleDateFormat df1 = XlsxConfig.findDateFormat("02-02-2014 12:15:40");
		SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		assertTrue(df1.equals(df2));
	}
	
	@Test
	public void test5() throws XlsxSetValueException {
		SimpleDateFormat df1 = XlsxConfig.findDateFormat("02-02-2014 12:15:40");
		SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		assertTrue(df1.equals(df2));
	}
	
	@Test
	public void test6() {
		int n1 = XlsxConfig.getDateFormats().size();
		XlsxConfig.setDateFormats(new ArrayList<SimpleDateFormat>());
		int n2 = XlsxConfig.getDateFormats().size();
		XlsxConfig.reset();
		int n3 = XlsxConfig.getDateFormats().size();
		assertTrue(n1==n3 && n2 == 0);
	}
	
	@Test
	public void testReadConfigValues() {
		XlsxConfig.maxCols = 50;
		XlsxConfig.setDateFormats(new ArrayList<SimpleDateFormat>());
		XlsxConfig.setAliases(new ArrayList<XlsxAlias>());
		XlsxConfig.setSpecialConvertUtils(null);
		assertTrue(XlsxConfig.getDateFormats().size() == 0);
		assertTrue(XlsxConfig.getAliases().size() == 0);
		XlsxConfig.readFromXlsx("/config.xlsx");
		assertTrue(XlsxConfig.maxCols == 500);
		assertTrue(XlsxConfig.getDateFormats().size()==8);
		assertTrue(XlsxConfig.getAliases().size()==1);
		assertTrue(XlsxConfig.getSpecialConvertUtils()!=null);
		XlsxConfig.reset();
	}

	@Test
	public void testMergeAliases() {
		XlsxConfig.readFromXlsx(CONFIG_FILE);
		assertTrue(XlsxConfig.getAliases().size() == 1);
		XlsxConfig.readFromXlsx(ALIAS_FILE);
		assertTrue(XlsxConfig.getAliases().size() == 2);
	}
	
}
