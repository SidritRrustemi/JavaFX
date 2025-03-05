package view;

import java.util.ArrayList;
import java.util.Date;

import controller.LoginController;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.Bill;
import model.Cashier;
import model.Employee;
import model.Item;

@SuppressWarnings({ "unchecked"})
public class CashierView extends BorderPane {

	// Data Fields for Sidebar
	private Button newBillButton = new Button("New Bill");
    private final Button viewTodaysBillsButton = new Button("View Today's Bills");
    private final Button totalOfAllBillsButton = new Button("Total of All Bills");
    private Button profileButton = new Button("Profile");
    private Button logoutButton = new Button("Logout");
        
    // Data Fields for TableView of Bill
    private Text total = new Text("Total Amount: $0.00");
    private TableView<TableItem> tableView = new TableView<>();
    private ArrayList<TableItem> tableItems = new ArrayList<>();
    private Button clearBillButton = new Button("Clear");
    
    // Data Fields in Change Password View
    private Stage popupStage = new Stage();
    private Button submitButton = new Button("Submit");
    private TextField currentPasswordField = new TextField();
    private TextField newPasswordField = new TextField();
    private TextField confirmNewPasswordField = new TextField();
    
    // Data Field in View Today's Bills View
    private FlowPane flowPane = new FlowPane();
    
    // Data Fields in Ask For Additional Information
    private Stage primaryStage = new Stage();
    private TextField searchField = new TextField();
    private RadioButton yesButton1 = new RadioButton();
    private RadioButton noButton1 = new RadioButton();
    private RadioButton yesButton2 = new RadioButton();
    private RadioButton noButton2 = new RadioButton();
    private TextField clientCodeField = new TextField();
    private TextField clientPhoneNrField = new TextField();
    private TextField clientNameField = new TextField();
    private TextField clientSurnameField = new TextField();
    private TextField cashPaidField = new TextField();
    private Button submitBtn = new Button("Submit");
    
    // Data Fields in Create Product Card
    private Button decreaseButton = new Button("-");
    private Button increaseButton = new Button("+");
    private TextField quantityField = new TextField();
    private TableItem tableItem;
    private ArrayList<VBox> allProductCards = new ArrayList<>();
	
    private Stage stage;
    private Employee employee;
    
    public CashierView(Stage stage, Employee employee) {
        this.stage = stage;
        this.employee = employee;
        setupView();
    }

    public void setupView() {
        BorderPane sidebar = createSidebar();
        this.setLeft(sidebar);
        displayNewBillView();
    }

    public BorderPane createSidebar() {
    	ImageView logoView = new ImageView(new Image("images/logosidebar.png"));
    	logoView.setFitWidth(140);
    	logoView.setPreserveRatio(true);
    	StackPane pane = new StackPane(logoView);
    	pane.setPadding(new Insets(20, 0, 20, 0));

    	String buttonStyle = "-fx-background-color: #1A1A1A; " +
    	                     "-fx-text-fill: white; " +
    	                     "-fx-font-size: 14px; " +
    	                     "-fx-padding: 10px 0; " +
    	                     "-fx-background-radius: 0; " +
    	                     "-fx-border-color: transparent; " +
    	                     "-fx-pref-width: 200px; " + 
    	                     "-fx-pref-height: 50px;";

    	String buttonHoverStyle = "-fx-background-color: #333333; -fx-border-color: #444444;";

    	String profileStyle = "-fx-background-color: #0056A6; " +
    	                            "-fx-text-fill: white; " +
    	                            "-fx-font-size: 14px; " +
    	                            "-fx-padding: 10px 0; " +
    	                            "-fx-background-radius: 5px; " +
    	                            "-fx-pref-width: 180px;";
    	
    	String profileHoverStyle = "-fx-background-color: #483d8b";

    	String logoutStyle = "-fx-background-color: #A80000; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 0; -fx-background-radius: 5px; -fx-pref-width: 180px;";
    	
    	String logoutHoverStyle = "-fx-background-color: #960303;";
    	
    	configureButton(viewTodaysBillsButton, buttonStyle, buttonHoverStyle);
    	configureButton(totalOfAllBillsButton, buttonStyle, buttonHoverStyle);
    	configureButton(newBillButton, buttonStyle, buttonHoverStyle);
    	configureButton(profileButton, profileStyle, profileHoverStyle);
    	configureButton(logoutButton, logoutStyle, logoutHoverStyle);

    	ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(newBillButton);
        buttons.add(viewTodaysBillsButton);
        buttons.add(totalOfAllBillsButton);
    	
    	ImageView logoutIcon = new ImageView(new Image("images/logout.png"));
    	logoutIcon.setFitWidth(16);
    	logoutIcon.setFitHeight(16);
    	logoutIcon.setPreserveRatio(true);
    	logoutButton.setGraphic(logoutIcon);
    	
    	VBox buttonContainer = new VBox(newBillButton, viewTodaysBillsButton, totalOfAllBillsButton);
    	buttonContainer.setSpacing(0); // Remove spacing between buttons
    	buttonContainer.setAlignment(Pos.TOP_CENTER);

    	VBox bottomContainer = new VBox(10, profileButton, logoutButton);
    	bottomContainer.setAlignment(Pos.BOTTOM_CENTER);
    	bottomContainer.setPadding(new Insets(10, 0, 10, 0));

    	BorderPane sidebar = new BorderPane();
    	sidebar.setStyle("-fx-background-color: #2E2E2E; -fx-padding: 0;");
    	sidebar.setPrefWidth(200);
    	sidebar.setTop(pane);
    	BorderPane.setAlignment(logoView, Pos.CENTER);
    	sidebar.setCenter(buttonContainer);
    	sidebar.setBottom(bottomContainer);
    	
    	return sidebar;
    }

