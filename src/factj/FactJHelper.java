package factj;

import factj.decorators.AssociationDecorator;
import factj.decorators.FieldDecorator;
import factj.decorators.SequenceDecorator;
import factj.decorators.SequenceDecorator.Sequence;

/**
 * This contains helper methods that make the creation of factories easier. Also, using this
 * methods make the factories more readable.
 * @author Diego Aguir Selzlein
 *
 */
public abstract class FactJHelper {
	/**
	 * Generates and registers a {@link Factory} for the given <strong>clazz</strong>
	 * with the given <strong>decorators</strong> that customize the objects when they get built.
	 * @param clazz Class that the factory will concern about.
	 * @param decorators Array of {@link Decorator} that customize the object when it gets built.
	 */
	public static void fabricate(Class<?> clazz, Decorator<?> ... decorators) {
		FactJ.registerFactory(new Factory(clazz, decorators));
	}

	/**
	 * Generates and registers a {@link Factory} for the given <strong>clazz</strong>
	 * with the given <strong>name</strong> and <strong>decorators</strong> that customize
	 * the objects when they get built.
	 * @param clazz Class that the factory will concern about.
	 * @param decorators Array of {@link Decorator} that customize the objects when they get built.
	 */
	public static void fabricate(Class<?> clazz, String name, Decorator<?> ... decorators) {
		FactJ.registerFactory(new Factory(clazz, name, decorators));
	}

	/**
	 * Helper method that generates a {@link Decorator} of the type {@link FieldDecorator}.
	 * @param fieldName The name of the field that needs to be changed.
	 * @param value The value the field will receive.
	 * @return A {@link FieldDecorator} implementation.
	 */
	public static Decorator<Object> field(String fieldName, Object value) {
		return new FieldDecorator(fieldName, value);
	}

	/**
	 * Helper method that generates a {@link Decorator} of the type {@link SequenceDecorator}.
	 * @param fieldName The name of the field that needs to be changed.
	 * @param sequence The {@link Sequence} that needs to get called to customize the field.
	 * @return A {@link SequenceDecorator} implementation.
	 */
	public static Decorator<Object> sequence(String fieldName, Sequence sequence) {
		return new SequenceDecorator(fieldName, sequence);
	}

	/**
	 * Helper method that generates a {@link Decorator} of the type {@link SequenceDecorator}
	 * with a <strong>null</strong> {@link Sequence}. This will make the field receive directly
	 * an integer value, being 1, 2, 3, 4 and so on.
	 * @param fieldName The name of the field that needs to be changed.
	 * @return A {@link SequenceDecorator} implementation.
	 */
	public static Decorator<Object> sequence(String fieldName) {
		return new SequenceDecorator(fieldName, null);
	}

	/**
	 * Helper method that generates a {@link Decorator} of the type {@link AssociationDecorator}.
	 * See {@link AssociationDecorator#AssociationDecorator(String)} for details.
	 * @param fieldName The name of the field that needs to be changed.
	 * @return An {@link AssociationDecorator} implementation.
	 */
	public static Decorator<Object> association(String fieldName) {
		return new AssociationDecorator(fieldName);
	}

	/**
	 * Helper method that generates a {@link Decorator} of the type {@link AssociationDecorator}.
	 * See {@link AssociationDecorator#AssociationDecorator(String, String)} for details.
	 * @param fieldName The name of the field that needs to be changed.
	 * @return An {@link AssociationDecorator} implementation.
	 */
	public static Decorator<Object> association(String fieldName, String factoryName) {
		return new AssociationDecorator(fieldName, factoryName);
	}

	/**
	 * Helper method that generates a {@link Decorator} of the type {@link AssociationDecorator}.
	 * See {@link AssociationDecorator#AssociationDecorator(String, Class)} for details.
	 * @param fieldName The name of the field that needs to be changed.
	 * @return An {@link AssociationDecorator} implementation.
	 */
	public static Decorator<Object> association(String fieldName, Class<?> factoryClass) {
		return new AssociationDecorator(fieldName, factoryClass);
	}

	/**
	 * Helper method that generates a {@link Decorator} of the type {@link AssociationDecorator}.
	 * See {@link AssociationDecorator#AssociationDecorator(String, Class, String)} for details.
	 * @param fieldName The name of the field that needs to be changed.
	 * @return An {@link AssociationDecorator} implementation.
	 */
	public static Decorator<Object> association(String fieldName, Class<?> factoryClass, String factoryName) {
		return new AssociationDecorator(fieldName, factoryClass, factoryName);
	}
}
