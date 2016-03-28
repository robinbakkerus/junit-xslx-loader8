package test.example;

import java.math.BigDecimal;

import flca.xlsx.util.ConvertUtils;
import flca.xlsx.util.XlsxSetValueException;
import test.example.data.Salary;

public class ExampleConvertUtils implements ConvertUtils {

	@Override
	public boolean canConvert(Class<?> aClass) {
		return Salary.class.equals(aClass);
	}

	@Override
	public Object convert(Class<?> aClass, String aValue) throws XlsxSetValueException {
		if (aClass.equals(Salary.class)) {
			return Salary.withAmount(new BigDecimal(aValue));
		} else {
			throw new XlsxSetValueException("Can not convert " + aClass.getName(), aValue, null);
		}
	}
}
