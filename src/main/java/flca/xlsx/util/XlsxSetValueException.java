package flca.xlsx.util;

public class XlsxSetValueException extends Exception {

	private static final long serialVersionUID = -7438842187577469710L;

	private String message;
	private Object value;
	
	public XlsxSetValueException(String message, Object value, Exception exception) {
		super(exception);
		this.message = message;
		this.value = value;
	}

	public XlsxSetValueException(String message, Object value) {
		super();
		this.message = message;
		this.value = value;
	}

	@Override
	public String toString() {
		return "XlsSetValueException [" + message + ", value=" + value + "]" + " " + this.getMessage();
	}
	
	
	
}
