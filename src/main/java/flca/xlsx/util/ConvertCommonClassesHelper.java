package flca.xlsx.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * 
 * @author rbakkerus
 * @version $Date: $ $Revision: $
 * 
 */
final class ConvertCommonClassesHelper {

	@SuppressWarnings("serial")
	private static final List<Class<?>> TRANSFERABLES = new ArrayList<Class<?>>() {
		{
			add(Date.class);
			add(java.sql.Date.class);
			add(DateTime.class);
			add(LocalDate.class);
			add(BigDecimal.class);
		}
	};

	private static SimpleDateFormat getDateFormat(final String value) throws XlsxSetValueException {
		return XlsxConfig.findDateFormat(value);
	}

	/**
	 * Returns true if the given class can be converted.
	 * The following classes are supported: 
	 * java.util.Date, org.joda.time.DateTime, org.joda.time.LocalDate, BigDecimal
	 * @param clz, Class<?>
	 * @return boolean
	 */
	public static boolean isTransferable(Class<?> clz) {
		return TRANSFERABLES.contains(clz);
	}

	/**
	 * Return an object of the given type filled with the given String value. The input must a valid non-empty string. 
	 * @param targetClass, Class<?>
	 * @param value, String
	 * @return Object
	 * @throws XlsxSetValueException
	 */
	public static Object string2Object(Class<?> targetClass, String value) throws XlsxSetValueException {
		if (value == null || value.isEmpty()) {
			final String msg = "string2Object called with null or empty value " + targetClass.getName();
			throw new XlsxSetValueException(msg, value);
		} else {
			if (targetClass.equals(Date.class)) {
				return string2Date(value);
			} else if (targetClass.equals(java.sql.Date.class)) {
				return string2SqlDate(value);
			} else if (targetClass.equals(java.sql.Time.class)) {
				return string2SqlTime(value);
			} else if (targetClass.equals(LocalDate.class)) {
				return string2LocalDate(value);
			} else if (targetClass.equals(DateTime.class)) {
				return string2DateTime(value);
			} else if (targetClass.equals(BigDecimal.class)) {
				return string2BigDecimal(value);
			} else {
				throw new XlsxSetValueException("Can not convert to " + targetClass.getName(), value);
			}
		}
	}

	// -------------- convert methods --------------------

	private static BigDecimal string2BigDecimal(String aValue) {
		if (aValue != null && aValue.trim().length() > 0) {
			return new BigDecimal(aValue);
		} else {
			return null;
		}
	}

	private static Date string2Date(String aValue) throws XlsxSetValueException {
		try {
			return getDateFormat(aValue).parse(aValue);
		} catch (Exception ex) {
			throw new XlsxSetValueException("string2Date", aValue, ex);
		}
	}

	private static java.sql.Date string2SqlDate(String aValue) throws XlsxSetValueException {
		try {
			Date date = string2Date(aValue);
			return new  java.sql.Date(date.getTime());
		} catch (Exception ex) {
			throw new XlsxSetValueException("string2SqlDate", aValue, ex);
		}
	}

	private static java.sql.Time string2SqlTime(String aValue) throws XlsxSetValueException {
		try {
			Date date = string2Date(aValue);
			return new  java.sql.Time(date.getTime());
		} catch (Exception ex) {
			throw new XlsxSetValueException("string2SqlDate", aValue, ex);
		}
	}
	

	private static LocalDate string2LocalDate(String aValue) throws XlsxSetValueException {
		try {
			Date date = string2Date(aValue);
			return new LocalDate(date.getTime());
		} catch (Exception ex) {
			throw new XlsxSetValueException("string2LocalDate", aValue, ex);
		}
	}

	private static DateTime string2DateTime(String aValue) throws XlsxSetValueException {
		try {
			Date date = string2Date(aValue);
			return new DateTime(date.getTime());
		} catch (Exception ex) {
			throw new XlsxSetValueException("string2DateTime", aValue, ex);
		}
	}


}