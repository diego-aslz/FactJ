package factj.decorators;

/**
 * This {@link FieldDecorator} changes a field in an object setting a value that
 * respects a sequence.
 * @author Diego Aguir Selzlein
 *
 */
public class SequenceDecorator extends FieldDecorator {
	/**
	 * Generates a value for the given sequence number.
	 * @author Diego Aguir Selzlein
	 */
	public interface Sequence {
		public Object generate(int count);
	}
	protected Sequence sequence;
	protected int count = 1;

	/**
	 * When no {@link Sequence} is given (see {@link #SequenceDecorator(String, Sequence)}),
	 * the field will receive the sequence integer value itself.
	 * @param fieldName The name of the field which value will respect a sequence.
	 */
	public SequenceDecorator(String fieldName) {
		this(fieldName, null);
	}

	/**
	 * Changes the field in the object accordingly to the value returned by the
	 * {@link Sequence#generate(int)} method called on the <strong>sequence</strong>
	 * parameter.
	 * @param fieldName The name of the field which value will respect a sequence.
	 * @param sequence The {@link Sequence} implementation that will be called in order
	 * to generate a new value every time an object is built.
	 */
	public SequenceDecorator(String fieldName, Sequence sequence) {
		super(fieldName);
		this.sequence = sequence;
	}

	@Override
	public Object getValue() {
		if (sequence == null)
			return count++;
		return sequence.generate(count++);
	}
}
