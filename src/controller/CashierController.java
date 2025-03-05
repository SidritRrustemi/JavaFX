package controller;

import java.util.ArrayList;
import java.util.InputMismatchException;

import dao.EmployeeDAO;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Cashier;
import model.Employee;
import model.Inventory;
import model.Item;
import model.ClientCard;
import view.CashierView;

public class CashierController {

	private final CashierView view;
	private final EmployeeDAO employeeDAO;
	
	public CashierView getView() {
		return view;
	}
	
	public CashierController(Stage stage, Employee employee) {

		this.view = new CashierView(stage, employee);
		this.employeeDAO = new EmployeeDAO();
		
		setSidebarOnClickActions();
		populateInitialProducts();
		searchProducts();
		setOtherActions();
		setClearBill();
	}
	
	private void setSidebarOnClickActions() {
        this.view.getNewBillButton().setOnAction(e -> this.view.displayNewBillView());
        this.view.getViewTodaysBillsButton().setOnAction(e -> this.view.displayViewTodaysBillsView());
        this.view.getTotalOfAllBillsButton().setOnAction(e -> this.view.displayTotalOfAllBillsView());
        this.view.getProfileButton().setOnAction(e -> this.view.displayProfileView());
        this.view.getLogoutButton().setOnAction(e -> this.view.navigateToLogin());
	}
	
	private void searchProducts() {
		this.view.getSearchField().setOnKeyTyped(e -> {
		    String searchText = this.view.getSearchField().getText().trim().toLowerCase();

		    view.getFlowPane().getChildren().clear();

		    if (searchText.trim().isEmpty()) {
		        view.getFlowPane().getChildren().addAll(this.view.getAllProductCards());
		    } else {
		        for (Node node : this.view.getAllProductCards()) {
		            VBox productCard = (VBox) node;
		            Text nameLabel = (Text) productCard.getChildren().get(1);
		            String productName = nameLabel.getText().toLowerCase();
		            if (productName.contains(searchText)) {
		                view.getFlowPane().getChildren().add(productCard);
		            }
		        }
		    }
		    if (view.getFlowPane().getChildren().isEmpty()) {
		        Text noResultsText = new Text("No results found!");
		        noResultsText.setFont(Font.font("Arial", 16));
		        noResultsText.setFill(Color.DARKGRAY);
		        StackPane centeredTextContainer = new StackPane(noResultsText);
		        view.getFlowPane().getChildren().add(centeredTextContainer);
		    }
		});
    }
	
	private void populateInitialProducts() {
	    this.view.getFlowPane().getChildren().clear();
	    this.view.getTableItems().clear();
	    this.view.getTableView().getItems().clear();
	    this.view.getAllProductCards().clear();
	    
	    for (int i = 0; i < Inventory.getItems().size(); i++) {
	    	Item product = Inventory.getItems().get(i);
	        String productName = product.getName();
	        int quantity = 0;
	        for(Item item: Inventory.getItems()) {
	        	if(item.getName().equals(productName)) {
	        		quantity += Inventory.getQuantity().get(Inventory.getItems().indexOf(item));
	        	}
	        }
	        boolean productExists = false;
	        
	        for (Node child : this.view.getFlowPane().getChildren()) {
	            VBox productCard = (VBox)child;
	            Text nameLabel = (Text)productCard.getChildren().get(1);
	            if (nameLabel != null && nameLabel.getText().equals(productName)) {
	                productExists = true;
	                break;
	            }
	        }
	        if (!productExists && quantity != 0) {
	            VBox productCard = this.view.createProductCard(product, quantity);
	            attachCardListeners(productCard, quantity);
	            this.view.getFlowPane().getChildren().add(productCard);
	        }
	    }
	}
	
	private void attachCardListeners(VBox productCard, int quantity) {
        HBox quantityControl = (HBox) productCard.getChildren().get(3);
        Button decreaseButton = (Button) quantityControl.getChildren().get(0);
        TextField quantityField = (TextField) quantityControl.getChildren().get(1);
        Button increaseButton = (Button) quantityControl.getChildren().get(2);

        CashierView.TableItem tableitem = new CashierView.TableItem((Item) productCard.getUserData(), 0);

        decreaseButton.setOnAction(e -> {
            int currentQuantity = Integer.parseInt(quantityField.getText());
            if (currentQuantity > 0) {
            	if(currentQuantity == 1) {
                	decreaseButton.setDisable(true);
                }
                if (currentQuantity == 1 && this.view.getTableItems().contains(tableitem)) {
                    this.view.getTableItems().remove(tableitem);
                }
            	increaseButton.setDisable(false);
                currentQuantity--;
                quantityField.setText(String.valueOf(currentQuantity));
                tableitem.setQuantity(currentQuantity);
                tableitem.setTotal(tableitem.getPrice() * currentQuantity);
                this.view.updateTable();
            }
        });

        increaseButton.setOnAction(e -> {
            int currentQuantity = Integer.parseInt(quantityField.getText());
            if (currentQuantity < quantity) {
            	if (currentQuantity == quantity - 1) {
                	increaseButton.setDisable(true);
                }
                if (!this.view.getTableItems().contains(tableitem)) {
                	this.view.getTableItems().add(tableitem);
                }
            	decreaseButton.setDisable(false);
                currentQuantity++;
                quantityField.setText(String.valueOf(currentQuantity));
                tableitem.setQuantity(currentQuantity);
                tableitem.setTotal(tableitem.getPrice() * currentQuantity);
                this.view.updateTable();
            } else {
            }
        });
	}
	
