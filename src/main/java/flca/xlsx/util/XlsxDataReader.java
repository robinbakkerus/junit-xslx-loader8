package flca.xlsx.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * todo
 * @author robin
 *
 */
class XlsxDataReader extends AbstractXlsxUtils {

	private static final Logger LOGGER = Logger.getLogger(XlsxDataReader.class.getName());
	
	private Workbook workbook = new XSSFWorkbook();
	private Sheet worksheet = null;
	private int maxRow = XlsxConfig.maxRows;
	private int maxCol = XlsxConfig.maxCols;

	XlsxDataReader() {
	}

	/**
	 * Returns the XlsDataHash from the given excel filename and (0-based) sheetindex.
	 * The filename may be an absolute path or a file on the classpath.
	 * @param excelFilename, String
	 * @param sheetIndex byte
	 * @return XlsDataHash
	 */
	static XlsxDataHash read(final String excelFilename, final byte sheetIndex) {
		XlsxDataReader reader;
		try {
			reader = new XlsxDataReader(excelFilename);
			return new XlsxDataHash(reader.readData(sheetIndex));
		} catch (IOException e) {
			throw new XlsxDataUtilsException(e.getMessage(), e);
		}
	}
	
	static byte sheetCount(final String excelFilename) {
		XlsxDataReader reader;
		try {
			reader = new XlsxDataReader(excelFilename);
			return reader.sheetCount();
		} catch (IOException e) {
			throw new XlsxDataUtilsException(e.getMessage(), e);
		}		
	}
	
	private byte sheetCount() {
		return (byte) workbook.getNumberOfSheets();
	}
	
	private XlsxDataReader(final String excelFilename) throws IOException {
		final InputStream instream = getInputStream(excelFilename);
		if (instream != null) {
			workbook = new XSSFWorkbook(instream);
			instream.close();
		} else {
			throw new XlsxDataUtilsException("Can not read " + excelFilename, null);
		}
	}


	private InputStream getInputStream(String excelFilename) throws FileNotFoundException {
		InputStream result = this.getClass().getResourceAsStream(excelFilename);
		if (result != null) {
			return result;
		} else {
			return new FileInputStream(new File(excelFilename));
		}
	}
	
	private List<XlsxData> readData(final int sheetIndex) {
		List<XlsxData> result = new ArrayList<>();

		worksheet = workbook.getSheet(workbook.getSheetName(sheetIndex));
		List<FqnRowColHash> fqnames = findFqnames();

		for (FqnRowColHash fqnHash : fqnames) {
			final String fqn = fqnHash.fqn;
			XlsxData xlsdata = getData(fqn, fqnHash.rowcol);
			result.add(xlsdata);
		}
		return result;
	}

	private XlsxData getData(final String classname, RowCol pnt) {
		final int[][] rect = getRectangle(pnt);
		if (isValid(rect)) {
			return readExcelValues(rect);
		} else {
			return null;
		}
	}
	
	private int[][] getRectangle(RowCol pnt) {
		final int result[][] = new int[][] { { -1, -1 }, { -1, -1 } };
		int[] ul = new int[] { pnt.row, pnt.col };
		int[] lr = findLowerRight(ul);
		if (lr[0] >= 0 && lr[1] >= 0) {
			result[0][0] = ul[0];
			result[0][1] = ul[1];
			result[1][0] = lr[0];
			result[1][1] = lr[1];
		}
		return result;
	}

	private List<FqnRowColHash> findFqnames() {
		List<FqnRowColHash> result = new ArrayList<>();

		for (int r = 0; r < maxRow; r++) {
			final Row row = worksheet.getRow(r);
			if (row != null) {
				for (int c = 0; c < maxCol; c++) {
					Cell cell = row.getCell(c);
					if (isFqn(cell)) {
						result.add(new FqnRowColHash(stringFrom(cell), new RowCol(r, c)));
					}
				}
			}
		}

		return result;
	}

