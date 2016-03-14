
package flca.xlsx.util;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Ignore;
import org.junit.Test;

import flca.xlsx.util.ConvertUtils;
import flca.xlsx.util.ConvertUtilsImpl;
import flca.xlsx.util.XlsxSetValueException;

public class TestConvertUtils {

	private ConvertUtils cnv = new ConvertUtilsImpl();

	@Test
	public void testbyte() throws XlsxSetValueException {
		byte r = (byte) cnv.convert(byte.class, "123");
		assertTrue(r == 123);
	}

	@Test
	public void testByte() throws XlsxSetValueException {
		Byte r = (Byte) cnv.convert(Byte.class, "123");
		assertTrue(r == 123);
	}

	@Test
	public void testchar() throws XlsxSetValueException {
		char r = (char) cnv.convert(char.class, "1");
		assertTrue(r == '1');
	}

	@Test
	public void testCharacter() throws XlsxSetValueException {
		Character r = (Character) cnv.convert(Character.class, "1");
		assertTrue(r == '1');
	}

	@Test
	public void testint() throws XlsxSetValueException {
		int r = (int) cnv.convert(int.class, "123");
		assertTrue(r == 123);
	}

	@Test
	public void testInteger() throws XlsxSetValueException {
		Integer r = (Integer) cnv.convert(Integer.class, "123");
		assertTrue(r == 123);
	}

	@Test
	public void testlong() throws XlsxSetValueException {
		long r = (long) cnv.convert(long.class, "123");
		assertTrue(r == 123);
	}

	@Test
	public void testLong() throws XlsxSetValueException {
		Long r = (Long) cnv.convert(Long.class, "123");
		assertTrue(r == 123);
	}

	@Test
	public void testfloat() throws XlsxSetValueException {
		float r = (float) cnv.convert(float.class, "123.45");
		assertTrue(Float.valueOf(r).equals(new Float(123.45)));
	}

	@Test
	public void testFloat() throws XlsxSetValueException {
		Float r = (Float) cnv.convert(Float.class, "123.45");
		assertTrue(Float.valueOf(r).equals(new Float(123.45)));
	}

	@Test
	public void testdouble() throws XlsxSetValueException {
		double r = (double) cnv.convert(double.class, "123.45");
		assertTrue(Double.valueOf(r).equals(new Double(123.45)));
	}

	@Test
	public void testDouble() throws XlsxSetValueException {
		Double r = (Double) cnv.convert(Double.class, "123.45");
		assertTrue(Double.valueOf(r).equals(new Double(123.45)));
	}

	@Test
	public void testboolean() throws XlsxSetValueException {
		boolean r = (boolean) cnv.convert(boolean.class, "T");
		assertTrue(r);
		r = (boolean) cnv.convert(boolean.class, "F");
		assertTrue(!r);
	}

	@Test
	public void testBoolean() throws XlsxSetValueException {
		Boolean r = (Boolean) cnv.convert(Boolean.class, "T");
		assertTrue(r);
		r = (Boolean) cnv.convert(Boolean.class, "F");
		assertTrue(!r);
	}

	@Test
	public void testString() throws XlsxSetValueException {
		String r = (String) cnv.convert(String.class, "test convert");
		assertTrue(r.equals("test convert"));
	}


	@Test
	public void testBigDecimal() throws XlsxSetValueException {
		BigDecimal r = (BigDecimal) cnv.convert(BigDecimal.class, "123.45");
		assertTrue(Double.valueOf(r.doubleValue()).equals(new Double(123.45)));
	}

	@Test
	public void testDate() throws XlsxSetValueException {
		Date r = (Date) cnv.convert(Date.class, "02-03-2016");
		assertTrue(r.equals(new DateTime(2016,3,2,0,0,0).toDate()));
	}

	@Test
	public void testDateTime() throws XlsxSetValueException {
		DateTime r = (DateTime) cnv.convert(DateTime.class, "02-03-2016");
		assertTrue(r.equals(new DateTime(2016,3,2,0,0,0)));
	}


	@Test
	public void testJodaDateTime() throws XlsxSetValueException {
		DateTime r = (DateTime) cnv.convert(DateTime.class, "02-03-2016");
		assertTrue(r.equals(new DateTime(2016,3,2,0,0,0)));
	}

	@Test
	public void testJodaLocalDate() throws XlsxSetValueException {
		LocalDate r = (LocalDate) cnv.convert(LocalDate.class, "02-03-2016");
		assertTrue(r.equals(new LocalDate(2016,3,2)));
	}

	@Test
	public void testSqlDate() throws XlsxSetValueException {
		java.sql.Date r = (java.sql.Date) cnv.convert(java.sql.Date.class, "02-03-2016");
		assertTrue(r.equals(new java.sql.Date(new DateTime(2016,3,2,0,0,0).toDate().getTime())));
	}

	@Ignore //TODO
	public void testSqlTime() throws XlsxSetValueException {
		java.sql.Time r = (java.sql.Time) cnv.convert(java.sql.Time.class, "10:30");
		assertTrue(r.equals(new java.sql.Time(new DateTime(2016,3,2,12,10,30).toDate().getTime())));
	}


	//-- test null and empty values
	@Test (expected=XlsxSetValueException.class)
	public void testNullString()  throws XlsxSetValueException {
		cnv.convert(String.class, null);
	}

	@Test (expected=XlsxSetValueException.class)
	public void testNullDate()  throws XlsxSetValueException {
		cnv.convert(Date.class, null);
	}

	@Test
	public void testEmptyString()  throws XlsxSetValueException {
		String r = (String) cnv.convert(String.class, "");
		assertTrue(r.equals(""));
	}

	@Test  (expected=XlsxSetValueException.class)
	public void testNullChar()  throws XlsxSetValueException {
		cnv.convert(char.class, null);
	}

	@Test (expected=XlsxSetValueException.class)
	public void testEmptyChar()  throws XlsxSetValueException {
		cnv.convert(char.class, "");
	}}

