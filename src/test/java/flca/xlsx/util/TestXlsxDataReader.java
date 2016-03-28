package flca.xlsx.util;

import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.junit.Test;

import data.Foo;
import data.FooType;

public class TestXlsxDataReader {

	private static final byte SHEET1 = 0;
	private static final String FILENAME1 = "/testdata/foo.xlsx";
	private static final String FILENAME2 = "/testdata/test-reader.xlsx";
	private static final String FILENAME3 = "/testdata/example.xlsx";
	
	@Test
	public void testGetValues() throws Exception {
		Xlsx xls = new Xlsx(FILENAME1);
		XlsxData xlsdata = xls.getData(Foo.class, (byte)0).get(0);
		assertTrue(xlsdata.getFqn().equals(Foo.class.getName()));
	}

	@Test
	public void testMakeObject() throws Exception {
		Xlsx xls = new Xlsx(FILENAME1);
		Foo foo = (Foo) xls.make(Foo.class, SHEET1, 1);
		assertTrue(foo != null);
		assertTrue(foo.getBar() != null);
		assertTrue(foo.getBasColl() != null && foo.getBasColl().size()==2); 
		assertTrue(foo.getBasSet() != null && foo.getBasSet().size()==2);
		assertTrue(foo.getIntArray() != null && foo.getIntArray().length == 3);
	}

	@Test
	public void testReadKnownObject() throws Exception {
		Xlsx xls = new Xlsx(FILENAME2);
		Date r = (Date) xls.make(Date.class, SHEET1, 1);
		assertTrue(r != null && r.equals(new DateTime(1954,11,9,0,0,0).toDate()));
	}

	@Test
	public void testReadMultiNrs() throws Exception {
		Xlsx xls = new Xlsx(FILENAME2);
		FooType r = (FooType) xls.make(FooType.class, SHEET1, 1);
		assertTrue(r != null && r.equals(FooType.FOO_1));
		r = (FooType) xls.make(FooType.class, SHEET1, 2);
		assertTrue(r != null && r.equals(FooType.FOO_2));
		r = (FooType) xls.make(FooType.class, SHEET1, 10);
		assertTrue(r != null && r.equals(FooType.FOO_3));
	}

	@Test
	public void testGetAllNrs() throws Exception {
		Xlsx xls = new Xlsx(FILENAME2);
		Set<Integer> nrs = xls.getAllNrs(FooType.class, SHEET1);
		assertTrue(nrs != null && nrs.size()==10);
	}
		
	@Test
	public void testDateFmt() throws Exception {
		Xlsx xls = new Xlsx(FILENAME2);
		Date r = (Date) xls.make(Date.class, SHEET1, 2);
		assertTrue(r != null && r.equals(new DateTime(1954,11,9,15,30,0).toDate()));
		
		r = (Date) xls.make(Date.class, SHEET1, 3);
		assertTrue(r != null && r.equals(new DateTime(1970,1,1,15,30,0).toDate()));

		r = (Date) xls.make(Date.class, SHEET1, 4);
		assertTrue(r != null && r.equals(new DateTime(1970,1,1,15,30,50).toDate()));
	}

	@Test
	public void testReferToOtherDate() throws Exception {
		Xlsx xls = new Xlsx(FILENAME2);
		Foo foo = (Foo) xls.make(Foo.class, SHEET1, 1);
		assertTrue(foo.getDatum().equals(new DateTime(1954,11,9,0,0,0).toDate()));
	}
	
	@Test 
	public void testEmptyString() throws Exception {
		Xlsx xls = new Xlsx(FILENAME2);
		Foo foo = (Foo) xls.make(Foo.class, SHEET1, 1);
		assertTrue(foo.getNaam() != null && foo.getNaam().isEmpty());
	}

	@Test 
	public void testMultiLineString() throws Exception {
		Xlsx xls = new Xlsx(FILENAME2);
		Foo foo = (Foo) xls.make(Foo.class, SHEET1, 2);
		assertTrue(foo.getNaam() != null && foo.getNaam().length()>8);
	}


	@Test
	public void testOtherDateFormats() throws Exception {
		Xlsx xls = new Xlsx(FILENAME2);
		List<SimpleDateFormat> defaultDateFormats = XlsxConfig.getDateFormats();
		List<SimpleDateFormat> myDateFormats = new ArrayList<SimpleDateFormat>();
		myDateFormats.add(new SimpleDateFormat("dd/MM/yyyy"));
		XlsxConfig.setDateFormats(myDateFormats);
		Date r = (Date) xls.make(Date.class, SHEET1, 5);
		XlsxConfig.setDateFormats(defaultDateFormats); //reset to defaults!
		assertTrue(r != null && r.equals(new DateTime(1954,11,9,0,0,0).toDate()));
	}
	
	@Test
	public void testAlias() throws Exception {
		Xlsx xls = new Xlsx(FILENAME1);
		XlsxConfig.reset();
		List<XlsxAlias> aliasList = new ArrayList<XlsxAlias>();
		aliasList.add(new XlsxAlias("*", "veryLongPropertyName", "vlpn"));
		XlsxConfig.setAliases(aliasList);
		Foo foo = (Foo) xls.make(Foo.class, SHEET1, 10);
		assertTrue(foo.getVeryLongPropertyName().equals("ABC"));
	}
	
	@Test
	public void testSheetCount() {
		Xlsx xls = new Xlsx(FILENAME3);
		assertTrue(2==xls.sheetCount());
	}
}
