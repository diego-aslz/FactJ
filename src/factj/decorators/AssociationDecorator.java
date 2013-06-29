package factj.decorators;

import factj.Decorator;
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
	protected Decorator<?>[] decorators;

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
	 * @param factoryName The name of the factory that must be used.
	 */
	public AssociationDecorator(String fieldName, String factoryName) {
		this(fieldName, null, factoryName);
	}

	/**
	 * Will cause the field to receive the object built by the factory that has
	 * an empty {@link Factory#name} and the {@link Factory#clazz} matching
	 * the <strong>factoryClass</strong> parameter.
	 * @param fieldName
	 * @param factoryClass The Class of the object that will be set to the field. 
	 */
	public AssociationDecorator(String fieldName, Class<?> factoryClass) {
		this(fieldName, factoryClass, "");
	}

	/**
	 * Will cause the field to receive the object built by the factory that has
	 * a {@link Factory#name} matching the <strong>factoryName</strong> parameter
	 * and the {@link Factory#clazz} matching the <strong>factoryClass</strong> parameter.
	 * @param fieldName
	 * @param factoryClass The Class of the object that will be set to the field. 
	 * @param factoryName The name of the factory that must be used.
	 */
	public AssociationDecorator(String fieldName, Class<?> factoryClass, String factoryName) {
		super(fieldName);
		clazz = factoryClass;
		this.factoryName = factoryName;
	}

	/**
	 * Will cause the field to receive the object built by the factory that has
	 * a {@link Factory#name} matching the <strong>factoryName</strong> parameter
	 * and the {@link Factory#clazz} matching the <strong>factoryClass</strong> parameter.
	 * Also, the objects built will be customized using the <strong>decorators</strong>.
	 * @param fieldName
	 * @param factoryClass The Class of the object you want to set to this field. 
	 * @param factoryName The name of the factory that must be used.
	 * @param decorators To customize the generated object.
	 */
	public AssociationDecorator(String fieldName, Class<?> factoryClass, String factoryName,
			Decorator<?>... decorators) {
		super(fieldName);
		clazz = factoryClass;
		this.factoryName = factoryName;
		this.decorators = decorators;
	}

	@Override
	public Object getValue() {
		if (clazz == null)
			try {
				clazz = getField(fieldName, object.getClass()).getType();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		return FactJ.create(clazz, factoryName, decorators);
	}
}
