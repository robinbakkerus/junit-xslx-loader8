package flca.xlsx.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XlsxAliasHelper {

	private List<XlsxAlias> aliases = new ArrayList<XlsxAlias>();

	/*
	 * this is map of classname to a map of alias to propertyname
	 */
	private Map<String, Map<String, String>> aliasMap = new HashMap<String, Map<String,String>>();
	

	/**
	 * default constructor
	 */
	public XlsxAliasHelper() {
		super();
	}

	/**
	 * 
	 * @param sAliases
	 */
	public XlsxAliasHelper(List<XlsxAlias> aAliases) {
		super();
		this.aliases = aAliases;
		setupAliasMap();
	}
	
	
	/**
	 * Returns the actual given the input name. This may an alias.
	 * @param name String
	 * @return String actual property name.
	 */
	public String getPropertyName(final String classname, final String propnameOrAlias) {
		for (String findClassname : findClsNames(classname)) {
			String r = findPropertyName(findClassname, propnameOrAlias);
			if (r != null) {
				return r;
			}
		}
		return propnameOrAlias;
	}

	/**
	 * Returns the actual given the input name. This may an alias.
	 * @param name String
	 * @return String actual property name.
	 */
	public String getAlias(final String classname, final String property) {
		for (String findClassname : findClsNames(classname)) {
			String r = findAlias(findClassname, property);
			if (r != null) {
				return r;
			}
		}
		return property;
	}

	private String findPropertyName(final String clsname, final String property) {
		if (aliasMap.containsKey(clsname)) {
			if (aliasMap.get(clsname).containsKey(property)) {
				return aliasMap.get(clsname).get(property);
			}
		}
		return null;
	}

	private String findAlias(final String clsname, final String property) {
		if (aliasMap.containsKey(clsname)) {
			Map<String, String> propmap = aliasMap.get(clsname);
			if (propmap.containsValue(property)) {
				for (String key : propmap.keySet()) {
					if (propmap.get(key).equals(property)) {
						return key;
					}
				}
			}
		}
		return null;
	}

	private List<String> findClsNames(String classname) {
		List<String> r = new ArrayList<String>();
		r.add(classname);
		if (classname.indexOf(".") > 0) {
			r.add(classname.substring(classname.lastIndexOf(".")+1));
		}
		r.add(XlsxAlias.APPLIES_TO_ALL);
		return r;
	}
	
	private void setupAliasMap() {
		for (XlsxAlias xlsxAlias : aliases) {
			String clsname = xlsxAlias.getForName();
			if (!aliasMap.containsKey(clsname)) {
				aliasMap.put(clsname, propertyMap(xlsxAlias));
			} else {
				Map<String, String>  propMap = aliasMap.get(clsname);
				propMap.put(xlsxAlias.getAlias(), xlsxAlias.getProperty());
			}
		}
	}

	private Map<String, String> propertyMap(XlsxAlias xlsxAlias) {
		Map<String, String> r = new HashMap<String, String>();
		r.put(xlsxAlias.getAlias(), xlsxAlias.getProperty());
		return r;
	}

	public List<XlsxAlias> getAliases() {
		return aliases;
	}

	
}
