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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
		result = prime * result + ((forName == null) ? 0 : forName.hashCode());
		result = prime * result + ((property == null) ? 0 : property.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		XlsxAlias other = (XlsxAlias) obj;
		if (alias == null) {
			if (other.alias != null)
				return false;
		} else if (!alias.equals(other.alias))
			return false;
		if (forName == null) {
			if (other.forName != null)
				return false;
		} else if (!forName.equals(other.forName))
			return false;
		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "XlsxAlias [forName=" + forName + ", property=" + property + ", alias=" + alias + "]";
	}
	
	
}
