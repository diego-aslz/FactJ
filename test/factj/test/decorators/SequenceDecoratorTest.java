package factj.test.decorators;

import junit.framework.TestCase;
import factj.Decorator;
import factj.decorators.SequenceDecorator;
import factj.decorators.SequenceDecorator.Sequence;
import factj.test.models.Person;

public class SequenceDecoratorTest extends TestCase {
	public void testGivesValueOfSequenceWhenProvided() {
		Decorator d = new SequenceDecorator("name", new Sequence() {
			@Override
			public Object generate(int count) {
				return "person " + count;
			}
		});
		Person p = new Person();
		d.decorate(p);
		assertEquals("SequenceDecorator did not give the correct value", "person 1", p.getName());
		d.decorate(p);
		assertEquals("SequenceDecorator did not give the correct value", "person 2", p.getName());
	}

	public void testGivesSequenceValueWhenNoSequenceIsProvided() {
		Decorator d = new SequenceDecorator("id");
		Person p = new Person();
		d.decorate(p);
		assertEquals("SequenceDecorator did not give the correct value", 1, p.getId());
		d.decorate(p);
		assertEquals("SequenceDecorator did not give the correct value", 2, p.getId());
	}
}
