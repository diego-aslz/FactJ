package factj.test.decorators;

import static factj.FactJHelper.field;
import static factj.FactJHelper.sequence;
import junit.framework.TestCase;
import factj.FactJHelper;
import factj.Decorator;
import factj.FactJ;
import factj.decorators.AssociationDecorator;
import factj.test.PersistenceTest;
import factj.test.models.Address;
import factj.test.models.Address2;
import factj.test.models.Person;

public class AssociationDecoratorTest extends TestCase {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
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

	public void testGivesValueToField() {
		PersistenceTest per = new PersistenceTest();
		FactJ.setPersistence(per);

		Decorator d = new AssociationDecorator("address");
		Person p = new Person();
		d.decorate(p);
		assertNotNull("AssociationDecorator did not build the object", p.getAddress());
		assertEquals("AssociationDecorator did not build the correct object", "Street X",
				p.getAddress().getAddress());
		assertTrue("It should have called save(Object) on persistence", per.called);
	}

	public void testUsesSecondFactory() {
		Decorator d = new AssociationDecorator("address", "secondFactory");
		Person p = new Person();
		d.decorate(p);
		assertNotNull("AssociationDecorator did not build the object", p.getAddress());
		assertEquals("AssociationDecorator did not build the correct object", "Street Y",
				p.getAddress().getAddress());
	}

	public void testUsesAnotherClass() {
		Decorator d = new AssociationDecorator("address", Address2.class);
		Person p = new Person();
		d.decorate(p);
		assertNotNull("AssociationDecorator did not build the object", p.getAddress());
		assertEquals("AssociationDecorator did not build the correct object", "Test 1",
				((Address2)p.getAddress()).getNeighborhood());
	}

	public void testUsesAnotherClassAndAnotherFactoryName() {
		Decorator d = new AssociationDecorator("address", Address2.class, "secondFactory");
		Person p = new Person();
		d.decorate(p);
		assertNotNull("AssociationDecorator did not build the object", p.getAddress());
		assertEquals("AssociationDecorator did not build the correct object", "Test 2",
				((Address2)p.getAddress()).getNeighborhood());
	}

	@Override
	protected void tearDown() throws Exception {
		FactJ.clear();
		super.tearDown();
	}
}