	private void setOtherActions() {
        // Submit Button Action in Change Password View
		this.view.getSubmitButton().setOnAction(e -> {
            String currentPassword = this.view.getCurrentPasswordField().getText();
            String newPassword = this.view.getNewPasswordField().getText();
            String confirmNewPassword = this.view.getConfirmNewPasswordField().getText();

            if (currentPassword.trim().isEmpty() || newPassword.trim().isEmpty() || confirmNewPassword.trim().isEmpty()) {
                this.view.showAlert(Alert.AlertType.WARNING, "Empty Fields", "All fields must be filled!");
            } else if (!newPassword.equals(confirmNewPassword)) {
                this.view.showAlert(Alert.AlertType.ERROR, "Password Mismatch", "New password and confirmation do not match.");
            } else if (currentPassword.equals(((Cashier)this.view.getEmployee()).getPassword())) {
                this.view.showAlert(Alert.AlertType.INFORMATION, "Success", "Password changed successfully!");
                this.view.getEmployee().setPassword(newPassword);
                this.view.getPopupStage().close();
                this.view.displayProfileView();
                this.employeeDAO.writeEmployeeToFile();
            } else {
            	this.view.showAlert(Alert.AlertType.ERROR, "Wrong password", "The current password you entered is wrong.");
            }
        });
		
		this.view.getSubmitBtn().setOnAction(e -> {
		    String cashPaidText = this.view.getCashPaidField().getText().trim();

		    System.out.println("YesButton1 selected: " + this.view.getYesButton1().isSelected());
		    System.out.println("NoButton1 selected: " + this.view.getNoButton1().isSelected());
		    System.out.println("YesButton2 selected: " + this.view.getYesButton2().isSelected());
		    System.out.println("NoButton2 selected: " + this.view.getNoButton2().isSelected());
		    
		    if (this.view.getYesButton1().isSelected()) { 
		        if (this.view.getClientCodeField().getText().trim().isEmpty() || cashPaidText.isEmpty()) {
		            this.view.showAlert(Alert.AlertType.ERROR, "Bill", "Fields CANNOT be empty!");
		        } else {
		            String clientCode = this.view.getClientCodeField().getText();
		            ClientCard client = null;
		            for (ClientCard clientCard : Cashier.getClients()) {
		                if (clientCard.getClientCode().equals(clientCode)) {
		                    client = clientCard;
		                }
		            }
		            if (client == null) {
		                this.view.showAlert(Alert.AlertType.ERROR, "Client Card", "Client card with this ID not found!");
		            } else {
		                submitBill(client, cashPaidText, this.view.getPrimaryStage());
		            }
		        }

		    } else if (this.view.getNoButton1().isSelected() && this.view.getYesButton2().isSelected()) { 
		        String phoneNumber = this.view.getClientPhoneNrField().getText();
		        boolean isNumeric = true;
		        for (char c : phoneNumber.toCharArray()) {
		            if (!Character.isDigit(c)) {
		                isNumeric = false;
		                break;
		            }
		        }
		        if (!isNumeric) {
		            this.view.showAlert(Alert.AlertType.ERROR, "Client Card", "Phone number isn't numeric!");
		        } else {
		            ClientCard client = new ClientCard(this.view.getClientNameField().getText(), this.view.getClientSurnameField().getText(), phoneNumber);
		            submitBill(client, cashPaidText, this.view.getPrimaryStage());
		        }

		    } else if (this.view.getNoButton1().isSelected() && this.view.getNoButton2().isSelected()) { 
		        submitBill(null, cashPaidText, this.view.getPrimaryStage());
		    }
		});
		
		this.view.getCreateBill().setOnAction(e -> {
            if (!this.view.getTableItems().isEmpty() && !(this.view.getTableItems() == null)) {
                this.view.askForAdditionalInformation();
            } else {
                this.view.showAlert(AlertType.ERROR, "Bill", "Bill cannot be created without any items added to the receipt!");
            }
        });
	}
	
	private void submitBill(ClientCard clientCard, String cashField, Stage stage) {
    	try {
    		double cashInput = Double.parseDouble(cashField);
  			ArrayList<Item> items = new ArrayList<>();
  			ArrayList<Integer> quantities = new ArrayList<>();
  			for(CashierView.TableItem item: this.view.getTableItems()) {
            	items.add(item.getItem());
            	quantities.add(item.getQuantity());
            }
  			if(clientCard!=null) {
  				this.view.getEmployee().createClientCard(clientCard.getClientName(), clientCard.getClientSurname(), clientCard.getClientPhoneNr());
  			}
  			((Cashier)this.view.getEmployee()).createBill(this.view.getEmployee().getEmployeeID(), items, quantities, cashInput, clientCard);
            this.view.showAlert(Alert.AlertType.INFORMATION, "Bill", "Bill created successfully!");
            stage.close();
            this.view.getTotal().setText("Total Amount: $0.00");
            this.view.displayNewBillView();
            this.populateInitialProducts();
            employeeDAO.writeInventoryToFile();
            employeeDAO.writeEmployeeToFile();
  		 } catch(NumberFormatException ex) {
  			this.view.showAlert(Alert.AlertType.ERROR, "Bill", "Cash Field cannot be empty!");
  		 } catch(InputMismatchException ex) {
  			this.view.showAlert(Alert.AlertType.ERROR, "Bill", "Cash Field must be Numeric!");
  		 } catch(IllegalArgumentException ex) {
  			this.view.showAlert(Alert.AlertType.ERROR, "Bill", ex.getMessage());
  		 } 
    }
	
	private void setClearBill() {
		this.view.getClearBillButton().setOnAction(e -> {
			this.view.getTableItems().clear();
			this.view.updateTable();
			populateInitialProducts();
		});
	}
}
