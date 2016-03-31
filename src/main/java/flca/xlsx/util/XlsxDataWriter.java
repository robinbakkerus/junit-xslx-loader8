package flca.xlsx.util;

import java.beans.IntrospectionException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

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
    private String configFilename = null;
    private List<XlsxAlias> aliasses = new ArrayList<XlsxAlias>();
    private Workbook workbook = null;
    private Sheet worksheet = null;
    private CreationHelper createHelper = null;
    private short rownr = 0;
    private short aliasNr = 0;

    private XlsxAliasHelper aliasHelper = null;

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
    public static void writeXlsxFile(final String excelFilename, final Class<?>... classes) {
        final XlsxDataWriter writer = new XlsxDataWriter(excelFilename);
        writer.writeXlsxFile(classes);
    }

    /**
     * static shortcut to generate a template excel with the given (absoulute)
     * filename, and classes
     * 
     * @param excelFilename
     *            String
     * @param classes
     *            any number of classes
     */
    public static void writeXlsxFile(final String excelFilename, final String configEexcelFilename, final Class<?>... classes) {
        final XlsxDataWriter writer = new XlsxDataWriter(excelFilename, configEexcelFilename);
        writer.writeXlsxFile(classes);
    }

    /**
     * static shortcut to generate a template excel with the given (absoulute)
     * filename, and classes
     * 
     * @param excelFilename
     *            String
     * @param classes
     *            any number of classes
     */
    public static void writeXlsxFile(final String excelFilename, final List<XlsxAlias> aAliasses, final Class<?>... classes) {
        final XlsxDataWriter writer = new XlsxDataWriter(excelFilename, aAliasses);
        writer.writeXlsxFile(classes);
    }

    /**
     * static shortcut to generate a template excel with the given (absoulute)
     * filename, and classes
     * 
     * @param excelFilename
     *            String
     * @param classes
     *            any number of classes
     */
    public static void writeAliasXlsxFile(final String excelFilename, final short startNr, final Class<?>... classes) {
        final XlsxDataWriter writer = new XlsxDataWriter(excelFilename);
        writer.writeAliasXlsxFile(startNr, classes);
    }

    //--------------------------------
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
     * constructor
     * 
     * @param filename
     *            String
     * @param configFilename
     *            String
     */
    public XlsxDataWriter(final String filename, final String configFilename) {
        super();
        this.filename = filename;
        this.configFilename = configFilename;
    }

    /**
     * constructor
     * 
     * @param filename
     *            String
     * @param configFilename
     *            String
     */
    public XlsxDataWriter(final String filename, final List<XlsxAlias> aAliasses) {
        super();
        this.filename = filename;
        this.aliasses = aAliasses;
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
    public void writeXlsxFile(final Class<?>... classes) {
        try {
            if (configFilename != null) {
                XlsxConfig.readFromXlsx(configFilename);
            }
            final FileOutputStream fileOut = new FileOutputStream(filename);
            workbook = new XSSFWorkbook();
            createHelper = workbook.getCreationHelper();
            worksheet = workbook.createSheet(SHEET1);
            for (final Class<?> clz : classes) {
                writeClass(clz);
                rownr = (short) (rownr + 5);
            }
            workbook.write(fileOut);
            fileOut.close();
        } catch (final Exception e) {
            throw new XlsxDataUtilsException(e.getMessage(), e);
        }
    }

    private void writeClass(final Class<?> clz) throws IntrospectionException {
        writeClassname(clz);
        if (clz.isEnum() || convertUtils().canConvert(clz)) {
            writeEnumOrKnownClass(clz);
        } else {
            writePropertyNames(clz);
        }
    }

    private void writeClassname(final Class<?> clz) {
        final Row row = worksheet.createRow(rownr++);
        final Cell cell = row.createCell(0);
        cell.setCellValue(createHelper.createRichTextString(clz.getName()));
        final CellStyle style = workbook.createCellStyle();
        cell.setCellStyle(withBoldStyle(style));
    }

    private void writeEnumOrKnownClass(final Class<?> clz) throws IntrospectionException {
        final Row row = worksheet.createRow(rownr++);
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

    private void writePropertyNames(final Class<?> clz) throws IntrospectionException {
        final Row row = worksheet.createRow(rownr++);
        short col = 0;
        Cell cell = row.createCell(col++);
        cell.setCellValue(createHelper.createRichTextString(HDR_NR));
        CellStyle style = workbook.createCellStyle();
        cell.setCellStyle(withBoldStyle(style));

        for (final String propname : MethodHelper.getAllProperties(clz)) {
            cell = row.createCell(col++);
            final String propnameOrAlias = getAliasHelper().getAlias(clz.getName(), propname);
            cell.setCellValue(createHelper.createRichTextString(propnameOrAlias));
            style = workbook.createCellStyle();
            cell.setCellStyle(withBoldStyle(style));
        }
    }

    private CellStyle withBoldStyle(final CellStyle style) {
        final Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        return style;
    }

    private XlsxConvertUtils convertUtils() {
        return XlsxConfig.getConvertUtils();
    }

    private XlsxAliasHelper getAliasHelper() {
        if (this.aliasHelper == null) {
            if (this.aliasses != null && !this.aliasses.isEmpty()) {
                this.aliasHelper = new XlsxAliasHelper(this.aliasses);
            } else if (this.configFilename != null) {
                XlsxConfig.readFromXlsx(this.configFilename);
                this.aliasHelper = new XlsxAliasHelper(XlsxConfig.getAliases());
            } else {
                this.aliasHelper = new XlsxAliasHelper();
            }
        }
        return this.aliasHelper;
    }
    
    private final static String ALIAS_COMMENT = 
    		"# forName can be '*', the simple classname or the fully qualified classname the fqn should be between ";
    private final static String ALIAS_FQN = "flca.xlsx.util.XlsxAlias";

    
    public void writeAliasXlsxFile(final short startNr, Class<?>... classes) {
        try {
        	aliasNr = startNr;
            final FileOutputStream fileOut = new FileOutputStream(this.filename);
            workbook = new XSSFWorkbook();
            createHelper = workbook.getCreationHelper();
            worksheet = workbook.createSheet(SHEET1);
            writeXlsxAliasHeader();
            for (Class<?> clz : classes) {
            	writeXlsxAliasNames(clz);
            }
            workbook.write(fileOut);
            fileOut.close();
        } catch (final Exception e) {
            throw new XlsxDataUtilsException(e.getMessage(), e);
        }
     }

    private void writeXlsxAliasHeader() throws IntrospectionException {
        Row row = worksheet.createRow(rownr++);
        Cell cell = row.createCell(0);
        cell.setCellValue(createHelper.createRichTextString(ALIAS_COMMENT));
        row = worksheet.createRow(rownr++);
        cell = row.createCell(0);
        cell.setCellValue(createHelper.createRichTextString(ALIAS_FQN));
        row = worksheet.createRow(rownr++);
        cell = row.createCell(0);
        cell.setCellValue(createHelper.createRichTextString("nr"));
        cell = row.createCell(1);
        cell.setCellValue(createHelper.createRichTextString("forName"));
        cell = row.createCell(2);
        cell.setCellValue(createHelper.createRichTextString("property"));
        cell = row.createCell(3);
        cell.setCellValue(createHelper.createRichTextString("alias"));
    }


    private void writeXlsxAliasNames(final Class<?> clz) throws IntrospectionException {
        for (final String propname : MethodHelper.getAllProperties(clz)) {
            final Row row = worksheet.createRow(rownr++);
            short col = 0;
            Cell cell = row.createCell(col++);
            cell.setCellValue(createHelper.createRichTextString("" + aliasNr++));
            cell = row.createCell(col++);
            cell.setCellValue(createHelper.createRichTextString(clz.getSimpleName()));
            cell = row.createCell(col++);
            cell.setCellValue(createHelper.createRichTextString(propname));
            cell = row.createCell(col++);
            cell.setCellValue(createHelper.createRichTextString(propname));
        }
    }


}
