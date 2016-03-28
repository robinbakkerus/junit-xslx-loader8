package flca.xlsx.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Static class that maintains some properties that are being use by the xls
 * utility classes. Currently the following config values are supported:
 * maxRows, default=750. You may want to increase this values if you have huge
 * excel sheets maxCols, default=500. You may want to increase this values if
 * you have huge excel sheets dateFormat used in the excel file to populate a
 * Date (LocalDate or DateTime) convertUtils, the interface that you can with an
 * implementation that extends the ConvertUtils implementation.
 * 
 * @author robin.bakkerus
 *
 */
public class XlsxConfig {

	private static final List<SimpleDateFormat> DEF_DATEFORMATS = makeDefDateFormats();
	private static final int DEF_MAX_ROWS = 750;
	private static final int DEF_MAX_COLS = 500;
	private static final ConvertUtils DEF_SPECIAL_CONVERT_UTILS = new EmptyConvertUtils();

	private static final XlsxConvertUtils sConvertUtils = new XlsxConvertUtils(); 
	private static List<SimpleDateFormat> sDateFormats;
	private static XlsxAliasHelper sAliasHelper = new XlsxAliasHelper();
	private static XlsxDateFmtHelper dateFormatHelper; // will (re)created in
														// reset() or
														// setDateFormats()

