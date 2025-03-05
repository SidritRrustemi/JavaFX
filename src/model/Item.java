package model;

import java.io.Serializable;
import java.util.Date;

public class Item implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5997937447746774278L;
	private String name;
	private String category;
	private Supplier supplier;
	private Date purchaseDate;
	private double purchasePrice;
	private double sellingPrice;
	
	public Item(String name, String category, Supplier supplier, Date purchaseDate, double purchasePrice, double sellingPrice) {

	    if (name == null || name.trim().isBlank()) {
	        throw new IllegalArgumentException("Item name cannot be null or empty.");
	    }
	    if (category == null || category.trim().isBlank()) {
	        throw new IllegalArgumentException("Category cannot be null or empty.");
	    }
	    if (supplier == null) {
	        throw new IllegalArgumentException("Supplier cannot be null.");
	    }
	    if (purchaseDate == null) {
	        throw new IllegalArgumentException("Purchase date cannot be null.");
	    }
	    if (purchasePrice <= 0) {
	        throw new IllegalArgumentException("Purchase price must be greater than 0.0.");
	    }
	    if (sellingPrice <= 0) {
	        throw new IllegalArgumentException("Selling price must be greater than 0.0.");
	    }

	    this.name = name;
	    this.category = category;
	    this.supplier = supplier;
	    this.purchaseDate = purchaseDate;
	    this.purchasePrice = purchasePrice;
	    this.sellingPrice = sellingPrice;
	}

	
	public String getName() { return this.name; }
	public String getCategory() { return this.category; }
	public Supplier getSupplier() { return this.supplier; }
	public Date getPurchaseDate() { return this.purchaseDate; }
	public double getPurchasePrice() { return this.purchasePrice; }
	public double getSellingPrice() { return this.sellingPrice; }
	
	public void setSellingPrice(double sellingPrice) {
		if (sellingPrice <= 0) {
			throw new IllegalArgumentException("Selling price must be greater than 0.0.");
		}
		this.sellingPrice = sellingPrice;
	}
}



























