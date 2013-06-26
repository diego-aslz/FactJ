package factj.test.decorators;

import junit.framework.TestCase;
import factj.Decorator;
import factj.decorators.FieldDecorator;
import factj.test.models.Person;

public class FieldDecoratorTest extends TestCase {
	public void testGivesValueToField() {
		Decorator d = new FieldDecorator("name", "This is a test!");
		Person p = new Person();
		d.decorate(p);
		assertEquals("FieldDecorator did not give the correct value", "This is a test!",
				p.getName());
	}
}
