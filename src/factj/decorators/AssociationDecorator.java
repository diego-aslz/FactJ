package factj.decorators;

import factj.FactJ;
import factj.Factory;

/**
 * This {@link FieldDecorator} changes a field in an object using the
 * {@link FactJ#create(Class)} method.
 * @author Diego Aguir Selzlein
 *
 */
public class AssociationDecorator extends FieldDecorator {
	protected Class<?> clazz;
	protected String factoryName;

	/**
	 * Will cause the field to receive the object built by the factory that has
	 * an empty {@link Factory#name} and the {@link Factory#clazz} matching
	 * the field's Class.
	 * @param fieldName
	 */
	public AssociationDecorator(String fieldName) {
		this(fieldName, "");
	}

	/**
	 * Will cause the field to receive the object built by the factory that has
	 * a {@link Factory#name} matching the <strong>factoryName</strong> parameter
	 * and the {@link Factory#clazz} matching the field's Class.
	 * @param fieldName
	 */
	public AssociationDecorator(String fieldName, String factoryName) {
		this(fieldName, null, factoryName);
	}

	/**
	 * Will cause the field to receive the object built by the factory that has
	 * an empty {@link Factory#name} and the {@link Factory#clazz} matching
	 * the <strong>factoryClass</strong> parameter.
	 * @param fieldName
	 */
	public AssociationDecorator(String fieldName, Class<?> factoryClass) {
		this(fieldName, factoryClass, "");
	}

	/**
	 * Will cause the field to receive the object built by the factory that has
	 * a {@link Factory#name} matching the <strong>factoryName</strong> parameter
	 * and the {@link Factory#clazz} matching the <strong>factoryClass</strong> parameter.
	 * @param fieldName
	 */
	public AssociationDecorator(String fieldName, Class<?> factoryClass, String factoryName) {
		super(fieldName, null);
		clazz = factoryClass;
		this.factoryName = factoryName;
	}

	@Override
	public Object getValue() {
		if (clazz == null)
			try {
				clazz = getField(fieldName, object.getClass()).getType();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		return FactJ.create(clazz, factoryName);
	}
}
