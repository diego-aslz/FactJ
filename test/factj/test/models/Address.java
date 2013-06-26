package factj.test.models;

public class Address {
	protected int id;
	protected String address;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String name) {
		this.address = name;
	}
	@Override
	public String toString() {
		return "Address [id=" + id + ", address=" + address + "]";
	}
}
