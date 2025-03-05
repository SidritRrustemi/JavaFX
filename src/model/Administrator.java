package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


public class Administrator extends Employee implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -2432884845067305885L;
	private static ArrayList<Employee> employees = new ArrayList<>();

    public Administrator(String empName, String empSurname, Date dob, String phoneNr, String email, double salary, String address) {
        super(empName, empSurname, dob, phoneNr, email, salary, address);
        this.role = "Administrator";
    }
    
    
    public static ArrayList<Employee> getEmployees() {
        return employees;
    }


    public void addEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null.");
        }
        if (employees.contains(employee)) {
            System.out.println("Employee is already in the list. Cannot add again.");
        } else {
            employees.add(employee);
            System.out.println("Employee added successfully.");
            //sortEmployees();
        }
    }

    public void removeEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null.");
        }
        if (employees.contains(employee)) {
            employees.remove(employee);
            System.out.println("Employee removed successfully.");
        } else {
            System.out.println("Employee is not in the list. Cannot remove.");
        }
    }


    public double getTotalCost() {
        double totalCost = 0.0;

        for (Employee employee : employees) {
            totalCost += employee.getSalary();
        }

        ArrayList<Item> items = Inventory.getItems();
        ArrayList<Integer> quantities = Inventory.getQuantity();

        for (int i = 0; i < items.size(); i++) {
            totalCost += items.get(i).getPurchasePrice() * quantities.get(i);
        }
        return totalCost;
    }
  

    public double getTotalIncome() {
        double totalRevenue = 0.0;

        for (Employee employee : employees) {
            if (employee instanceof Cashier) {
                Cashier cashier = (Cashier) employee;
                ArrayList<Bill> bills = cashier.getBills();

                for (Bill bill : bills) {
                    totalRevenue += bill.getTotalAmount();
                }
            }
        }

        double totalCost = getTotalCost();
        return totalRevenue - totalCost;
    }
   

    public static void addSupplier(Supplier supplier, Manager manager) {
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier cannot be null.");
        }

        if (manager == null) {
            throw new IllegalArgumentException("Manager cannot be null.");
        }

        for (Employee employee : employees) {
            if (employee instanceof Manager && employee.equals(manager)) {
                Manager targetManager = (Manager) employee;

                if (targetManager.getSupplierList().contains(supplier)) {
                    System.out.println("Supplier is already in the manager's supplier list. Cannot add again.");
                    return;
                }

                targetManager.getSupplierList().add(supplier);
                System.out.println("Supplier added successfully to the manager's supplier list." + supplier.getSupplierID());
                return;
            }
        }

        System.out.println("Manager not found in the employee list. Cannot add supplier.");
    }


    public void removeSupplier(Supplier supplier) {
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier cannot be null.");
        }

        for (Employee employee : employees) {
            if (employee instanceof Manager) {
                Manager targetManager = (Manager)employee;
                if (targetManager.getSupplierList().contains(supplier)) {
                	targetManager.getSupplierList().remove(supplier);
                    System.out.println("Supplier removed successfully from the manager's supplier list.");
                    return;
                }
            }
        }
    }
}
