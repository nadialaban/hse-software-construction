package nlaban.hw6.phonebook.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import nlaban.hw6.phonebook.model.validator.annotatios.Name;
import nlaban.hw6.phonebook.model.validator.annotatios.PhoneNumber;
import nlaban.hw6.phonebook.model.validator.annotatios.Required;

public class Contact {
	@Required(isPhone = false)
	private long id = -1;

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
			note = data[7];
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

	public Contact(ResultSet res) throws SQLException {
		this.id = res.getLong("id");

		this.firstName = res.getString("firstname");
		this.middleName = res.getString("middlename");
		this.lastName = res.getString("lastname");

		this.mobilePhoneNumber = res.getString("mobile_phone_number");
		this.homePhoneNumber = res.getString("home_phone_number");

		this.address = res.getString("address");
		this.note = res.getString("note");

		this.birthDate = res.getDate("birthdate");
	}


	public Contact() {
		this("", "", "", "",
				"", "", null, "");
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getBD() {
		if (birthDate == null) {
			return null;
		}
		return new SimpleDateFormat("yyyy-MM-dd").format(birthDate);
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
				";" + address.replace(';', ',') +
				";" + (birthDate == null ? "null" : new SimpleDateFormat("dd MMM yyyy").format(birthDate)) +
				";'" + note.replace(';', ',');
	}
}
