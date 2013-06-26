package factj.test.integration;

import static factj.FactJHelper.association;
import static factj.FactJHelper.fabricate;
import static factj.FactJHelper.field;
import static factj.FactJHelper.sequence;
import junit.framework.TestCase;
import factj.FactJ;
import factj.decorators.SequenceDecorator.Sequence;
import factj.test.PersistenceTest;
import factj.test.models.Address;
import factj.test.models.Person;

public class FactJTest extends TestCase {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
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

	@Override
	protected void tearDown() throws Exception {
		FactJ.clear();
		super.tearDown();
	}
}
