package flca.xlsx.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Static class that maintains some properties that are being use by the xls utility classes.
 * Currently the following config values are supported:
 * maxRows, default=750. You may want to increase this values if you have huge excel sheets
 * maxCols, default=500. You may want to increase this values if you have huge excel sheets
 * dateFormat used in the excel file to populate a Date (LocalDate or DateTime)
 * convertUtils, the interface that you can with an implementation that extends the ConvertUtils implementation.
 * @author robin.bakkerus
 *
 */
public class XlsxConfig {
	
	private static final List<SimpleDateFormat> DEF_DATEFORMATS = makeDefDateFormats();
	private static final int DEF_MAX_ROWS = 750;
	private static final int DEF_MAX_COLS = 500;
	private static final ConvertUtils DEF_CONVERT_UTILS = new ConvertUtilsImpl();

	private static XlsxDateFmtHelper dateFormatHelper; //will (re)created in reset() or setDateFormats()

	/**
	 * The following three properties are used to convert an excel value to a Date, DateTime, LocalData or Time class
	 * the length of an excel value is used to pick one of the formats below.
	 */
	private static List<SimpleDateFormat> makeDefDateFormats() {
		List<SimpleDateFormat>  r = new ArrayList<>();
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
	
	private static List<SimpleDateFormat> sDateFormats;
	
	/**
	 * This interface gives access to the mappers that are used to convert a String value (inside excel) to corresponding object.
	 * When the out-of-the-box mappers (all primitives, Date, DateTime, BigDecimal etc) are not sufficient you can extends the ConvertUtils class 
	 * and provide your own converters. 
	 */
	public static ConvertUtils sConvertUtils = new ConvertUtilsImpl();

	/**
	 * These two values are used to find data within an excel sheet, you may want to increase these values for huge sheets.
	 */
	public static int maxRows = DEF_MAX_ROWS;
	public static int maxCols = DEF_MAX_COLS;
	

	/**
	 * This reset's all properties to the default settings.
	 */
	public static void reset() {
		sDateFormats = DEF_DATEFORMATS;
		sConvertUtils = DEF_CONVERT_UTILS;
		maxRows = DEF_MAX_ROWS;
		maxCols = DEF_MAX_ROWS;
		setDateFormats(DEF_DATEFORMATS);
	}

	/**
	 * returns the current active list of SimpleDateFormat's
	 * @return  List<SimpleDateFormat>
	 */
	public static List<SimpleDateFormat> getDateFormats() {
		return sDateFormats;
	}
	
	/**
	 * Overrides the current list of SimpleDateFormat's
	 * @param dateFormats List<SimpleDateFormat>
	 */
	public static void setDateFormats(List<SimpleDateFormat> dateFormats) {
		sDateFormats = dateFormats;
		dateFormatHelper = new XlsxDateFmtHelper(dateFormats);
	}

	static SimpleDateFormat findDateFormat(final String valueToConvert) throws XlsxSetValueException {
		return dateFormatHelper.find(valueToConvert);
	}
	
	static SimpleDateFormat defaultDateFormat() {
		return dateFormatHelper.getDefault();
	}

}