    public void displayProfileView() {
    	this.setRight(null);

        HBox dashboardContent = new HBox(40);
        dashboardContent.setPadding(new Insets(30));
        dashboardContent.setAlignment(Pos.CENTER_LEFT);

        ImageView profileImageView = new ImageView(new Image("images/user.png"));
        profileImageView.setFitWidth(300);
        profileImageView.setFitHeight(300);
        profileImageView.setPreserveRatio(true);
        profileImageView.setStyle("-fx-border-color: #ccc; -fx-border-width: 3; -fx-padding: 10;");

        VBox profilePictureBox = new VBox(profileImageView);
        profilePictureBox.setAlignment(Pos.TOP_CENTER);

        VBox detailsBox = new VBox(20);
        detailsBox.setPadding(new Insets(20));

        String[][] userDetails = {
            { "Employee ID:", employee.getEmployeeID() },
            { "Employee Name:", employee.getEmployeeName() },
            { "Employee Surname:", employee.getEmployeeSurname() },
            { "Date of Birth:", employee.getDoB()+"" },
            { "Phone Number:", employee.getPhoneNr() },
            { "Email:", employee.getEmail() },
            { "Address:", employee.getAddress() },
            { "Role:", "Cashier"},
            { "Sector:", ((Cashier)employee).getSector().toString()},
            { "Salary:", "$"+employee.getSalary()+""},
            { "Username:", employee.getUsername()},
            { "Password:", "*".repeat(employee.getPassword().length())}
        };

        for (String[] detail : userDetails) {
            HBox field = new HBox(15);
            Label label = new Label(detail[0]);
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
            Label value = new Label(detail[1]);
            value.setStyle("-fx-text-fill: #555; -fx-font-size: 15px;"); 

            field.getChildren().addAll(label, value);
            detailsBox.getChildren().add(field);
        }

        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 12px 16px;");
        changePasswordButton.setCursor(Cursor.HAND);
        changePasswordButton.setOnAction(e -> showChangePasswordWindow());

        detailsBox.getChildren().add(changePasswordButton);

        dashboardContent.getChildren().addAll(profilePictureBox, detailsBox);
        this.setCenter(dashboardContent);
    }

    public void showChangePasswordWindow() {
        popupStage = new Stage();
        popupStage.setTitle("Change Password");
        popupStage.setResizable(false);

        VBox popupContent = new VBox(10);
        popupContent.setPadding(new Insets(15, 15, 5, 15));
        popupContent.setAlignment(Pos.TOP_LEFT);

        currentPasswordField = new PasswordField();
        currentPasswordField.setPromptText("Enter Current Password");
        newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Enter New Password");
        confirmNewPasswordField = new PasswordField();
        confirmNewPasswordField.setPromptText("Confirm New Password");

        submitButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        submitButton.setCursor(Cursor.HAND);
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
        cancelButton.setCursor(Cursor.HAND);

        HBox buttonBox = new HBox(10, submitButton, cancelButton);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);

        cancelButton.setOnAction(e -> popupStage.close());

