package view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginView extends StackPane {

    private static final ImageView logoView = new ImageView(new Image("images/logo.png"));
    private static final Label titleLabel = new Label("Welcome Back");
    private static final Label subtitleLabel = new Label("Please log in to continue");
    private static TextField usernameField;
    private static PasswordField passwordField = new PasswordField();
    private static final Hyperlink forgotPasswordLink = new Hyperlink("Forgot password?");
    private static final Button loginButton = new Button("Login");
    private final Stage stage;
    
    public Stage getStage() { return stage; }
    public TextField getUsernameField() { return usernameField; }
    public PasswordField getPasswordField() { return passwordField; }
    public Button getLoginButton() { return loginButton; }

    public LoginView(Stage stage) {
        this.stage = stage;
        setupView();
    }
    
    public void setupView() {
    	
        logoView.setFitWidth(170);
        logoView.setFitHeight(90);
        
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#333333"));

        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        subtitleLabel.setTextFill(Color.web("#555555"));
        
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setStyle("-fx-background-color: #F9F9F9; -fx-border-color: #CCCCCC; -fx-border-radius: 5px; -fx-padding: 10px; -fx-font-size: 14px;");
        usernameField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
            	passwordField.requestFocus();
            }
        });
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-background-color: #F9F9F9; -fx-border-color: #CCCCCC; -fx-border-radius: 5px; -fx-padding: 10px; -fx-font-size: 14px;");
        
        loginButton.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-radius: 5px;");
        loginButton.setCursor(Cursor.HAND);
        
        forgotPasswordLink.setStyle("-fx-text-fill: #0078D7; -fx-font-size: 14px;");
        forgotPasswordLink.setOnAction(event -> {
            showAlert(Alert.AlertType.INFORMATION, "Forgot Password", "Please contact the administrator to change the password.");
        });

        HBox forgotPasswordBox = new HBox(forgotPasswordLink);
        forgotPasswordBox.setAlignment(Pos.CENTER_LEFT);
        
        VBox inputFields = new VBox(10, usernameField, passwordField);
        inputFields.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, logoView, titleLabel, subtitleLabel, inputFields, forgotPasswordBox, loginButton);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #FAFAFA; -fx-border-color: #DDDDDD; -fx-border-width: 1px;");
        
        this.getChildren().add(layout);
        this.setStyle("-fx-background-color: white;");

        Platform.runLater(() -> layout.requestFocus());
    }

    
    public void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();    
    }
}