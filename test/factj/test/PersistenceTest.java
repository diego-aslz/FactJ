package factj.test;

import factj.Persistence;

public class PersistenceTest implements Persistence {
	public boolean called = false;

	@Override
	public void save(Object o) {
		called = true;
	}
}