        popupContent.getChildren().addAll(new Label("Current Password:"), currentPasswordField, new Label("New Password:"), newPasswordField, new Label("Confirm Password:"), confirmNewPasswordField, buttonBox);

        Platform.runLater(() -> popupContent.requestFocus());
        
        Scene popupScene = new Scene(popupContent, 300, 245);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
    
    public void configureButton(Button button, String style, String hoverStyle) {
        button.setStyle(style);
        button.setOnMouseEntered(e -> button.setStyle(style + hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(style));
        button.setOnAction(e -> button.setStyle(style + hoverStyle));
        button.setCursor(Cursor.HAND);
    }
    
    public void navigateToLogin() {
        LoginController loginController = new LoginController(stage);
        Scene loginScene = new Scene(loginController.getView(), 350, 430);
        stage.setScene(loginScene);
        stage.setTitle("Login - Marvis Electronics");
        
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
 	    stage.setX((screenBounds.getWidth() - 350) / 2);
 	    stage.setY((screenBounds.getHeight() - 430) / 2);
        
        stage.show();
    }
    
    public void displayViewTodaysBillsView(){
    	if(!((Cashier)employee).getPermShowTodayBills()) {
    		showAlert(AlertType.ERROR, "Permission Error", "You don't have permission to view today's bills!");
    		return;
    	} else if(((Cashier)employee).showTodayBills().size() == 0 || ((Cashier)employee).showTodayBills() == null) {
    		showAlert(AlertType.INFORMATION, "Bills", "There are no bills to view for today!");
    		return;
    	}
    	Pane pane = new Pane();
    	this.setRight(pane);
    	VBox viewTodaysBillsContent = new VBox();
        viewTodaysBillsContent.setAlignment(Pos.CENTER);
        viewTodaysBillsContent.setPadding(new Insets(20));
        viewTodaysBillsContent.setStyle("-fx-background-color: #F9F9F9;");
        
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(10);
        flowPane.setVgap(10);
        flowPane.setPadding(new Insets(10));
        flowPane.setStyle("-fx-background-color: #f9f9f9;");

        ArrayList<Bill> bills = ((Cashier)employee).showTodayBills(); 
        
        final boolean[] isExpanded = new boolean[bills.size()];
        for(int i = 0; i < isExpanded.length; i++) {
            isExpanded[i] = false;
        }
        
        ArrayList<Button> billButtons = new ArrayList<>();
        
        int i=1;
        for (Bill bill: bills) {
        	VBox billCard = new VBox(10);
            billCard.setAlignment(Pos.CENTER);
            String billCardStyle = "-fx-background-color: #ffffff; -fx-border-radius: 10px; -fx-border-color: #ccc; -fx-border-width: 2; -fx-padding: 3;";
            String billCardHoverStyle = "-fx-background-color: #f2f0f0; -fx-border-radius: 10px; -fx-border-color: #ccc; -fx-border-width: 2; -fx-padding: 3;";
            billCard.setStyle(billCardStyle);
            billCard.setPrefWidth(175);
            billCard.setMaxWidth(175);

            Text billText = new Text("Bill " + i);
            billText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            billText.setFill(Color.BLACK);

            ImageView imageView = new ImageView(new Image("images/bill.png"));
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);
            billCard.getChildren().addAll(imageView, billText);

            Button billButton = new Button();
            billButton.setGraphic(billCard);
            billButton.setStyle("-fx-background-color: transparent; -fx-border-radius: 10px;");
            billButton.setCursor(Cursor.HAND);
            billButtons.add(billButton);
            billButton.setOnAction(e -> {
                pane.getChildren().clear();
                int billIndex = bills.indexOf(bill);
                
                for (int j = 0; j < billButtons.size(); j++) {
                    billButtons.get(j).getGraphic().setStyle(billCardStyle); 
                }
                
                if (!isExpanded[billIndex]) {
                    for (int j = 0; j < isExpanded.length; j++) {
                        isExpanded[j] = false;
                    }
                    
                    this.setRight(pane);
                    billButton.getGraphic().setStyle(billCardHoverStyle);
                    VBox billBox = openReceipt(bill);
                    pane.getChildren().add(billBox);
                    isExpanded[billIndex] = true;
                } else {
                    this.setRight(null);
                    isExpanded[billIndex] = false;
                }
            });
            
            flowPane.getChildren().add(billButton);
            i++;
        }
        
        this.setCenter(new ScrollPane(flowPane));
    }
    
