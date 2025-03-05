package model;

import java.util.UUID;
import java.io.Serializable;
import java.util.ArrayList;

public class Supplier implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1723309756158588173L;
	private String supplierID;
    private String supplierName;
    private String supplierPhoneNumber;
    private String supplierAddress;
    private ArrayList<String> itemsOffered;

    public Supplier(String supplierName, String supplierPhoneNumber, String supplierAddress, ArrayList<String> itemsOffered) {
        if (supplierName == null || supplierName.isBlank()) {
            throw new IllegalArgumentException("Supplier name cannot be null or empty.");
        }

        if (supplierPhoneNumber == null || !isAllDigits(supplierPhoneNumber) || supplierPhoneNumber.length() != 10) {
	throw new IllegalArgumentException("Phone number must be a 10-digit number.");
         }

        if (supplierAddress == null || supplierAddress.isBlank()) {
            throw new IllegalArgumentException("Supplier address cannot be null or empty.");
        }

        if (itemsOffered == null || itemsOffered.isEmpty()) {
            throw new IllegalArgumentException("Items offered cannot be null or empty.");
        }

        this.supplierID = UUID.randomUUID().toString();
        this.supplierName = supplierName;
        this.supplierPhoneNumber = supplierPhoneNumber;
        this.supplierAddress = supplierAddress;
        this.itemsOffered = new ArrayList<>(itemsOffered);  
    }
    
    private boolean isAllDigits(String str) {
		if (str == null) { return false; }
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c)) { return false; }
		}
		return true;
	}
    
    public String getSupplierName() { return this.supplierName; }
    public String getSupplierPhoneNr() { return this.supplierPhoneNumber; }
    public String getSupplierAddress() { return this.supplierAddress; }
    public String getSupplierID() { return this.supplierID; }
    public ArrayList<String> getItemsOffered() { return this.itemsOffered; }
    
    public void setSupplierPhoneNr(String supplierPhoneNumber) { this.supplierPhoneNumber = supplierPhoneNumber; }
    public void setSupplierAddress(String supplierAddress) { this.supplierAddress = supplierAddress; }
    public void setItemsOffered(ArrayList<String> itemsOffered) { 
    	this.itemsOffered = itemsOffered;
    	for(String s: this.itemsOffered) {
    		s.trim();
    	}
    	
    }
}
