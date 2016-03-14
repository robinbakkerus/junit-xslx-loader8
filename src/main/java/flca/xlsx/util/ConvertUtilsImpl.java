package flca.xlsx.util;

/** 
 * 
 *  
 */ 
public class ConvertUtilsImpl implements ConvertUtils
{ 
    
    @Override
	public boolean canConvert(Class<?> aClass) {
		return ConvertPrimitivesHelper.isTransferable(aClass) || ConvertCommonClassesHelper.isTransferable(aClass);
	}


	@Override
	public Object convert(Class<?> aClass, String aValue) throws XlsxSetValueException {
		if ( ConvertPrimitivesHelper.isTransferable(aClass)) {
			return ConvertPrimitivesHelper.string2Object(aClass, aValue);
		} else if ( ConvertCommonClassesHelper.isTransferable(aClass)) {
			return ConvertCommonClassesHelper.string2Object(aClass, aValue);
		} else {
			throw new XlsxSetValueException("Can not convert to " + aClass.getName(), aValue);
		}
	}

}