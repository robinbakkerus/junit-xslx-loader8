package flca.xlsx.util;

public class XlsxConvertUtils  
{ 
    private ConvertUtils specialConvertUtils = new EmptyConvertUtils();
	
	public boolean canConvert(Class<?> aClass) {
		return (specialConvertUtils!=null && specialConvertUtils.canConvert(aClass)) ||
				ConvertPrimitivesHelper.canConvert(aClass) || 
				ConvertCommonClassesHelper.canConvert(aClass);
	}


	public Object convert(Class<?> aClass, String aValue) throws XlsxSetValueException {
		if (specialConvertUtils!=null && specialConvertUtils.canConvert(aClass)) {
			return specialConvertUtils.convert(aClass, aValue);
		} else if ( ConvertPrimitivesHelper.canConvert(aClass)) {
			return ConvertPrimitivesHelper.convert(aClass, aValue);
		} else if ( ConvertCommonClassesHelper.canConvert(aClass)) {
			return ConvertCommonClassesHelper.convert(aClass, aValue);
		} else {
			throw new XlsxSetValueException("Can not convert to " + aClass.getName(), aValue);
		}
	}


	public ConvertUtils getSpecialConvertUtils() {
		return specialConvertUtils;
	}


	public void setSpecialConvertUtils(ConvertUtils specialConvertUtils) {
		this.specialConvertUtils = specialConvertUtils;
	}
	
	

}