	private boolean isFqn(Cell cell) {
		if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
			String text = cell.getRichStringCellValue().getString();
			if (text != null && text.contains(".")) {
				try {
					Class.forName(text);
					return true;
				} catch (ClassNotFoundException e) {
					return false;
				}
			}
		}
		return false;
	}

	private int[] findLowerRight(final int[] ul) {
		int[] result = new int[] { -1, -1 };
		int startRow = ul[0] + 1;
		int startCol = ul[1];

		// how many headers?
		Row row = worksheet.getRow(startRow);
		for (int c = startCol; c < startCol + maxCol; c++) {
			String text = stringFrom(row.getCell(c));
			if (text == null) {
				result[1] = c - 1;
				break;
			}
		}

		// how many rows
		for (int r = startRow; r < startRow + maxRow; r++) {
			row = worksheet.getRow(r);
			if (row == null || stringFrom(row.getCell(startCol)) == null) {
				result[0] = r;
				break;
			}
		}

		return result;
	}

	private XlsxData readExcelValues(final int rect[][]) {
		XlsxData result = new XlsxData();

		result.setFqn(getFqclassname(rect, worksheet));
		result.setNames(getNames(rect, worksheet));
		result.setValues(getValues(rect, worksheet));
		result.setUl(new RowCol(rect[0][0], rect[0][1]));
		result.setLr(new RowCol(rect[1][0], rect[1][1]));

		return result;
	}

	private boolean isValid(final int rect[][]) {
		return rect != null && rect[0][0] >= 0;
	}

	/*
	 * the Fqn is located in the cell next to the simplename just above the
	 * values
	 */
	private String getFqclassname(final int rect[][], final Sheet worksheet) {
		Row row = worksheet.getRow(rect[0][0]);
		return stringFrom(row.getCell(rect[0][1]));
	}

	private String[] getNames(final int rect[][], final Sheet worksheet) {
		int startRow = rect[0][0];
		int startCol = rect[0][1] + 1; // skip first column 'nr'
		int endCol = rect[1][1];
		int ncols = endCol - startCol + 1;
		String result[] = new String[ncols];

		int i = 0;
		for (int c = startCol; c <= endCol; c++) {
			Row row = worksheet.getRow(startRow + 1);
			result[i++] = stringFrom(row.getCell(c));
		}

		return result;
	}

	private Map<Integer, String[]> getValues(final int rect[][], final Sheet worksheet) {
		int startRow = rect[0][0] + 2;
		int startCol = rect[0][1];
		int endRow = rect[1][0];
		int endCol = rect[1][1];
		int ncols = endCol - startCol + 1;

		Map<Integer, String[]> result = new HashMap<>();

		Set<Integer> nrs = new HashSet<>();
		for (int r = startRow; r < endRow; r++) {
			int j = 0;
			String values[] = new String[ncols - 1];
			for (int c = startCol; c < startCol + (endCol - startCol + 1); c++) {
				Row row = worksheet.getRow(r);
				if (j == 0) {
					nrs = getNrs(stringFrom(row.getCell(c)));
				} else {
					values[j - 1] = stringFrom(row.getCell(c));
				}
				j++;
			}
			
			for (int nr : nrs) {
				result.put(nr, values);
			}
		}

		return result;
	}

	/**
	 * Get the cell value from the workbook and the specified cell in this workbook.
	 * 
	 * @param cell
	 *            the cell containing the data
	 * @return String representation of the cell data
	 */
	private String stringFrom(final Cell cell) {

		if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			return null;
		} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			return cell.getRichStringCellValue().getString();
		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return getNumericCellValue(cell).toString();
		} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			if (cell.getBooleanCellValue())
				return "true";
			else
				return "false";
		} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
			LOGGER.warning("formula's not supported");
			return null;
		} else {
			return null;
		}

	}


	/**
	 * Get numeric cell value
	 * 
	 * @param cell
	 *            the cell to get the data from
	 * @return the object representation of numeric cell value.
	 */
	private Object getNumericCellValue(final Cell cell) {
		Object cellValue;
		if (DateUtil.isCellDateFormatted(cell)) {
			System.out.println(cell.getCellComment());
			final Date date = new Date(cell.getDateCellValue().getTime());
			return dateformat().format(date);
		} else {
			cellValue = cell.getNumericCellValue();
			// below is the work around to remove suffix .0 from numeric fields
			if (cellValue.toString().endsWith(".0")) {
				cellValue = cellValue.toString().replace(".0", "");
			}
		}
		return cellValue;
	}

	private SimpleDateFormat dateformat() {
		return XlsxConfig.defaultDateFormat();
	}

	//------------- class ----------------
	
	protected class RowCol {
		private int row;
		private int col;

		public RowCol(int row, int col) {
			super();
			this.row = row;
			this.col = col;
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public int getCol() {
			return col;
		}

		public void setCol(int col) {
			this.col = col;
		}
	}
	
	/**
	 * The same cellname may appear more than once in excel, so we can't use a Map hence this class
	 * @author robin
	 *
	 */
	private class FqnRowColHash {
		String fqn;
		RowCol rowcol;
		
		public FqnRowColHash(String fqn, RowCol rowcol) {
			super();
			this.fqn = fqn;
			this.rowcol = rowcol;
		}
	}
	
}
