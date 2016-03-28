package flca.xlsx.util;

import java.util.ArrayList;
import java.util.List;

public class XlsxDataHash {

	private List<XlsxData> xlsDataHash;

	XlsxDataHash(List<XlsxData> xlsmap) {
		super();
		this.xlsDataHash = xlsmap;
	}

	/**
	 * Return a list of XlsData's that belong to the given class
	 * 
	 * @param clz
	 *            Class
	 * @return List<XlsData>
	 */
	public List<XlsxData> getData(final Class<?> clz) {
		return getData(clz.getName());
	}

	/**
	 * Return a list of XlsData's that belong to the given classname @see
	 * getData(Class<?> clz)
	 * 
	 * @param classname
	 *            String
	 * @return List<XlsData>
	 */
	public List<XlsxData> getData(final String classname) {
		List<XlsxData> r = new ArrayList<XlsxData>();
		for (XlsxData xlsdata : xlsDataHash) {
			if (xlsdata.getFqn().equals(classname)) {
				r.add(xlsdata);
			}
		}
		return r;
	}

	/**
	 * Return the XlsData from the given class and key number
	 * 
	 * @param clz
	 *            Class
	 * @param nr
	 *            int
	 * @return XlsData
	 */
	public XlsxData getData(final Class<?> clz, final int nr) {
		return getData(clz.getName(), nr);
	}

	/**
	 * Return the XlsData from the given classname and key number
	 * 
	 * @param classname
	 *            String
	 * @param nr
	 *            int
	 * @return XlsData
	 */
	public XlsxData getData(final String classname, final int nr) {
		for (XlsxData xls : getData(classname)) {
			if (xls.getNrs().contains(nr)) {
				return xls;
			}
		}
		throw new XlsxDataUtilsException("Can not find " + classname + " with nr " + nr, null);
	}

	/**
	 * Returns true if the XlsData with the given Class and nr are available.
	 * 
	 * @param clz Class<?>
	 * @param nr int
	 * @return boolean
	 */
	public boolean hasXlsData(final Class<?> clz, final int nr) {
		return hasXlsData(clz.getName(), nr);
	}

	/**
	 * Returns true if the XlsData with the given Class and nr are available.
	 * 
	 * @param classname classname
	 * @param nr int
	 * @return boolean
	 */
	public boolean hasXlsData(final String classname, final int nr) {
		for (XlsxData xls : getData(classname)) {
			if (xls.getNrs().contains(nr)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		for (XlsxData xls : xlsDataHash) {
			sb.append(xls + "\n");
		}
		return sb.toString();
	}

}
