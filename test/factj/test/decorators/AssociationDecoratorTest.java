package factj.test.decorators;

import static factj.FactJHelper.field;
import static factj.FactJHelper.sequence;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import factj.Decorator;
import factj.FactJ;
import factj.FactJHelper;
import factj.decorators.AssociationDecorator;
import factj.test.PersistenceTest;
import factj.test.models.Address;
import factj.test.models.Address2;
import factj.test.models.Person;

@RunWith(JUnit4.class)
public class AssociationDecoratorTest {
	@Before
	public void setUp() throws Exception {
		FactJHelper.fabricate(Address.class,
				sequence("id"),
				field("address", "Street X"));
		FactJHelper.fabricate(Address.class, "secondFactory",
				sequence("id"),
				field("address", "Street Y"));
		FactJHelper.fabricate(Address2.class,
				sequence("id"),
				field("neighborhood", "Test 1"));
		FactJHelper.fabricate(Address2.class, "secondFactory",
				sequence("id"),
				field("neighborhood", "Test 2"));
	}

	@Test
	public void testGivesValueToField() {
		PersistenceTest per = new PersistenceTest();
		FactJ.setPersistence(per);

		Decorator<Object> d = new AssociationDecorator("address");
		Person p = new Person();
		d.decorate(p);
		assertNotNull("AssociationDecorator did not build the object", p.getAddress());
		assertEquals("AssociationDecorator did not build the correct object", "Street X",
				p.getAddress().getAddress());
		assertTrue("It should have called save(Object) on persistence", per.called);
	}

	@Test
	public void testUsesSecondFactory() {
		Decorator<Object> d = new AssociationDecorator("address", "secondFactory");
		Person p = new Person();
		d.decorate(p);
		assertNotNull("AssociationDecorator did not build the object", p.getAddress());
		assertEquals("AssociationDecorator did not build the correct object", "Street Y",
				p.getAddress().getAddress());
	}

	@Test
	public void testUsesAnotherClass() {
		Decorator<Object> d = new AssociationDecorator("address", Address2.class);
		Person p = new Person();
		d.decorate(p);
		assertNotNull("AssociationDecorator did not build the object", p.getAddress());
		assertEquals("AssociationDecorator did not build the correct object", "Test 1",
				((Address2)p.getAddress()).getNeighborhood());
	}

	@Test
	public void testUsesAnotherClassAndAnotherFactoryName() {
		Decorator<Object> d = new AssociationDecorator("address", Address2.class, "secondFactory");
		Person p = new Person();
		d.decorate(p);
		assertNotNull("AssociationDecorator did not build the object", p.getAddress());
		assertEquals("AssociationDecorator did not build the correct object", "Test 2",
				((Address2)p.getAddress()).getNeighborhood());
	}

	@After
	public void tearDown() throws Exception {
		FactJ.clear();
	}
}
