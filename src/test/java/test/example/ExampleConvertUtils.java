package test.example;

import java.math.BigDecimal;

import flca.xlsx.util.ConvertUtils;
import flca.xlsx.util.ConvertUtilsImpl;
import flca.xlsx.util.XlsxSetValueException;
import test.example.data.Salary;

public class ExampleConvertUtils extends ConvertUtilsImpl implements ConvertUtils {

	@Override
	public boolean canConvert(Class<?> aClass) {
		return super.canConvert(aClass) || Salary.class.equals(aClass);
	}

	  @Override
	  public Object convert(Class<?> aClass, String aValue) throws XlsxSetValueException {
	    if (aClass.equals(Salary.class)) {
	      return Salary.withAmount(new BigDecimal(aValue));
	    } else {
	      return super.convert(aClass, aValue);
	    }  
	  }
}
