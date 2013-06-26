package factj;

/**
 * This class is responsible of building objects.
 * @author Diego Aguir Selzlein
 *
 */
public class Factory {
	protected Class<?> clazz;
	protected String name;
	protected Decorator[] decorators;

	public Factory(Class<?> clazz) {
		this(clazz, (Decorator[])null);
	}

	/**
	 * Generates a {@link Factory} for the given <strong>clazz</strong>
	 * with the given <strong>decorators</strong> that customize the objects when they get built.
	 * @param clazz Class of the objects this factory will build.
	 * @param decorators Array of {@link Decorator} that customize the objects.
	 */
	public Factory(Class<?> clazz, Decorator ... decorators) {
		this(clazz, "", decorators);
	}

	/**
	 * Generates a {@link Factory} for the given <strong>clazz</strong> and the given <strong>name</strong>
	 * and with the given <strong>decorators</strong> that customize the objects when they get built.
	 * @param clazz Class of the objects this factory will build.
	 * @param name The factory's name that distinguishes it from other factories of the same Class.
	 * @param decorators Array of {@link Decorator} that customize the objects.
	 */
	public Factory(Class<?> clazz, String name, Decorator ... decorators) {
		this.clazz = clazz;
		this.name = name;
		this.decorators = decorators;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Builds a new object. Every time this method gets called, a new instance of
	 * {@link #clazz} will be created and all the {@link #decorators} will be called on it.
	 * @return
	 */
	public Object fabricate() {
		try {
			Object o = clazz.newInstance();
			for (Decorator d : decorators)
				d.decorate(o);
			return o;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