    public VBox openReceipt(Bill bill) {
    	String shopName = "Marvis Electronics";
        String billNumber = bill.getBillNumber();
        Date saleDate = bill.getSaleDate();
        double totalAmount = bill.getTotalAmount();
        double discountRate = bill.getDiscountRate();
        double cashPaid = bill.getCashPaid();
        double change = bill.getChange();
        String cashierID = bill.getCashierID();
        String clientCode = bill.getClientCode();

        ArrayList<Item> itemsSold = bill.getItemsSold();
        ArrayList<Integer> quantitiesSold = bill.getQuantitiesSold();

        int separatorLength = 60;
        String wideSeparator = "=".repeat(separatorLength);
        String midSeparator = "-".repeat(separatorLength);

        VBox billBox = new VBox(10);
        billBox.setPadding(new Insets(20));
        billBox.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ccc; -fx-border-width: 2;");

        Text header = new Text(shopName);
        header.setFont(Font.font("Courier New", FontWeight.BOLD, 20));
        header.setFill(Color.DARKBLUE);
        header.setTextAlignment(TextAlignment.CENTER);

        Text billInfo = new Text(String.format("%-20s: %-10s\n", "Bill Number ", billNumber) +
        						 String.format("%-20s: %-10s", "Date: ", saleDate));
        billInfo.setFont(Font.font("Courier New", 14));
        billInfo.setFill(Color.BLACK);

        Text separator1 = new Text(wideSeparator);
        separator1.setFont(Font.font("Courier New", FontWeight.BOLD, 14));

        Text tableHeader = new Text(String.format("%-15s%-15s%-13s%-13s", "Item", "Quantity", "Price", "Total"));
        tableHeader.setFont(Font.font("Courier New", FontWeight.BOLD, 14));
        tableHeader.setFill(Color.DARKGRAY);

        Text separator2 = new Text(midSeparator);
        separator2.setFont(Font.font("Courier New", FontWeight.BOLD, 14));

        VBox itemsBox = new VBox(5);
        for (int j = 0; j < itemsSold.size(); j++) {
            String itemRow = String.format("%-15s%-15d$%-12.2f$%-11.2f",
                    itemsSold.get(j).getName(),
                    quantitiesSold.get(j),
                    itemsSold.get(j).getSellingPrice(),
                    quantitiesSold.get(j) * itemsSold.get(j).getSellingPrice());
            Text itemText = new Text(itemRow);
            itemText.setFont(Font.font("Courier New", 14));
            itemsBox.getChildren().add(itemText);
        }

        Text separator3 = new Text(midSeparator);
        separator3.setFont(Font.font("Courier New", FontWeight.BOLD, 14));

        Text footer = new Text(
        		String.format("%-20s: %-10s\n", "Discount", discountRate * 100+"%") +
        		String.format("%-20s: $%-10.2f\n", "Total Amount", totalAmount) +
                String.format("%-20s: $%-10.2f\n", "Cash Paid", cashPaid) +
                String.format("%-20s: $%-10.2f\n", "Change", change) +
                String.format("%-20s: %s\n", "Cashier ID", cashierID) +
                String.format("%-20s: %-10s\n", "Client Code", clientCode)
        );
        footer.setFont(Font.font("Courier New", 14));
        footer.setFill(Color.BLACK);

        Text separator4 = new Text(wideSeparator);
        separator4.setFont(Font.font("Courier New", FontWeight.BOLD, 14));

        billBox.getChildren().addAll(header, billInfo, separator1, tableHeader, separator2, itemsBox, separator3, footer, separator4);
        
        return billBox;
    }
    
