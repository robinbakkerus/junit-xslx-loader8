package flca.xlsx.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Xlsx {

	private String excelFilename;
	private Map<Byte,XlsxDataHash> xlsDataHashMap = new HashMap<Byte, XlsxDataHash>();


	/**
	 * constructor this will initialize the XlsDataMap
	 * @param excelFilename String
	 */
	public Xlsx(final String excelFilename) {
		this.excelFilename = excelFilename;
	}
	
	public Xlsx(final String excelFilename, final String configFilename) {
		this.excelFilename = excelFilename;
		XlsxConfig.readFromXlsx(configFilename);
	}

	/**
	 * return the XlsDataMap
	 * @param aClass Class<?> 
	 * @return List<XlsData>
	 */
	public List<XlsxData> getData(final Class<?> aClass, final byte sheetIndex) {
		return readData(sheetIndex).getData(aClass.getName());
	}

	/**
	 * Creates an instance of the Class-type object, with the values belonging to the given nr. 
	 * @param cls Class<?>
	 * @param nr int
	 * @return Object
	 */
	public Object make(final Class<?> cls, final byte sheetIndex, int nr) {
		try {
			XlsxConvertUtils convertUtils = XlsxConfig.getConvertUtils();
			ReflectionHelper reflhelper = new ReflectionHelper(readData(sheetIndex), convertUtils);
			return reflhelper.makeObject(cls, nr);
		} catch (Exception e) {
			throw new XlsxDataUtilsException(e.getMessage(), e);
		}
	}

	/**
	 * Returns the total number of worksheet in the excel file.
	 * @return byte
	 */
	public byte sheetCount() {
		return XlsxDataReader.sheetCount(this.excelFilename);
	}
	
	/**
	 * returns all the nr's belong to the given class 
	 * @param clz Class<?> 
	 * @return Set<Integer> 
	 */
	public Set<Integer> getAllNrs(final Class<?> clz, final byte sheetIndex) {
		Set<Integer> r = new HashSet<Integer>();
		for (XlsxData xlsdata : getData(clz, sheetIndex)) {
			r.addAll(xlsdata.getNrs());
		}
		return r;
	}
	
	/**
	 * Returns the XlsDataHash that corresponds with the given sheet
	 * @param sheetIndex byte
	 * @return XlsDataHash
	 */
	public XlsxDataHash getDataHash(final byte sheetIndex) {
		return readData(sheetIndex);
	}
	
	private XlsxDataHash readData(byte sheetIndex) {
		if (!xlsDataHashMap.containsKey(sheetIndex)) {
			xlsDataHashMap.put(sheetIndex, XlsxDataReader.read(excelFilename, sheetIndex));
		}
		return xlsDataHashMap.get(sheetIndex);
	}
	
}
