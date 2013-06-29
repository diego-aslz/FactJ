package factj;

/**
 * Interface used to customize the object when building it. It's using the Decorator Design
 * Pattern.
 * @author Diego Aguir Selzlein
 *
 */
public interface Decorator<T> {
	public void decorate(T o);
}