    public void displayNewBillView() {
    	if(employee instanceof Cashier && !((Cashier)employee).getPermCreateBill()) {
    		showAlert(AlertType.ERROR, "Permission Error", "You don't have permission to create a new bill!");
    		return;
    	}
    	this.setCenter(null);
    	VBox dashboardContent = new VBox();
        dashboardContent.setPadding(new Insets(10, 5, 10, 5));
        dashboardContent.setStyle("-fx-background-color: #F9F9F9;");
        
        searchField.setPromptText("Search...");
        searchField.setStyle("-fx-font-size: 14px; -fx-background-color: #f0f0f0; -fx-padding: 5px;");
        searchField.setPrefWidth(200);
        searchField.setMinWidth(200);
        searchField.setMaxWidth(200);
        
        HBox searchBarContainer = new HBox(searchField);
        searchBarContainer.setSpacing(10);
        searchBarContainer.setPadding(new Insets(10));
        searchBarContainer.setAlignment(Pos.TOP_CENTER);
        searchBarContainer.setStyle("-fx-border-radius: 10px;");
        
        flowPane.setHgap(10);
        flowPane.setVgap(10);
        flowPane.setPadding(new Insets(10));
        flowPane.setStyle("-fx-background-color: #f9f9f9;");
        
        billView();
        dashboardContent.getChildren().addAll(searchBarContainer, flowPane);
        dashboardContent.setAlignment(Pos.TOP_CENTER);
        this.setCenter(dashboardContent);
    }
    
    private Button createBill = new Button("Create Bill");
    public Button getCreateBill() { return this.createBill; }
    
