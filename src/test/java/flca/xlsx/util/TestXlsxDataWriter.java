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

	private final static String FILENAME = "/tmp/test.xlsx";

	@Before
	public void beforeEach() {
		File file = new File(FILENAME);
		if (file.exists()) {
			if (!file.delete()) {
				System.out.println("Could not delete fie " + FILENAME + " before running test");
			}
		}	
	}
	
	@Test
	public void test1() throws IOException, IntrospectionException {
		XlsxDataWriter writer = new XlsxDataWriter(FILENAME);
		writer.writeXlsxFile(Foo.class, Bar.class, Date.class, FooType.class);
		File file = new File(FILENAME);
		Assert.assertTrue(file.exists());
	}
	
}
