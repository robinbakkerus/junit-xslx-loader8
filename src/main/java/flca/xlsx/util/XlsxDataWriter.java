package flca.xlsx.util;

import java.beans.IntrospectionException;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsxDataWriter {

	// private final static Logger LOGGER =
	// Logger.getLogger(XlsxDataWriter.class.getName());

	private String filename = null;
	private Workbook workbook = null;
	private Sheet worksheet = null;
	private CreationHelper createHelper = null;
	private short rownr = 0;

	private static final String SHEET1 = "sheet1";
	private static final String HDR_NR = "nr";
	private static final String HDR_VALUE = "value";

	/**
	 * static shortcut to generate a template excel with the given (absoulute)
	 * filename, and classes
	 * 
	 * @param excelFilename
	 *            String
	 * @param classes
	 *            any number of classes
	 */
	public static void writeXlsxFile(final String excelFilename, Class<?>... classes) {
		XlsxDataWriter writer = new XlsxDataWriter(excelFilename);
		writer.writeXlsxFile(classes);
	}

	/**
	 * constructor
	 * 
	 * @param excelFilename
	 *            String
	 */
	public XlsxDataWriter(final String excelFilename) {
		this.filename = excelFilename;
	}

	/**
	 * This generates a template excel with the given (absoulute) filename, and
	 * classes
	 * 
	 * @param excelFilename
	 *            String
	 * @param classes
	 *            any number of classes
	 */
	public void writeXlsxFile(Class<?>... classes) {
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook = new XSSFWorkbook();
			createHelper = workbook.getCreationHelper();
			worksheet = workbook.createSheet(SHEET1);
			for (Class<?> clz : classes) {
				writeClass(clz);
				rownr = (short) (rownr + 5);
			}
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			throw new XlsxDataUtilsException(e.getMessage(), e);
		}
	}

	private void writeClass(Class<?> clz) throws IntrospectionException {
		writeClassname(clz);
		if (clz.isEnum() || convertUtils().canConvert(clz)) {
			writeEnumOrKnownClass(clz);
		} else {
			writePropertyNames(clz);
		}
	}

	private void writeClassname(Class<?> clz) {
		Row row = worksheet.createRow(rownr++);
		Cell cell = row.createCell(0);
		cell.setCellValue(createHelper.createRichTextString(clz.getName()));
		CellStyle style = workbook.createCellStyle();
		cell.setCellStyle(withBoldStyle(style));
	}

	private void writeEnumOrKnownClass(Class<?> clz) throws IntrospectionException {
		Row row = worksheet.createRow(rownr++);
		short col = 0;
		Cell cell = row.createCell(col++);
		cell.setCellValue(createHelper.createRichTextString(HDR_NR));
		CellStyle style = workbook.createCellStyle();
		cell.setCellStyle(withBoldStyle(style));

		cell = row.createCell(col);
		cell.setCellValue(createHelper.createRichTextString(HDR_VALUE));
		style = workbook.createCellStyle();
		cell.setCellStyle(withBoldStyle(style));
	}

	private void writePropertyNames(Class<?> clz) throws IntrospectionException {
		Row row = worksheet.createRow(rownr++);
		short col = 0;
		Cell cell = row.createCell(col++);
		cell.setCellValue(createHelper.createRichTextString(HDR_NR));
		CellStyle style = workbook.createCellStyle();
		cell.setCellStyle(withBoldStyle(style));

		for (String propname : MethodHelper.getAllProperties(clz)) {
			cell = row.createCell(col++);
			cell.setCellValue(createHelper.createRichTextString(propname));
			style = workbook.createCellStyle();
			cell.setCellStyle(withBoldStyle(style));
		}
	}

	private CellStyle withBoldStyle(CellStyle style) {
		Font font = workbook.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		return style;
	}

	private XlsxConvertUtils convertUtils() {
		return XlsxConfig.getConvertUtils();
	}
}
