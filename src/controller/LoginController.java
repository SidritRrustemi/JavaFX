package controller;

import dao.EmployeeDAO;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.Administrator;
import model.Cashier;
import model.Employee;
import model.Manager;
import view.LoginView;

public class LoginController {

	private final LoginView view;
	private final EmployeeDAO employeeDAO; 
	
	public LoginView getView() {
		return view;
	}
	
	public LoginController(Stage stage) {
		this.view = new LoginView(stage);
		this.employeeDAO = new EmployeeDAO();
		setActions();
		this.employeeDAO.getAvailableData();
	}
	
    private void setActions() {
    	this.view.getPasswordField().setOnKeyPressed(event -> {
        	if(event.getCode() == KeyCode.ENTER) {
        		handleLogin();
        	}
        });
        this.view.getLoginButton().setOnAction(event -> handleLogin());
    }
	
	private void handleLogin() {
        String username = this.view.getUsernameField().getText();
        String password = this.view.getPasswordField().getText();
        Employee loginEmp = null;

        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            this.view.showAlert(Alert.AlertType.WARNING, "Login Failed", "Username and Password fields cannot be blank!");
        } else{
        	for(Employee emp : this.employeeDAO.getAllEmployees()) {
    			if(username.equals(emp.getUsername()) && password.equals(emp.getPassword()))
    				loginEmp = emp;
    		}
    		if(loginEmp == null) {
    			this.view.showAlert(Alert.AlertType.ERROR, "Login Failed", "Account not Found!");
    		}
        	if (loginEmp instanceof Cashier) {
        		this.view.showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " +
                    username.substring(0, 1).toUpperCase() + username.substring(1) + "!");
        		navigateToCashier(loginEmp);
	        } else if (loginEmp instanceof Manager) {
	        	this.view.showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " +
	                username.substring(0, 1).toUpperCase() + username.substring(1) + "!");
	        	navigateToManager(loginEmp);
	        } else if (loginEmp instanceof Administrator) {
	        	this.view.showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " +
	                username.substring(0, 1).toUpperCase() + username.substring(1) + "!");
	        	navigateToAdministrator(loginEmp);
	        }
        }
    }
	
	 private void navigateToCashier(Employee emp) {
	        CashierController cashierController = new CashierController(this.view.getStage(), emp);
	        Scene cashierScene = new Scene(cashierController.getView(), 1200, 675); // Example size
	        this.view.getStage().setScene(cashierScene);
	        this.view.getStage().setTitle("Cashier Dashboard");
	        
	        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
	        this.view.getStage().setX((screenBounds.getWidth()-1200) / 2);
	 	    this.view.getStage().setY((screenBounds.getHeight()-700) / 2);
	 	    this.view.getStage().show();
	    }
	    
	    private void navigateToManager(Employee emp) {
	        ManagerController managerController = new ManagerController(this.view.getStage(), emp);
	        Scene managerScene = new Scene(managerController.getView(), 1200, 675); // Example size
	        this.view.getStage().setScene(managerScene);
	        this.view.getStage().setTitle("Manager Dashboard");
	        
	        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
	        this.view.getStage().setX((screenBounds.getWidth()-1200) / 2);
	        this.view.getStage().setY((screenBounds.getHeight()-700) / 2);
	        this.view.getStage().show();
	    }
	    
	    private void navigateToAdministrator(Employee emp) {
	        AdministratorController administratorController = new AdministratorController(this.view.getStage(), emp);
	        Scene administratorScene = new Scene(administratorController.getView(), 1200, 675); // Example size
	        this.view.getStage().setScene(administratorScene);
	        this.view.getStage().setTitle("Administrator Dashboard");
	        
	        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
	 	    this.view.getStage().setX((screenBounds.getWidth()-1200) / 2);
	 	    this.view.getStage().setY((screenBounds.getHeight()-700) / 2);
	 	    this.view.getStage().show();
	    }
}