    public void billView() {
        Text title = new Text("RECEIPT");
        title.setFont(new Font("Times New Roman", 24));
        
        VBox titleBox = new VBox(title);
        titleBox.setPadding(new Insets(10, 0, 10, 0));
        titleBox.setAlignment(Pos.CENTER);
        
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefWidth(400);

        TableColumn<TableItem, String> nameCol = new TableColumn<>("Item");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<TableItem, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<TableItem, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<TableItem, Double> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));

        if(tableView.getColumns().isEmpty()) {
        	tableView.getColumns().addAll(nameCol, quantityCol, priceCol, totalCol);
        }

        if(tableItems.isEmpty())
        total = new Text("Total Amount: $0.00");
        
        total.setFont(new Font("Times New Roman", 18));
        total.setStyle("-fx-padding: 10px 10px 0px 10px;");
        
        createBill.setCursor(Cursor.HAND);
        String createBillStyle = "-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-margin: 10px 0;";
        createBill.setStyle(createBillStyle); 

        createBill.setOnMouseEntered(e -> createBill.setStyle(createBillStyle + "-fx-background-color: #218838;"));
        createBill.setOnMouseExited(e -> createBill.setStyle(createBillStyle));

        
        clearBillButton.setCursor(Cursor.HAND);
        String clearBillStyle = "-fx-background-color: #f21d1d; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-margin: 10px 0;";
        clearBillButton.setStyle(clearBillStyle); 

        clearBillButton.setOnMouseEntered(e -> clearBillButton.setStyle(clearBillStyle + "-fx-background-color: #d60d0d;"));
        clearBillButton.setOnMouseExited(e -> clearBillButton.setStyle(clearBillStyle));
        
        HBox buttonBox = new HBox(15, createBill, clearBillButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(0, 0, 10, 0));
        
        HBox totalBox = new HBox(total);
        totalBox.setAlignment(Pos.CENTER_RIGHT);
        totalBox.setPadding(new Insets(10));

        VBox bottom = new VBox(10, totalBox, buttonBox);

        BorderPane layout = new BorderPane();
        layout.setTop(titleBox);
        layout.setCenter(tableView);
        layout.setBottom(bottom);

        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setAlignment(bottom, Pos.CENTER);

        this.setRight(layout);
    }
    
    public static class TableItem {
    	private final Item item;
    	private final StringProperty name;
        private final IntegerProperty quantity;
        private final DoubleProperty price;
        private final DoubleProperty total;
            
    	public TableItem(Item item, int quantity){
    		this.item = item;
    		this.name = new SimpleStringProperty(item.getName());
            this.quantity = new SimpleIntegerProperty(quantity);
            this.price = new SimpleDoubleProperty(item.getSellingPrice());
            this.total = new SimpleDoubleProperty(item.getSellingPrice() * quantity);
    	}

        public String getName() { return name.get(); }
        public void setName(String name) { this.name.set(name); }
        public Integer getQuantity() { return quantity.get(); }
        public void setQuantity(Integer quantity) { this.quantity.set(quantity); }
        public Double getPrice() { return price.get(); }
        public void setPrice(Double price) {this.price.set(price); }
        public Double getTotal() { return total.get(); }
        public void setTotal(Double total) { this.total.set(total); }
        public StringProperty nameProperty() { return name;}
        public IntegerProperty quantityProperty() { return quantity;}
        public DoubleProperty priceProperty() { return price; }
        public DoubleProperty totalProperty() { return total; }
        public Item getItem(){ return this.item; }
    }
    
    public void askForAdditionalInformation() {
        primaryStage = new Stage();
        primaryStage.setResizable(false);
        ArrayList<Node> list = new ArrayList<>();
        GridPane root = new GridPane();
        root.setPadding(new Insets(20));
        root.setVgap(15);
        root.setHgap(10);

        Text question1 = new Text("Do you have a client ID?");
        question1.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        ToggleGroup initialGroup = new ToggleGroup();
        yesButton1 = new RadioButton("Yes");
        noButton1 = new RadioButton("No");
        yesButton1.setToggleGroup(initialGroup);
        noButton1.setToggleGroup(initialGroup);
        HBox question1Box = new HBox(15, yesButton1, noButton1);
        question1Box.setAlignment(Pos.CENTER_LEFT);

        Label clientCode = new Label("Enter Client Code:");
        clientCodeField = new TextField();
        clientCodeField.setPromptText("Enter code here...");
        list.add(clientCodeField);

        Text question2 = new Text("Do you want to create a client ID?");
        question2.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        ToggleGroup secondGroup = new ToggleGroup();
        yesButton2 = new RadioButton("Yes");
        list.add(yesButton2);
        noButton2 = new RadioButton("No");
        list.add(noButton2);
        yesButton2.setToggleGroup(secondGroup);
        noButton2.setToggleGroup(secondGroup);
        HBox question2Box = new HBox(15, yesButton2, noButton2);
        question2Box.setAlignment(Pos.CENTER_LEFT);

        Label clientName = new Label("Enter Client Name:");
        clientNameField = new TextField();
        clientNameField.setPromptText("Enter client name...");
        list.add(clientNameField);

        Label clientSurname = new Label("Enter Client Surname:");
        clientSurnameField = new TextField();
        clientSurnameField.setPromptText("Enter surname...");
        list.add(clientSurnameField);

        Label clientPhoneNr = new Label("Enter Client Phone Number:");
        clientPhoneNrField = new TextField();
        clientPhoneNrField.setPromptText("Enter phone number...");
        list.add(clientPhoneNrField);

        Label cashPaid = new Label("Enter Amount of Cash Paid:");
        cashPaidField = new TextField();
        cashPaidField.setPromptText("Enter amount...");
        list.add(cashPaidField);

        submitBtn.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white; -fx-padding: 8px 20px;");
        submitBtn.setCursor(Cursor.HAND);
        list.add(submitBtn);

        setDefault(list, true);

        root.add(question1, 0, 0);
        root.add(question1Box, 1, 0);
        root.add(question2, 0, 1);
        root.add(question2Box, 1, 1);
        root.add(clientCode, 0, 2);
        root.add(clientCodeField, 1, 2);
        root.add(clientName, 0, 3);
        root.add(clientNameField, 1, 3);
        root.add(clientSurname, 0, 4);
        root.add(clientSurnameField, 1, 4);
        root.add(clientPhoneNr, 0, 5);
        root.add(clientPhoneNrField, 1, 5);
        root.add(cashPaid, 0, 6);
        root.add(cashPaidField, 1, 6);

        yesButton1.setOnAction(e -> {
            setDefault(list, true);
            yesButton2.setSelected(false);
            noButton2.setSelected(false);
            clientCodeField.setDisable(false);
            cashPaidField.setDisable(false);
            submitBtn.setDisable(false);
        });

        noButton1.setOnAction(e -> {
            setDefault(list, true);
            yesButton2.setDisable(false);
            noButton2.setDisable(false);
        });

        yesButton2.setOnAction(e -> {
            setDefault(list, false);
            clientCodeField.setDisable(true);
        });

        noButton2.setOnAction(e -> {
            setDefault(list, true);
            yesButton2.setDisable(false);
            noButton2.setDisable(false);
            cashPaidField.setDisable(false);
            submitBtn.setDisable(false);
        });

        VBox box = new VBox();
        box.getChildren().addAll(root, submitBtn);
        box.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(box, 400, 400);
        primaryStage.setTitle("Client Card Information");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void setDefault(ArrayList<Node> list, boolean value) {
    	for(Node n: list) {
    		if(n instanceof TextField) {
    			((TextField) n).clear();
    		}
    		n.setDisable(value);
    	}
    }
    
    
    public VBox createProductCard(Item item, int quantity) {
        VBox productCard = new VBox(5);
        productCard.setStyle("-fx-border-color: #ccc; -fx-border-width: 1; -fx-padding: 10; -fx-background-color: #fff;");
        productCard.setAlignment(Pos.CENTER);

        Image productImage = new Image("images/"+ item.getName()+".png");
        ImageView productImageView = new ImageView(productImage);
        productImageView.setFitWidth(100);
        productImageView.setPreserveRatio(true);
        
        Text nameText = new Text(item.getName());
        nameText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        Text priceText = new Text("$" + item.getSellingPrice());

        HBox quantityControl = new HBox(5);
        quantityControl.setAlignment(Pos.CENTER);

        decreaseButton = new Button("-");
        decreaseButton.setDisable(true);
        decreaseButton.setStyle("-fx-min-width: 30px;");

        quantityField = new TextField("0");
        quantityField.setPrefWidth(40);
        quantityField.setAlignment(Pos.CENTER);
        quantityField.setEditable(false);

        increaseButton = new Button("+");
        increaseButton.setStyle("-fx-min-width: 30px;");
        
        productCard.setUserData(item);

        quantityControl.getChildren().addAll(decreaseButton, quantityField, increaseButton);
        productCard.getChildren().addAll(productImageView, nameText, priceText, quantityControl);
        allProductCards.add(productCard);
        return productCard;
    }

    public void updateTable() {
    	if(tableView != null) {
    		tableView.getItems().clear();
    	}
        for(TableItem t: tableItems) {
        	tableView.getItems().add(t); 
        }
        tableView.refresh(); 
        
        double totalAmount = 0;
        for (TableItem item : tableItems) {
            totalAmount += item.getTotal();
        }
        total.setText("Total Amount: $" + String.format("%.2f", totalAmount));

    }
    
    public void displayTotalOfAllBillsView() {
    	if(!((Cashier)employee).getPermTotalOfAllBills()) {
    		showAlert(AlertType.ERROR, "Permission Error", "You don't have permission to view the total of all bills!");
    		return;
    	}
    	this.setRight(null);        
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Date");
        yAxis.setLabel("Cost (in $)");

        double total = ((Cashier)employee).totalOfAllBills();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Total of all Bills: $" + total);
        barChart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Cost");

        series.getData().add(new XYChart.Data<>("Total", total));

        barChart.getData().add(series);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(barChart);
        stackPane.setPadding(new Insets(10, 30, 10, 30));
        this.setCenter(stackPane);
    }

    public void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();    
    } 
    
    public Button getNewBillButton() { return newBillButton; }
    public Button getViewTodaysBillsButton() { return viewTodaysBillsButton; }
    public Button getTotalOfAllBillsButton() { return totalOfAllBillsButton; }
    public Button getProfileButton() { return profileButton; }
    public Button getLogoutButton() { return logoutButton; }
    
    public TextField getCurrentPasswordField() { return this.currentPasswordField; }
	public TextField getNewPasswordField() { return this.newPasswordField; }
    public TextField getConfirmNewPasswordField() { return this.confirmNewPasswordField; }
    public Employee getEmployee() { return this.employee; }
    public Stage getPopupStage() { return this.popupStage; }
	public FlowPane getFlowPane() { return this.flowPane; }

	public RadioButton getYesButton1() { return this.yesButton1; }
	public RadioButton getNoButton1() { return this.noButton1; }
	public RadioButton getYesButton2() { return this.yesButton2; }
	public RadioButton getNoButton2() { return this.noButton2; }
	public TextField getClientCodeField() { return this.clientCodeField; }
	public TextField getClientPhoneNrField() { return this.clientPhoneNrField; }
	public TextField getClientNameField() { return this.clientNameField; }
	public TextField getClientSurnameField() { return this.clientSurnameField; }
	public TextField getCashPaidField() { return this.cashPaidField; }

	public Stage getPrimaryStage() {return primaryStage; }
	public TextField getQuantityField() { return quantityField; }

	public ArrayList<TableItem> getTableItems() { return this.tableItems; }
	public TableItem getTableitem() { return this.tableItem; }
	public TableView<TableItem> getTableView() { return this.tableView; }
	public ArrayList<VBox> getAllProductCards() { return this.allProductCards; }
    
    public Button getSubmitButton() { return this.submitButton; }
    public TextField getSearchField() { return this.searchField; }
    public Button getSubmitBtn() { return this.submitBtn; }
    public Button getClearBillButton() { return this.clearBillButton; }
    public Text getTotal() { return this.total; }
}