package model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Cashier extends Employee implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 8698966567986719825L;
	private Sector cashierSector;
    private ArrayList<Bill> bills = new ArrayList<>();
    private boolean permTotalOfAllBills = true;
    private boolean permShowTodayBills = true;
    private boolean permCreateBill = true;
    private static ArrayList<ClientCard> clients = new ArrayList<>();


    public Cashier(Sector cashierSector, String employeeName, String employeeSurname, Date DOB, String phoneNumber, String email,
            double salary, String address) {
        super(employeeName, employeeSurname, DOB, phoneNumber, email, salary, address);
        if(cashierSector == null) {
        	throw new IllegalArgumentException("Sector cannot be null.");
        }
        this.cashierSector = cashierSector;
        this.role = "Cashier";
    }


    public double totalOfAllBills() {
        if (!permTotalOfAllBills) {
            throw new SecurityException("You do not have permission to view the total of all bills.");
        }
        double sum = 0.0;
        
        for (Bill bill : bills) {
            sum += bill.getTotalAmount();
        }
        
        return sum;
   }


    public ArrayList<Bill> showTodayBills() {
    	if (!permShowTodayBills) {
            throw new SecurityException("You do not have permission to view today's bills.");
        }
        ArrayList<Bill> todayBills = new ArrayList<>();
        for (Bill bill : bills) {
            if (sameDate(new Date(), bill.getSaleDate())) {
                todayBills.add(bill);
            }
        }
        return todayBills;
    }


    private boolean sameDate(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("Dates must not be null.");
        }


        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();


        calendar1.setTime(date1);
        calendar2.setTime(date2);


        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
               calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
               calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }


    public Bill createBill(String employeeID, ArrayList<Item> itemsSold, ArrayList<Integer> quantities, double cash, ClientCard clientCard) {
        if (!permCreateBill) {
            throw new SecurityException("You do not have permission to create bills.");
        }


        if (itemsSold == null || itemsSold.isEmpty() || quantities == null || quantities.isEmpty()) {
            throw new IllegalArgumentException("Items and quantities cannot be null or empty.");
        }


        if (itemsSold.size() != quantities.size()) {
            throw new IllegalArgumentException("Items and quantities lists must have the same size.");
        }


        if (cash <= 0) {
            throw new IllegalArgumentException("Cash amount must be greater than zero.");
        }


        double totalAmount = 0.0;


        for (int i = 0; i < itemsSold.size(); i++) {
            Item item = itemsSold.get(i);
            int quantity = quantities.get(i);
            if (item == null) {
                throw new IllegalArgumentException("Item at index " + i + " is null.");
            }
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity for item '" + item.getName() + "' must be positive.");
            }
            totalAmount += item.getSellingPrice() * quantity;
        }
        
        if (cash < totalAmount) {
            throw new IllegalArgumentException("Insufficient cash. Total amount: " + totalAmount + ", provided: " + cash);
        }

        
        for (int i = 0; i < itemsSold.size(); i++) {
            Item item = itemsSold.get(i);
            int quantity = quantities.get(i);
            
            for (int j = 0; j < Inventory.getItems().size(); j++) {
                if (Inventory.getItems().get(j).getName().equals(item.getName())) {
                    int remainingQuantity = Inventory.getQuantity().get(j) - quantity;
                    
                    if (remainingQuantity <= 0) {
                        quantity -= Inventory.getQuantity().get(j);
                        Inventory.getItems().remove(j);
                        Inventory.getQuantity().remove(j);
                        j--;
                    } else {
                        Inventory.getQuantity().set(j, remainingQuantity);
                        break;
                    }
                }
            }
        }
        
        Bill newBill = new Bill(employeeID, itemsSold, quantities, cash, clientCard);
        bills.add(newBill);
        
        return newBill;
    }

    public void setPermShowTotalOfAllBills(boolean value) {
    	this.permTotalOfAllBills = value;
    }

    public void setPermShowTodayBill(boolean value) {
        this.permShowTodayBills = value;
    }

    public void setPermCreateBill(boolean value) {
        this.permCreateBill = value;
    }

    public void setCashierSector(Sector sector) {
        this.cashierSector = sector;
    }
    
    public static ArrayList<ClientCard> getClients() {
        return clients;
    }

	public ArrayList<Bill> getBills() {
		return bills;
	}
	
	public Sector getSector() {
		return this.cashierSector;
	}
	
	public void setSector(Sector cashierSector) {
		this.cashierSector = cashierSector;
	}

	public boolean getPermTotalOfAllBills() {
		return this.permTotalOfAllBills;
	}

	public boolean getPermCreateBill() {
		return this.permCreateBill;
	}

	public boolean getPermShowTodayBills() {
		return this.permShowTodayBills;
	}
}