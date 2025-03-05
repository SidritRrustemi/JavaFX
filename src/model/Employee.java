package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public abstract class Employee implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5261310028226706034L;
	private String employeeID;
	private String employeeName;
	private String employeeSurname;
	private Date dob;
	private String phoneNumber;
	private String email;
	private double salary;
	private String address;
	private String username;
	private String password;
	protected String role;
	
	public Employee(String employeeName, String employeeSurname, Date dob, String phoneNumber, String email, double salary, String address) {
		if (employeeName == null || employeeName.trim().isBlank()) {
			throw new IllegalArgumentException("Employee name cannot be null or empty.");
		}
		if (employeeSurname == null || employeeSurname.trim().isBlank()) {
			throw new IllegalArgumentException("Employee surname cannot be null or empty.");
		}
		if (dob == null || !isAtLeast18YearsOld(dob)) {
			throw new IllegalArgumentException("Employee's dob must be written and he must be at least 18 years old.");
		}
		if (phoneNumber == null || !isAllDigits(phoneNumber) || phoneNumber.length() != 10) {
			throw new IllegalArgumentException("Phone number must be a 10-digit number.");
		}
		if (email == null || !email.contains("@")) {
			throw new IllegalArgumentException("Invalid email format.");
		}
		if (salary < 400) {
			throw new IllegalArgumentException("Minimum salaray allowed is 400€.");
		}
		if (address == null || address.trim().isBlank()) {
			throw new IllegalArgumentException("Address cannot be null or empty.");
		}

		this.employeeID = UUID.randomUUID().toString();
		this.employeeName = employeeName;
		this.employeeSurname = employeeSurname;
		this.dob = dob;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.salary = salary;
		this.address = address;
		this.username = employeeName.toLowerCase();
		this.password = employeeName.toLowerCase()+"2025";
	}

	private boolean isAtLeast18YearsOld(Date dob) {
		Date today = new Date();
		long ageInMillis = today.getTime() - dob.getTime();
		int ageInYears = (int) (ageInMillis / (1000 * 60 * 60 * 24 * 365));
		return ageInYears >= 18;
	}

	private boolean isAllDigits(String str) {
		if (str == null) {
			return false;
		}
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}

	public double getSalary() { return this.salary; }

	public void setPhoneNumber(String phoneNumber) {
		if (phoneNumber == null || !isAllDigits(phoneNumber) || phoneNumber.length() != 10) {
			throw new IllegalArgumentException("Phone number must be a 10-digit number(without prefix).");
		}
		this.phoneNumber = phoneNumber;
	}

	public void setEmail(String email) {
		if (email == null || !email.contains("@")) {
			throw new IllegalArgumentException("Invalid email format.");
		}
		this.email = email;
	}

	public void setSalary(double salary) {
		if (salary < 400) {
			throw new IllegalArgumentException("Minimum salaray allowed is 400€.");
		}
		this.salary = salary;
	}

	public void setAddress(String address) {
		if (address == null || address.isBlank()) {
			throw new IllegalArgumentException("Address cannot be null or empty.");
		}
		this.address = address;
	}
	
	public ClientCard createClientCard(String clientName, String clientSurname, String clientPhoneNr) {
    	if(clientName == null || clientSurname == null || clientPhoneNr == null 
    	   || clientName.trim().isEmpty() || clientSurname.trim().isEmpty() || clientPhoneNr.trim().isEmpty()) {
    		throw new IllegalArgumentException("No field can be empty or null.");
    	}
    	ClientCard clientcard = new ClientCard(clientName, clientSurname, clientPhoneNr);
    	Cashier.getClients().add(clientcard);
    	System.out.println(clientcard.getClientCode());
    	return clientcard;
    }
		
	public String getEmployeeID() {
		return this.employeeID;
	}
	public String getEmployeeName() {
		return this.employeeName;
	}
	public String getEmployeeSurname() {
		return this.employeeSurname;
	}
	public String getUsername() {
		return this.username;
	}
	public String getPassword() {
		return this.password;
	}
	public Date getDoB() {
		return this.dob;
	}
	public String getPhoneNr() {
		return this.phoneNumber;
	}
	public String getEmail() {
		return this.email;
	}
	public String getAddress() {
		return this.address;
	}
	public String getRole() {
		return this.role;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}

