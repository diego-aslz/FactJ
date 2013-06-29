package factj.decorators;

import java.lang.reflect.Field;

import factj.Decorator;

/**
 * This {@link Decorator} changes a field in an object setting the given value on it.
 * @author Diego Aguir Selzlein
 *
 */
public class FieldDecorator implements Decorator<Object> {
	protected String fieldName;
	protected Object value;

	protected Object object;

	/**
	 * @param fieldName The name of the field that will be changed.
	 */
	public FieldDecorator(String fieldName) {
		this(fieldName, null);
	}

	/**
	 * @param fieldName The name of the field that will be changed.
	 * @param value The value the field will receive.
	 */
	public FieldDecorator(String fieldName, Object value) {
		this.fieldName = fieldName;
		this.value = value;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public void decorate(Object object) {
		this.object = object;
		try {
			Field f = getField(getFieldName(), object.getClass());
			f.setAccessible(true);
			f.set(object, getValue());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Loads the field based on the given <strong>fieldName</strong>. It searches in the
	 * superclasses if needed.
	 * @param fieldName
	 * @param clazz
	 * @return
	 * @throws NoSuchFieldException
	 */
	protected Field getField(String fieldName, Class<?> clazz) throws NoSuchFieldException {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			if (clazz.getSuperclass() != null)
				return getField(fieldName, clazz.getSuperclass());
			else
				throw e;
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		}
	}
}
