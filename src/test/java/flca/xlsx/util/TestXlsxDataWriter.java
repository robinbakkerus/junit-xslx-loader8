package flca.xlsx.util;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.util.Date;

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
}
