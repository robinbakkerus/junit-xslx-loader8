package flca.xlsx.util;

public class XlsxAlias {

	public static final String APPLIES_TO_ALL = "*";
	
	/**
	 * this indicates to which class this alias applies. Possible values '*' Simple classname or fully qualified classname
	 */
	private String forName;
	private String property;
	private String alias;
	
	
	public XlsxAlias() {
		super();
	}
	
	
	public XlsxAlias(String appliesTo, String property, String alias) {
		super();
		this.forName = appliesTo;
		this.property = property;
		this.alias = alias;
	}


	public String getForName() {
		return forName;
	}
	public void setForName(String appliesTo) {
		this.forName = appliesTo;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	
}
