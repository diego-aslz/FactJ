package factj.test.decorators;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import factj.Decorator;
import factj.decorators.FieldDecorator;
import factj.test.models.Person;

@RunWith(JUnit4.class)
public class FieldDecoratorTest {
	@Test
	public void testGivesValueToField() {
		Decorator d = new FieldDecorator("name", "This is a test!");
		Person p = new Person();
		d.decorate(p);
		assertEquals("FieldDecorator did not give the correct value", "This is a test!",
				p.getName());
	}
}