	/**
	 * The following three properties are used to convert an excel value to a
	 * Date, DateTime, LocalData or Time class the length of an excel value is
	 * used to pick one of the formats below.
	 */
	private static List<SimpleDateFormat> makeDefDateFormats() {
		List<SimpleDateFormat> r = new ArrayList<SimpleDateFormat>();
		r.add(new SimpleDateFormat("dd-MM-yyyy"));
		r.add(new SimpleDateFormat("d-MM-yyyy"));
		r.add(new SimpleDateFormat("dd-M-yyyy"));
		r.add(new SimpleDateFormat("d-M-yyyy"));
		r.add(new SimpleDateFormat("HH:mm"));
		r.add(new SimpleDateFormat("HH:mm:ss"));
		r.add(new SimpleDateFormat("dd-MM-yyyy HH:mm"));
		r.add(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
		return r;
	}

	static {
		reset();
	}
	
	/**
	 * Returns the current active XlsxConvertUtils
	 * @return XlsxConvertUtils
	 */
	public static XlsxConvertUtils getConvertUtils() {
		return sConvertUtils;
	}

	/**
	 * Return the special convert utils that implements ConvertUtils
	 * @return ConvertUtils
	 */
	public static ConvertUtils getSpecialConvertUtils() {
		return sConvertUtils.getSpecialConvertUtils();
	}
	
	/**
	 * This interface gives access to the mappers that are used to convert a
	 * String value (inside excel) to corresponding object. When the
	 * out-of-the-box mappers (all primitives, Date, DateTime, BigDecimal etc)
	 * are not sufficient you can extends the ConvertUtils class and provide
	 * your own converters.
	 */
	public static void setSpecialConvertUtils(final ConvertUtils specialConvertUtils) {
		sConvertUtils.setSpecialConvertUtils(specialConvertUtils);
	}

	/**
	 * These two values are used to find data within an excel sheet, you may
	 * want to increase these values for huge sheets.
	 */
	public static int maxRows = DEF_MAX_ROWS;
	public static int maxCols = DEF_MAX_COLS;

	/**
	 * This fills all the XlsxConfig values from the given excel file. The
	 * filepath can be an absolute path or a file on the classpath.
	 * Alternatively new Xlsx.readXlsxFile("/example.xlsx", "/config.xlsx") can
	 * be used.
	 * 
	 * @param filename String
	 */
	public static void readFromXlsx(final String filename) {
		Xlsx xlsx = new Xlsx(filename);
		fillConfigValuesFromExcel(xlsx);
		fillDateFormatsFromExcel(xlsx);
		fillAliasesFromExcel(xlsx);
	}

	private static void fillConfigValuesFromExcel(final Xlsx xlsx) {
		XlsxConfigValues configvalues = null;
		try {
			configvalues = (XlsxConfigValues) xlsx.make(XlsxConfigValues.class, (byte) 0, 1);
			XlsxConfig.maxCols = configvalues.getMaxColumns();
			XlsxConfig.maxRows = configvalues.getMaxRows();
			Class<?> clz = Class.forName(configvalues.getFqnSpecialConvertUtils());
			ConvertUtils specialConvertUtils = (ConvertUtils) clz.newInstance();
			sConvertUtils.setSpecialConvertUtils(specialConvertUtils);
		} catch (ClassNotFoundException e) {
			String msg = configvalues == null ? "Could nog create XlsxConfigValues"
					: "Could not instantiate " + configvalues.getFqnSpecialConvertUtils();
			throw new XlsxDataUtilsException(msg, e);
		} catch (InstantiationException e) {
			String msg = configvalues == null ? "Could nog create XlsxConfigValues"
					: "Could not instantiate " + configvalues.getFqnSpecialConvertUtils();
			throw new XlsxDataUtilsException(msg, e);
		} catch (IllegalAccessException e) {
			String msg = configvalues == null ? "Could nog create XlsxConfigValues"
					: "Could not instantiate " + configvalues.getFqnSpecialConvertUtils();
			throw new XlsxDataUtilsException(msg, e);
		}
	}

	private static void fillDateFormatsFromExcel(final Xlsx xlsx) {
		List<SimpleDateFormat> sdflist = new ArrayList<SimpleDateFormat>();
		Set<Integer> nrs = xlsx.getAllNrs(SimpleDateFormat.class, (byte) 0);
		for (int nr : nrs) {
			SimpleDateFormat sdf = (SimpleDateFormat) xlsx.make(SimpleDateFormat.class, (byte) 0, nr);
			sdflist.add(sdf);
		}
		XlsxConfig.setDateFormats(sdflist);
	}

	private static void fillAliasesFromExcel(final Xlsx xlsx) {
		List<XlsxAlias> aliaslist = new ArrayList<XlsxAlias>();
		Set<Integer> nrs = xlsx.getAllNrs(XlsxAlias.class, (byte) 0);
		for (int nr : nrs) {
			XlsxAlias alias  = (XlsxAlias) xlsx.make(XlsxAlias.class, (byte) 0, nr);
			aliaslist.add(alias);
		}
		XlsxConfig.setAliases(aliaslist);
	}


	/**
	 * This reset's all properties to the default settings.
	 */
	public static void reset() {
		sDateFormats = DEF_DATEFORMATS;
		sConvertUtils.setSpecialConvertUtils(DEF_SPECIAL_CONVERT_UTILS);
		maxRows = DEF_MAX_ROWS;
		maxCols = DEF_MAX_ROWS;
		setDateFormats(DEF_DATEFORMATS);
		sAliasHelper = new XlsxAliasHelper(new ArrayList<XlsxAlias>());
	}

	/**
	 * returns the current active list of SimpleDateFormat's
	 * 
	 * @return List<SimpleDateFormat>
	 */
	public static List<SimpleDateFormat> getDateFormats() {
		return sDateFormats;
	}

	/**
	 * Overrides the current list of SimpleDateFormat's
	 * 
	 * @param dateFormats
	 *            List<SimpleDateFormat>
	 */
	public static void setDateFormats(List<SimpleDateFormat> dateFormats) {
		sDateFormats = dateFormats;
		dateFormatHelper = new XlsxDateFmtHelper(dateFormats);
	}

	/**
	 * returns the current active list of XlsxAlias's
	 * 
	 * @return List<SimpleDateFormat>
	 */
	public static List<XlsxAlias> getAliases() {
		return sAliasHelper.getAliases();
	}

	/**
	 * Overrides the current list of XlsxAlias's
	 * 
	 * @param dateFormats
	 *            List<SimpleDateFormat>
	 */
	public static void setAliases(List<XlsxAlias> aliases) {
		sAliasHelper = new XlsxAliasHelper(aliases);
	}

	/**
	 * Returns the actual given the input name. This may an alias.
	 * 
	 * @param name
	 *            String
	 * @return String actual property name.
	 */
	public static String getPropertyName(final String classname, final String propnameOrAlias) {
		return sAliasHelper.getPropertyName(classname, propnameOrAlias);
	}

	static SimpleDateFormat findDateFormat(final String valueToConvert) throws XlsxSetValueException {
		return dateFormatHelper.find(valueToConvert);
	}

	static SimpleDateFormat defaultDateFormat() {
		return dateFormatHelper.getDefault();
	}

}
