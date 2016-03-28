package flca.xlsx.util;

/** 
 * 
 *  
 */ 
public class EmptyConvertUtils implements ConvertUtils
{ 
    
    @Override
	public boolean canConvert(Class<?> aClass) {
		return false;
	}


	@Override
	public Object convert(Class<?> aClass, String aValue) throws XlsxSetValueException {
		throw new XlsxSetValueException("EmptyConvertUtils not convert class " + aClass.getName() + " you should provide your own implementation", aValue);
	}

}