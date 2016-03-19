package flca.xlsx.util;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import flca.xlsx.util.XlsxDataReader.RowCol;

public class XlsxData {
	private String fqn; // fully qualfied classname
	private RowCol ul; // the location of the actual values
	private RowCol lr;
	private String names[]; // the header that correspond with properties of
							// class
	private Map<Integer, String[]> valuesMap;

	/**
	 * Returns an array of String that belong to row with 'nr' equals the given nr. The first instance (r[0] corresponds with the first property.
	 * @param nr, int 
	 * @return String[]
	 * @throws NoSuchElementException
	 */
	public String[] getValues(final int nr) throws NoSuchElementException {
		if (valuesMap.containsKey(nr)) {
			return valuesMap.get(nr);
		} else {
			throw new NoSuchElementException("Row with key " + nr + " does not exist ");
		}
	}
	
	/**
	 * Return the String value that belows to row with 'nr' equals nr and the given column. 
	 * Note column is the position within this object inside the Excel file, so if this object is moved inside the excel sheet to the right, the column differs from the excel column,
	 * column = 0 corresponds with first property (that this the column next to column with the header 'nr'). 
	 * @param nr, int
	 * @param column, int
	 * @return String
	 * @throws NoSuchElementException
	 */
	public String getValue(final int nr, final int column) throws NoSuchElementException {
		final String strvals[] = getValues(nr);
		if (strvals != null && strvals.length >= column+1) {
			return strvals[column];
		} else {
			int n = strvals==null ? 0 : strvals.length;
			String msg = "Row with key " + nr + " only contains " + n + " columns, and not " + column;
			throw new NoSuchElementException(msg);
		}
	}
	
	/**
	 * This is similar to getValue(int nr, int column), but instead of providing the column index, the corresponding property name (that is displayed in the header) is given.
	 * @param nr, int
	 * @param columnName, String
	 * @return String
	 * @throws NoSuchElementException
	 */
	public String getValue(final int nr, final String columnName) throws NoSuchElementException {
		return getValue(nr, getColumnIndex(columnName));
	}

	/**
	 * Returns the column index that corresponds with the given property name.
	 * @param columnName, String
	 * @return String
	 * @throws NoSuchElementException
	 */
	public int getColumnIndex(final String columnName) throws NoSuchElementException {
		for (int i=0; i < names.length; i++) {
			if (columnName.equals(names[i])) {
				return i;
			}
		}
		throw new NoSuchElementException("Columnname " + columnName + " does not exist");
	}
	
	public Set<Integer> getNrs() {
		return valuesMap.keySet();
	}
	
	public String getFqn() {
		return fqn;
	}

	public String[] getNames() {
		return names;
	}

	public Map<Integer, String[]> getValues() {
		return valuesMap;
	}

	public RowCol getUl() {
		return ul;
	}

	public RowCol getLr() {
		return lr;
	}

	void setFqn(String fqn) {
		this.fqn = fqn;
	}

	void setNames(String[] names) {
		this.names = names;
	}

	void setValues(Map<Integer, String[]> values) {
		this.valuesMap = values;
	}

	void setUl(RowCol ul) {
		this.ul = ul;
	}

	void setLr(RowCol lr) {
		this.lr = lr;
	}

	@Override
	public String toString() {
		return "\nXlsData [fqn=" + fqn + ", names=[" + Arrays.toString(names)
				+ "], values=" + valuesToString();
	}

	private String valuesToString() {
		 StringBuffer sb = new StringBuffer();
		 for (Integer nr : valuesMap.keySet()) {
			 sb.append("\n" + nr + "=");
			 sb.append(Arrays.toString(valuesMap.get(nr)));
		 }
		 return sb.toString();
	}
}
