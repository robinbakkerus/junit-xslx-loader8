package flca.xlsx.util;

public interface ConvertUtils {

	/**
	 * Return true if the given class can be converted
	 * @param aClass
	 * @return boolean 
	 */
	boolean canConvert(final Class<?> aClass);
	
	/**
	 * Given the input class and string return the corresponding object
	 * @param aClass
	 * @param aValue
	 * @return Object
	 */ 
	Object convert(final Class<?> aClass, final String aValue) throws XlsxSetValueException;
	
	

}
