package nlaban.hw6.phonebook.model.validator;

public class ValidationError {

	private final String message;
	private final String sender;
	private final String fullName;
	private final long id;
	private final String contact;


	public ValidationError(String message, String sender, String fullName, long id, String contact) {
		this.message = message;
		this.sender = sender;
		this.fullName = fullName;
		this.id = id;
		this.contact = contact;
	}


	public String getMessage() {
		return message;
	}

	public String getSender() {
		return sender;
	}

	public String getFullName() {
		return fullName;
	}
	public long getID() {
		return id;
	}

	public String getContact() {
		return contact;
	}
}
