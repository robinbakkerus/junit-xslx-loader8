package flca.xlsx.util;

import java.beans.IntrospectionException;
import java.lang.reflect.Method;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import data.Bar;
import data.Foo;
import flca.xlsx.util.MethodHelper;

public class TestMethodHelper {

	@Test
	public void testGetGetter() throws IntrospectionException {
		Foo foo = new Foo();
		Method m = MethodHelper.getGetter(foo, "naam");
		Assert.assertNotNull(m);
	}


	@Test
	public void testGetSetter() throws IntrospectionException {
		Foo foo = new Foo();
		Method setter = MethodHelper.getSetter(foo, "naam");
		Assert.assertNotNull(setter);
		Assert.assertTrue(setter.getParameterTypes().length==1);
	}
	
	@Test
	public void testGetSetter2() throws IntrospectionException {
		Bar bar = new Bar();
		Method setter = MethodHelper.getSetter(bar, "p2");
		Assert.assertNotNull(setter);
		Assert.assertTrue(setter.getParameterTypes().length==1);
	}
	
	@Test
	public void testGetAllProperties() throws IntrospectionException {
		Foo foo = new Foo();
		Set<String> r = MethodHelper.getAllProperties(foo);
		Assert.assertTrue(r.size()==15);
	}
	
}
