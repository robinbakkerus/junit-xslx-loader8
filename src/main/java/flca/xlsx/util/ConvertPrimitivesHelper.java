
package flca.xlsx.util;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 */
final class ConvertPrimitivesHelper {

	private static final Logger LOGGER = Logger.getLogger(ConvertPrimitivesHelper.class.getName());

	@SuppressWarnings("serial")
	private static final Set<Class<?>> TRANSFERABLES = new HashSet<Class<?>>() {
		{
			add(byte.class);
			add(Byte.class);
			add(int.class);
			add(Integer.class);
			add(long.class);
			add(Long.class);
			add(float.class);
			add(Float.class);
			add(double.class);
			add(Double.class);
			add(short.class);
			add(Short.class);
			add(boolean.class);
			add(Boolean.class);
			add(char.class);
			add(Character.class);
			add(String.class);
		}
	};

	/**
	 * Returns true if the given class (a Java primitive or object counterpart)
	 * can be converted.
	 * 
	 * @param clz,
	 *            Class<?>
	 * @return boolean
	 */
	public static boolean canConvert(Class<?> clz) {
		return TRANSFERABLES.contains(clz);
	}

	/**
	 * Return an object of the given type filled with the given String value.
	 * The input must a valid non-empty string.
	 * 
	 * @param targetClass,
	 *            Class<?>
	 * @param value,
	 *            String
	 * @return Object
	 * @throws XlsxSetValueException
	 */
	public static Object convert(Class<?> targetClass, String value) throws XlsxSetValueException {
		if (value == null) {
			final String msg = "string2Object called with null " + targetClass.getName();
			throw new XlsxSetValueException(msg, null);
		} else if (value.isEmpty()) {
			return emptyString2Object(targetClass, "");
		} else {
			return stringValue2Object(targetClass, value);
		}
	}

	private static Object emptyString2Object(Class<?> targetClass, String value) throws XlsxSetValueException {
		if (targetClass.equals(String.class)) {
			return value;
		} else {
			final String msg = "string2Object called with empty value " + targetClass.getName();
			throw new XlsxSetValueException(msg, value);
		}
	}

	private static Object stringValue2Object(Class<?> targetClass, String value) throws XlsxSetValueException {
		if (targetClass.equals(String.class)) {
			return string2String(value);
		} else if (targetClass.equals(Character.class) || targetClass.equals(char.class)) {
			return string2Char(value);
		} else if (targetClass.equals(Byte.class) || targetClass.equals(byte.class)) {
			return string2Byte(value);
		} else if (targetClass.equals(Integer.class) || targetClass.equals(int.class)) {
			return string2Integer(value);
		} else if (targetClass.equals(Short.class) || targetClass.equals(short.class)) {
			return string2Short(value);
		} else if (targetClass.equals(Long.class) || targetClass.equals(long.class)) {
			return string2Long(value);
		} else if (targetClass.equals(Float.class) || targetClass.equals(float.class)) {
			return string2Float(value);
		} else if (targetClass.equals(Double.class) || targetClass.equals(double.class)) {
			return string2Double(value);
		} else if (targetClass.equals(Boolean.class) || targetClass.equals(boolean.class)) {
			return string2Boolean(value);
		} else {
			final String msg = "Can not convert " + value + " to " + targetClass.getName();
			LOGGER.severe(msg);
			throw new XlsxSetValueException("Can not convert " + value + " to " + targetClass.getName(), value);
		}
	}

	private static String string2String(String aValue) throws XlsxSetValueException {
		try {
			byte b1 = (byte) aValue.charAt(0);
			byte b2 = (byte) aValue.charAt(aValue.length() - 1);
			if (b1 == 28 && b2 == 29) {
				if (aValue.length() > 2) {
					LOGGER.warning("A string doesn't have to be surounded with quotes");
				}
				return aValue.substring(1, aValue.length() - 1);
			} else {
				return aValue;
			}
		} catch (NumberFormatException e) {
			handleError("string2Char", e, aValue);
			return null;
		}
	}

	private static Character string2Char(String aValue) throws XlsxSetValueException {
		try {
			return new Character(aValue.charAt(0));
		} catch (NumberFormatException e) {
			handleError("string2Char", e, aValue);
			return null;
		}
	}

	private static Byte string2Byte(String aValue) throws XlsxSetValueException {
		try {
			return new Byte(aValue);
		} catch (NumberFormatException e) {
			handleError("string2Byte", e, aValue);
			return null;
		}
	}

	private static Integer string2Integer(String aValue) throws XlsxSetValueException {
		try {
			return new Integer(aValue);
		} catch (NumberFormatException e) {
			handleError("string2Integer", e, aValue);
			return null;
		}
	}

	private static long string2Long(String aValue) throws XlsxSetValueException {
		try {
			return Long.parseLong(aValue);
		} catch (NumberFormatException e) {
			handleError("string2Long", e, aValue);
			return 0;
		}
	}

	private static short string2Short(String aValue) throws XlsxSetValueException {
		try {
			return Short.parseShort(aValue);
		} catch (NumberFormatException e) {
			handleError("string2Short", e, aValue);
			return (short) 0;
		}
	}

	private static double string2Double(String aValue) throws XlsxSetValueException {
		try {
			return Double.parseDouble(aValue);
		} catch (NumberFormatException ex) {
			String msg = "invalid float " + " (" + aValue + ")";
			handleError("string2Double " + msg, ex, aValue);
			return 0.0;
		}
	}

	private static Float string2Float(String aValue) throws XlsxSetValueException {
		return new Float((float) string2Double(aValue));
	}

	private static boolean string2Boolean(String aValue) throws XlsxSetValueException {
		String s = aValue.toUpperCase().substring(0, 1);
		return (s.equals("T") || s.equals("1") || s.equals("J") || s.equals("Y"));
	}

	private static void handleError(String msg, Exception ex, Object val) throws XlsxSetValueException {
		throw new XlsxSetValueException(msg, val, ex);
	}

}
