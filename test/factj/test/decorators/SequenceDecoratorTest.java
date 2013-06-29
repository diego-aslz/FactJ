package factj.test.decorators;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import factj.Decorator;
import factj.decorators.SequenceDecorator;
import factj.decorators.SequenceDecorator.Sequence;
import factj.test.models.Person;

@RunWith(JUnit4.class)
public class SequenceDecoratorTest {
	@Test
	public void testGivesValueOfSequenceWhenProvided() {
		Decorator<Object> d = new SequenceDecorator("name", new Sequence() {
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

	@Test
	public void testGivesSequenceValueWhenNoSequenceIsProvided() {
		Decorator<Object> d = new SequenceDecorator("id");
		Person p = new Person();
		d.decorate(p);
		assertEquals("SequenceDecorator did not give the correct value", 1, p.getId());
		d.decorate(p);
		assertEquals("SequenceDecorator did not give the correct value", 2, p.getId());
	}
}
