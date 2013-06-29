package factj.test.integration;

import static factj.FactJHelper.association;
import static factj.FactJHelper.fabricate;
import static factj.FactJHelper.field;
import static factj.FactJHelper.sequence;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import factj.Decorator;
import factj.FactJ;
import factj.decorators.SequenceDecorator.Sequence;
import factj.test.PersistenceTest;
import factj.test.models.Address;
import factj.test.models.Person;

@RunWith(JUnit4.class)
public class FactJTest {
	@Before
	public void setUp() throws Exception {
		Sequence emailSequence = new Sequence() {
			@Override
			public Object generate(int count) {
				return "user" + count + "@company.com";
			}
		};

		fabricate(Address.class,
				sequence("id"),
				field("address", "An address"));
		fabricate(Person.class,
				sequence("id"),
				field("name", "A person"),
				sequence("email", emailSequence),
				association("address"));
	}

	@Test
	public void testFactJ() {
		Person p = (Person) FactJ.build(Person.class);
		assertEquals("Built wrong id", 1, p.getId());
		assertEquals("Built wrong name", "A person", p.getName());
		assertEquals("Built wrong email", "user1@company.com", p.getEmail());
		assertEquals("Built wrong address id", 1, p.getAddress().getId());
		assertEquals("Built wrong address address", "An address", p.getAddress().getAddress());

		p = (Person) FactJ.build(Person.class);
		assertEquals("Built wrong id", 2, p.getId());
		assertEquals("Built wrong name", "A person", p.getName());
		assertEquals("Built wrong email", "user2@company.com", p.getEmail());
		assertEquals("Built wrong address id", 2, p.getAddress().getId());
		assertEquals("Built wrong address address", "An address", p.getAddress().getAddress());
	}

	@Test
	public void testPersistence() {
		PersistenceTest p = new PersistenceTest();
		FactJ.setPersistence(p);

		FactJ.build(Address.class);
		assertFalse("It shouldn't have called save(Object) on persistence", p.called);
		FactJ.create(Address.class);
		assertTrue("It should have called save(Object) on persistence", p.called);

		p.called = false;
		FactJ.build(Address.class, "");
		assertFalse("It shouldn't have called save(Object) on persistence", p.called);
		FactJ.create(Address.class, "");
		assertTrue("It should have called save(Object) on persistence", p.called);
	}

	@Test
	public void testCustomizeBuild() {
		PersistenceTest p = new PersistenceTest();
		FactJ.setPersistence(p);
		
		Address a = (Address) FactJ.create(Address.class, new Decorator<Address>() {
			@Override
			public void decorate(Address a) {
				a.setAddress("another");
			}
		});
		assertEquals("It should have customized the object.", "another", a.getAddress());
		assertEquals("It should have saved the customized object.", "another", ((Address)p.
				lastReceived).getAddress());
	}

	@After
	public void tearDown() throws Exception {
		FactJ.clear();
	}
}
