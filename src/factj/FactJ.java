package factj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class manages all the factories and builds new objects using them.
 * 
 * @author Diego Aguir Selzlein
 *
 */
public final class FactJ {
	private FactJ() {}
	private static Map<Class<?>, List<Factory>> factories;
	private static Persistence persistence;

	static {
		factories = new HashMap<Class<?>, List<Factory>>();
	}

	/**
	 * Sets the {@link Persistence} that will take care of persisting objects when needed.
	 * @param persistence
	 */
	public static void setPersistence(Persistence persistence) {
		FactJ.persistence = persistence;
	}

	/**
	 * Registers a factory to be used to build objects. You are not supposed to call
	 * this method directly. Take a look at
	 * {@link FactJHelper#fabricate(Class, Decorator...)}.
	 * 
	 * @param factory The factory to be registered.
	 */
	public static void registerFactory(Factory factory) {
		List<Factory> fs = factories.get(factory.getClazz());
		if (fs == null)
			fs = new ArrayList<Factory>();
		fs.add(factory);
		factories.put(factory.getClazz(), fs);
	}

	/**
	 * Builds an object using the registered factory that has the {@link Factory#clazz} matching
	 * the <strong>clazz</strong> parameter and an empty {@link Factory#name}.<br />
	 * If there is more than one factory with an empty name matching the <strong>clazz</strong>,
	 * the one registered first will be used.
	 * @param clazz Class of the object to be built.
	 * @return The object built by the factory or <strong>null</strong> if the factory
	 * was not found.
	 */
	public static Object build(Class<?> clazz) {
		return build(clazz, "");
	}

	/**
	 * Builds an object through {@link #build(Class)} and saves it if {@link #persistence}
	 * is not <strong>null</strong>.
	 * @param clazz
	 * @return
	 */
	public static Object create(Class<?> clazz) {
		return save(build(clazz));
	}

	/**
	 * Builds an object using the registered factory that has the {@link Factory#clazz} matching
	 * the <strong>clazz</strong> parameter and an empty {@link Factory#name}.<br />
	 * If there is more than one factory with an empty name matching the <strong>clazz</strong>,
	 * the one registered first will be used.
	 * After building the object using the factory, the <strong>decorators</strong> will be
	 * used to customize it.
	 * @param clazz Class of the object to be built.
	 * @param decorators Used to customize the object after it is built.
	 * @return The object built by the factory or <strong>null</strong> if the factory
	 * was not found.
	 */
	public static Object build(Class<?> clazz, Decorator ... decorators) {
		return build(clazz, "", decorators);
	}

	/**
	 * Builds an object through {@link #build(Class, Decorator...)} and saves
	 * it if {@link #persistence} is not <strong>null</strong>. The object will
	 * be saved after customizing it using the <strong>decorators</strong>.
	 * @param clazz
	 * @param decorators Used to customize the object after it is built.
	 * @return
	 */
	public static Object create(Class<?> clazz, Decorator ... decorators) {
		return save(build(clazz, decorators));
	}

	/**
	 * Builds an object using the registered factory that has the {@link Factory#clazz} matching
	 * the <strong>clazz</strong> parameter and a {@link Factory#name} matching the
	 * <strong>name</strong> parameter.<br />
	 * If there is more than one factory matching the <strong>clazz</strong> and the
	 * <strong>name</strong>, the one registered first will be used.
	 * @param clazz Class of the object to be built.
	 * @param name The name of the factory, used when you register more than one factory
	 * for the same type.
	 * @return The object built by the factory or <strong>null</strong> if the factory
	 * was not found.
	 */
	public static Object build(Class<?> clazz, String name) {
		return build(clazz, name, (Decorator[]) null);
	}

	/**
	 * Builds an object through {@link #build(Class, String)} and saves it if {@link #persistence}
	 * is not <strong>null</strong>.
	 * @param clazz
	 * @return
	 */
	public static Object create(Class<?> clazz, String name) {
		return save(build(clazz, name));
	}

	/**
	 * Builds an object using the registered factory that has the {@link Factory#clazz} matching
	 * the <strong>clazz</strong> parameter and a {@link Factory#name} matching the
	 * <strong>name</strong> parameter.<br />
	 * If there is more than one factory matching the <strong>clazz</strong> and the
	 * <strong>name</strong>, the one registered first will be used.
	 * After building the object using the factory, the <strong>decorators</strong> will be
	 * used to customize it.
	 * @param clazz Class of the object to be built.
	 * @param name The name of the factory, used when you register more than one factory
	 * for the same type.
	 * @param decorators Used to customize the object after it is built.
	 * @return The object built by the factory or <strong>null</strong> if the factory
	 * was not found.
	 */
	public static Object build(Class<?> clazz, String name, Decorator ... decorators) {
		List<Factory> fs = factories.get(clazz);
		if (name == null)
			name = "";
		Object result = null;
		if (fs != null)
			for (Factory f : fs)
				if (f.getName().equals(name)) {
					result = f.fabricate();
					if (decorators != null)
						for (Decorator d : decorators)
							d.decorate(result);
					break;
				}
		return result;
	}

	/**
	 * Builds an object through {@link #build(Class, String, Decorator...)} and saves it
	 * if {@link #persistence} is not <strong>null</strong>. The object will
	 * be saved after customizing it using the <strong>decorators</strong>.
	 * @param clazz
	 * @param decorators Used to customize the object after it is built.
	 * @return
	 */
	public static Object create(Class<?> clazz, String name, Decorator ... decorators) {
		return save(build(clazz, name, decorators));
	}

	private static Object save(Object o) {
		if (persistence != null)
			persistence.save(o);
		return o;
	}
	/**
	 * Removes all the registered factories.
	 */
	public static void clear() {
		factories.clear();
	}
}
