package flca.xlsx.util;

import java.beans.IntrospectionException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;


class ReflectionHelper extends AbstractXlsxUtils {

	private static final Logger LOGGER = Logger.getLogger(ReflectionHelper.class.getName());

	private static final String DELIM = ",";
	
	private XlsxConvertUtils convertUtils;
	private Map<Byte, XlsxDataHash> xlsDataMap = null;
	private byte activeSheetNr;

	ReflectionHelper(byte activeSheetNr, Map<Byte, XlsxDataHash> xlsDataMap, XlsxConvertUtils convertUtils) {
		super();
		this.activeSheetNr = activeSheetNr;
		this.convertUtils = convertUtils;
		this.xlsDataMap = xlsDataMap;
	}

	Object makeObject(final Class<?> clz, final int nr)
			throws XlsxSetValueException, NoSuchFieldException, SecurityException, IntrospectionException 
	{
		XlsxData xlsdata = getXlsxData(clz.getName(), nr);
		if (convertUtils.canConvert(clz)) {
			return makeKnownObjectint(clz, nr, xlsdata);
		} else if (clz.isEnum()) {
			return makeEnum(clz, nr, xlsdata);
		} else {
			return makeAndFillObject(nr, xlsdata);
		}
	}

	private Object makeKnownObjectint(Class<?> clz, int nr, XlsxData xlsdata) throws IntrospectionException, NoSuchFieldException,XlsxSetValueException 
	{
		final String value = xlsdata.getValue(nr,  0);
		return convertUtils.convert(clz, value);
	}
	
