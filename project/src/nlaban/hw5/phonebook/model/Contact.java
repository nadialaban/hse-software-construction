package nlaban.hw5.phonebook.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import nlaban.hw5.phonebook.model.validator.annotatios.Name;
import nlaban.hw5.phonebook.model.validator.annotatios.PhoneNumber;
import nlaban.hw5.phonebook.model.validator.annotatios.Required;

public class Contact {

	// ФИО
	@Name
	@Required(isPhone = false)
	private final String firstName;
	@Name
	private final String middleName;
	@Name
	@Required(isPhone = false)
	private final String lastName;

	// Номера телефонов
	@PhoneNumber
	@Required(isPhone = true)
	private final String mobilePhoneNumber;
	@PhoneNumber
	@Required(isPhone = true)
	private final String homePhoneNumber;

	// Адрес
	private final String address;
	// Дата рождения
	private final Date birthDate;
	// Заметка
	private final String note;

	private boolean editing = false;

	public Contact(String firstName, String middleName, String lastName,
			String mobilePhoneNumber, String homePhoneNumber,
			String address, Date birthDate, String note) {
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.mobilePhoneNumber = mobilePhoneNumber;
		this.homePhoneNumber = homePhoneNumber;
		this.address = address;
		this.birthDate = birthDate;
		this.note = note;
	}

	public Contact(String line) throws InvalidPropertiesFormatException {
		String[] data = line.split(";");
		String fName, mName, lName,
				mPhone, hPhone,
				address, dateStr, note;
		try {
			fName = data[0];
			mName = data[1];
			lName = data[2];
			mPhone = data[3];
			hPhone = data[4];
			address = data[5];
			dateStr = data[6];
			note = data[7].substring(1, data[7].length() - 1);
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidPropertiesFormatException("Неверный формат данных");
		}

		Date date = null;
		if (!dateStr.equals("null")) {
			try {
				date = new SimpleDateFormat("dd MMM yyyy").parse(dateStr);
			} catch (ParseException e) {
				throw new InvalidPropertiesFormatException("Неверный формат даты (dd MMM yyyy)");
			}
		}

		this.firstName = fName;
		this.middleName = mName;
		this.lastName = lName;
		this.mobilePhoneNumber = mPhone;
		this.homePhoneNumber = hPhone;
		this.address = address;
		this.birthDate = date;
		this.note = note;
	}

	public Contact() {
		this("", "", "", "",
				"", "", null, "");
	}

	public boolean isEditing() {
		return editing;
	}

	public void setEditing(boolean editing) {
		this.editing = editing;
	}

	public String getFullName() {
		return String.format("%s %s %s", firstName, middleName, lastName);
	}

	public String getFirstName() {
		return firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhoneNumbers() {
		return mobilePhoneNumber + '\n' + homePhoneNumber;
	}

	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	public String getHomePhoneNumber() {
		return homePhoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public String getBirthDate() {
		if (birthDate == null) {
			return "";
		}
		return new SimpleDateFormat("dd MMM yyyy").format(birthDate);
	}

	public String getNote() {
		return note;
	}

	@Override
	public String toString() {
		return firstName +
				";" + middleName +
				";" + lastName +
				";" + mobilePhoneNumber +
				";" + homePhoneNumber +
				";" + address +
				";" + (birthDate == null ? "null" : new SimpleDateFormat("dd MMM yyyy").format(birthDate)) +
				";'" + note + '\'';
	}
}
