package model;



import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Bill implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1700092673449957943L;
	private UUID billNumber;
    private Date saleDate;
    private ArrayList<Item> items;
    private ArrayList<Integer> quantities;
    private String cashierID;
    private double cash;
    private double change;
    private double totalAmount;
    private ClientCard clientCard;
    private double discountRate;
    private static final int DISCOUNT_TIER_1 = 1000;
    private static final int DISCOUNT_TIER_2 = 2000;
    private static final int DISCOUNT_TIER_3 = 3000;


    public Bill(String cashierID, ArrayList<Item> items, ArrayList<Integer> quantities, double cash, ClientCard clientCard) {
        this.billNumber = UUID.randomUUID();
        this.cashierID = cashierID;
        this.items = items;
        this.quantities = quantities;
        this.cash = cash;
        this.clientCard = clientCard;
        this.saleDate = new Date();
        this.totalAmount = calculateTotalAmount(clientCard);
        this.change = calculateChange(cash, totalAmount);
        if(clientCard != null) {
        	clientCard.addPoints((int)(totalAmount/100.0));
        }
        createFile();
    }

    private void createFile() {
    	try (PrintWriter writer = new PrintWriter("src\\bills\\" + this.billNumber.toString())) {
            writer.println(this.toString());
            System.out.println("File created successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
    
    
    public Bill(String cashierID, ArrayList<Item> items, ArrayList<Integer> quantities, double cash) {
        this(cashierID, items, quantities, cash, null);
    }


    public Date getSaleDate() {
        return saleDate;
    }


    public int getTotalQuantity() {
        int total = 0;
        for (int quantity : quantities) {
            if (quantity < 0) {
                throw new IllegalArgumentException("Quantities must be non-negative.");
            }
            total += quantity;
        }
        return total;
    }


    public double calculateTotalAmount(ClientCard clientCard) {
        if (items == null || items.isEmpty() || quantities == null || quantities.isEmpty()) {
            throw new IllegalStateException("Items or quantities cannot be null or empty.");
        }
        double totalAmount = 0.0;
        for (int i = 0; i < items.size(); i++) {
            if (quantities.get(i) < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative.");
            }
            totalAmount += items.get(i).getSellingPrice() * quantities.get(i);
        }
        if (clientCard != null) {
        	int clientPoints = clientCard.getPoints();
        	if(clientPoints > DISCOUNT_TIER_3) {
        		totalAmount *= 0.7;
        		clientCard.subtractPoints(DISCOUNT_TIER_3);
        		this.discountRate = 0.3;
        	}
        	else if(clientPoints > DISCOUNT_TIER_2) {
        		totalAmount *= 0.8;
        		clientCard.subtractPoints(DISCOUNT_TIER_2);
        		this.discountRate = 0.2;
        	}
        	else if(clientPoints > DISCOUNT_TIER_1) {
        		totalAmount *= 0.9;
        		clientCard.subtractPoints(DISCOUNT_TIER_1);
        		this.discountRate = 0.1;
        	}
        	else{
        		this.discountRate = 0;
        	}
        }
        return totalAmount;
    }


    public double getTotalAmount() {
        return totalAmount;
    }


    public double calculateChange(double cash, double totalAmount) {
        if (cash < totalAmount) {
            throw new IllegalArgumentException("Cash provided is less than the total amount.");
        }
        return cash - totalAmount;
    }


    @Override
    /*public String toString() {
        return "The Bill Number: " + this.billNumber + "\nTotal Amount: " + this.totalAmount 
        		+ "\nDiscount: " + (this.discountRate * 100) + "%" + "\nCash Paid: " + this.cash 
        		+ "\nChange: " + this.change + "\nCashier ID: "
        		+ this.cashierID + "\nSale Date: " + this.getSaleDate() 
        		+ "\nClient Code: " + (this.clientCard != null ? this.clientCard.getClientCode() : "No client registered" + "\n");
    }*/
    
    public String toString() {
        StringBuilder text = new StringBuilder();

        // Header
        text.append("=======================================\n")
            .append(String.format("%30s\n", "MARVIS ELECTRONICS"))
            .append("=======================================\n")
            .append("Bill Number: ").append(this.billNumber).append("\n")
            .append("Date: ").append(this.getSaleDate()).append("\n")
            .append("---------------------------------------\n")
            .append(String.format("%-15s%-10s%-10s%-10s\n", "Item", "Qty", "Price", "Total"))
            .append("---------------------------------------\n");

        // Itemized List
        for (int i = 0; i < this.getItemsSold().size(); i++) {
            Item item = this.getItemsSold().get(i);
            String itemName = item.getName();
            int quantity = this.getQuantitiesSold().get(i);
            double price = item.getSellingPrice();
            double total = quantity * price;

            text.append(String.format("%-15s%-10d%-10.2f%-10.2f\n", itemName, quantity, price, total));
        }

        // Footer
        text.append("---------------------------------------\n")
            .append(String.format("%-20s: %-10.2f\n", "Total Amount", this.totalAmount))
            .append(String.format("%-20s: %-10.1f%%\n", "Discount", this.discountRate * 100))
            .append(String.format("%-20s: %-10.2f\n", "Cash Paid", this.cash))
            .append(String.format("%-20s: %-10.2f\n", "Change", this.change))
            .append("---------------------------------------\n")
            .append(String.format("%-20s: %-10s\n", "Cashier ID", this.cashierID))
            .append(String.format("%-20s: %-10s\n", "Client Code", 
                    this.clientCard != null ? this.clientCard.getClientCode() : "No client registered"))
            .append("=======================================\n");

        return text.toString();
    }
    
    public ArrayList<Item> getItemsSold(){
    	return items;
    }
    public ArrayList<Integer> getQuantitiesSold(){
    	return quantities;
    }
    public String getBillNumber() {
    	return this.billNumber.toString();    
    }
	public double getDiscountRate() {
		return this.discountRate;
	}

	public double getCashPaid() {
		return this.cash;
	}
	public double getChange() {
		return this.change;
	}
	public String getCashierID() {
		return this.cashierID.toString();
	}
	public String getClientCode() {
		return this.clientCard == null ? "No client registered" : this.clientCard.getClientCode();
	}
}