	private Object makeAndFillObject(int nr, XlsxData xlsdata) throws IntrospectionException, NoSuchFieldException,XlsxSetValueException 
	{
		Object target = makeInstance(xlsdata);

		int nCols = xlsdata.getNames().length;
		for (int col = 0; col < nCols; col++) {
			setValue(nr, xlsdata, target, col);
		}

		return target;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Enum makeEnum(final Class<?> clz, final int nr, final XlsxData xlsdata) {
		String enumval = xlsdata.getValue(nr, 0);
		return Enum.valueOf((Class<? extends Enum>)clz, enumval);
	}

	private void setValue(int nr, XlsxData xlsdata, Object target, int col)
			throws IntrospectionException, NoSuchFieldException, SecurityException, XlsxSetValueException {
		String propname = null;
		String value = null;
		try {
			propname = xlsdata.getNames()[col];
			value = xlsdata.getValue(nr, col);
			if (value != null) {
				Method setter = getSetter(target, propname);
				Class<?> proptyp = getPropType(setter);
				PropKind propkind = getPropertyKind(proptyp, propname);
				if (PropKind.SINGLE_PROP.equals(propkind)) {
					setPropValue(target, proptyp, setter, value);
				} else if (PropKind.ENUM_PROP.equals(propkind)) {
					setEnumValue(target, proptyp, setter, value, xlsdata);
				} else if (PropKind.SINGLE_REL.equals(propkind)) {
					setRelation(target, proptyp, setter, value);
				} else if (propkind.equals(PropKind.MULTI_REL)) {
					if (setter.getParameterTypes().length==1 && setter.getParameterTypes()[0].isArray()) {
						setArray(target, setter, value);
					} else {
						setMultiRelations(target, setter, value);
					}
				}
			}
		} catch (Exception e) {
			final String msg = "error setting propery " + propname + " with " + value;
			LOGGER.severe(msg);
			throw new XlsxSetValueException(msg, value);
		}
	}

	private Method getSetter(Object target, String propname) throws NoSuchFieldException, IntrospectionException {
		Method method = MethodHelper.getSetter(target, propname);
		if (method != null) {
			return method;
		} else {
			throw new NoSuchFieldException("No such field " + propname + " in " + target.getClass().getName());
		}
		
	}
	
	private Class<?> getGenericType(Method setter) {
		Type[] genericParameterTypes = setter.getGenericParameterTypes();
		for (int i = 0; i < genericParameterTypes.length; i++) {
			if (genericParameterTypes[i] instanceof ParameterizedType) {
				Type[] parameters = ((ParameterizedType) genericParameterTypes[i]).getActualTypeArguments();
				if (parameters != null && parameters.length == 1) {
					return (Class<?>) parameters[0];
				}
			}
		}
		return null;
	}

	private Class<?> getPropType(Method setter) throws NoSuchFieldException {
		if (setter.getParameterTypes().length == 1) {
			return setter.getParameterTypes()[0];
		} else {
			throw new NoSuchFieldException(setter.getName() + " should have exactly 1 parameter");
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setEnumValue(Object target, Object propType, Method setter, String value, XlsxData xlsdata)
			throws XlsxSetValueException {

		try {
			if (isNumeric(value)) {
				setter.invoke(target,
						Enum.valueOf((Class<Enum>) propType, getStringValue(xlsdata, propType, Integer.valueOf(value))));
			} else {
				setter.invoke(target, Enum.valueOf((Class<Enum>) propType, value));
			}
		} catch (Exception e) {
			throw new XlsxSetValueException("Can not set enum with " + value, target);
		}
	}

	private String getStringValue(XlsxData xlsdata, Object target, int nr) {
		final XlsxData xlsEnumData = getXlsxData((Class<?>) target, nr);
		return xlsEnumData.getValue(nr, 0);
	}

	private void setPropValue(Object target, Object propType, Method setter, String value) throws XlsxSetValueException {
		if (isRefToNr(propType, value)) {
			final XlsxData xlsdata = getXlsxData((Class<?>)propType, getNr(value));
			setThePropValue(target, propType, setter, getStringValue(xlsdata, propType, getNr(value)));
		} else {
			setThePropValue(target, propType, setter, value);
		}
	}
	
	private boolean isRefToNr(Object propType, String value) {
		Class<?> clz =(Class<?>)propType;
		return isNumeric(value) && !clz.isPrimitive() && getXlsxData((Class<?>)propType, getNr(value)) != null;
	}

	private void setThePropValue(Object target, Object propType, Method setter, String value) throws XlsxSetValueException {
		if (convertUtils.canConvert((Class<?>) propType)) {
			try {
				setter.invoke(target, convertUtils.convert((Class<?>) propType, value));
			} catch (Exception e) {
				final String msg = "Error setting via " + setter.getName() + " with " + value + " :" + e.getMessage();
				LOGGER.severe(msg);
				throw new XlsxSetValueException(msg, value);
			}
		}
	}

	private void setRelation(Object target, Class<?> propType, Method setter, String value)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, XlsxSetValueException,
			NumberFormatException, NoSuchFieldException, SecurityException, IntrospectionException {

		int nr = getNr(value);
		Object obj = makeObject(propType, nr);
		if (obj != null) {
			setter.invoke(target, new Object[] { obj });
		}
	}

	private void setMultiRelations(Object target, Method setter, String value) throws XlsxSetValueException {
		try {
			Class<?> gentyp = getGenericType(setter);
			if (gentyp != null) {
				Class<?> basetyp = getConcreteCollType(setter);
				if (basetyp.equals(List.class)) {
					setRelationsList(target, setter, value, gentyp);
				} else if (basetyp.equals(Set.class)) {
					setRelationsSet(target, setter, value, gentyp);
				} 
			} else {
				throw new Exception("dont know the generic type of " + setter.getName());
			}
		} catch (Exception ex) {
			String msg = "error while setting " + setter.getName() + " : " + ex.getMessage();
			LOGGER.severe(msg);
			throw new XlsxSetValueException(msg, target);
		}
	}

	private void setRelationsList(Object target, Method setter, String value, Class<?> gentyp)
			throws XlsxSetValueException, NoSuchFieldException, IllegalAccessException, InvocationTargetException,
			SecurityException, IntrospectionException {
		List<Object> objects = new ArrayList<Object>();
		for (int nr : getNrs(value)) {
			Object obj = makeObject(gentyp, nr);
			if (obj != null) {
				objects.add(obj);
			}
		}
		setter.invoke(target, new Object[] { objects });
	}

	private void setRelationsSet(Object target, Method setter, String value, Class<?> gentyp)
			throws XlsxSetValueException, NoSuchFieldException, IllegalAccessException, InvocationTargetException,
			SecurityException, IntrospectionException 
	{
		Set<Object> objects = new HashSet<Object>();
		for (int nr : getNrs(value)) {
			Object obj = makeObject(gentyp, nr);
			if (obj != null) {
				objects.add(obj);
			}
		}
		setter.invoke(target, new Object[] { objects });
	}

	private void setArray(Object target, Method setter, String value) throws XlsxSetValueException {
		try {
			Class<?> typ = setter.getParameterTypes()[0].getComponentType();
			String strvalues[] = value.split(DELIM);
			Object objects = Array.newInstance(typ, strvalues.length);
			for (int i=0; i<strvalues.length; i++) {
				Object obj = convertUtils.convert(typ, strvalues[i]);
				Array.set(objects, i, obj);
			}
			setter.invoke(target, new Object[]{objects});
		} catch (Exception ex) {
			String msg = "error while setting " + setter.getName() + " : " + ex.getMessage();
			LOGGER.severe(msg);
			throw new XlsxSetValueException(msg, target);
		}
	}
	
	private Object makeInstance(XlsxData xlsdata) {
		try {
			Class<?> clz = Class.forName(xlsdata.getFqn());
			return clz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	private PropKind getPropertyKind(Class<?> clz, String propName) throws NoSuchFieldException, SecurityException {
		if (clz.isEnum()) {
			return PropKind.ENUM_PROP;
		} else if (isMultiProp(clz)) {
			if (convertUtils.canConvert(clz)) {
				return PropKind.MULTI_PROP;
			} else {
				return PropKind.MULTI_REL;
			}
		} else {
			if (convertUtils.canConvert(clz)) {
				return PropKind.SINGLE_PROP;
			} else {
				return PropKind.SINGLE_REL;
			}
		}
	}

	private boolean isMultiProp(Class<?> clz) {
		return Collection.class.isAssignableFrom(clz) || clz.equals(Set.class) || clz.isArray();
	}
	
	private Class<?> getConcreteCollType(Method setter) throws NoSuchFieldException, ClassNotFoundException {
		Class<?> basetyp = getPropType(setter);
		if (Collection.class.equals(basetyp)) {
			return List.class;
		} else if (List.class.equals(basetyp)) {
			return List.class;
		} else if (Set.class.equals(basetyp)) {
			return Set.class;
		} else {
			String msg = "Dont know how to make concrete class of " + basetyp;
			LOGGER.severe(msg);
			throw new ClassNotFoundException(msg);
		}
	}

	private XlsxData getXlsxData(Class<?> clz, int nr) {
		return getXlsxData(clz.getName(), nr);
	}
	
	private XlsxData getXlsxData(String classname, int nr) {
		XlsxDataHash datahash = xlsDataMap.get(activeSheetNr);
		if (datahash.hasXlsData(classname, nr)) {
			return datahash.getData(classname, nr);
		} else {
			for (byte index=0; index<sheetCount(); index++) {
				if (index != activeSheetNr && datahash.hasXlsData(classname, nr)) {
					return datahash.getData(classname, nr);
				} 	
			}
		}
		return null;
	}
	
	private byte sheetCount() {
		return (byte) xlsDataMap.size();
	}
	
	private enum PropKind {
		SINGLE_PROP, MULTI_PROP, SINGLE_REL, MULTI_REL, ENUM_PROP
	}

}
