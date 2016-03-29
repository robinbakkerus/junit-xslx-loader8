package flca.xlsx.util;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import data.Bar;
import data.Foo;
import data.FooType;
import flca.xlsx.util.XlsxDataWriter;

public class TestXlsxDataWriter {

	private final static String FILENAME1 = "/tmp/test1.xlsx";
	private final static String FILENAME2 = "/tmp/test2.xlsx";
	private final static String FILENAME3 = "/tmp/test3.xlsx";
	private final static String FILENAME4 = "/tmp/test4.xlsx";
	private final static String CONFIG_FILE = "/config.xlsx";

	@Before
	public void beforeEach() {
		File file = new File(FILENAME1);
		if (file.exists()) {
			if (!file.delete()) {
				System.out.println("Could not delete fie " + FILENAME1 + " before running test");
			}
		}	
		file = new File(FILENAME2);
		if (file.exists()) {
			if (!file.delete()) {
				System.out.println("Could not delete fie " + FILENAME1 + " before running test");
			}
		}	
	}
	
	@Test
	public void test1() throws IOException, IntrospectionException {
		XlsxDataWriter writer = new XlsxDataWriter(FILENAME1);
		writer.writeXlsxFile(Foo.class, Bar.class, Date.class, FooType.class);
		File file = new File(FILENAME1);
		Assert.assertTrue(file.exists());
	}
	
	@Test
	public void test2() throws IOException, IntrospectionException {
		XlsxConfig.readFromXlsx(CONFIG_FILE);
		XlsxDataWriter writer = new XlsxDataWriter(FILENAME2);
		writer.writeXlsxFile(Foo.class, Bar.class, Date.class, FooType.class);
		File file = new File(FILENAME2);
		Assert.assertTrue(file.exists());
	}

	@Test
	public void test3() throws IOException, IntrospectionException {
		XlsxDataWriter.writeXlsxFile(FILENAME3, CONFIG_FILE, Foo.class, Bar.class, Date.class, FooType.class);
		File file = new File(FILENAME3);
		Assert.assertTrue(file.exists());
	}

	@Test
	public void test4() throws IOException, IntrospectionException {
		List<XlsxAlias> aliaslist = new ArrayList<XlsxAlias>();
		aliaslist.add(new XlsxAlias("Foo", "veryLongPropertyName", "vlpn"));
		XlsxDataWriter.writeXlsxFile(FILENAME4, aliaslist, Foo.class, Bar.class, Date.class, FooType.class);
		File file = new File(FILENAME4);
		Assert.assertTrue(file.exists());
	}

}
