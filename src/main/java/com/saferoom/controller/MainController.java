package com.saferoom.controller;

import com.jfoenix.controls.JFXButton;
import com.saferoom.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Objects;

public class MainController {

    private static MainController instance;

    @FXML private BorderPane mainPane;
    @FXML private Label viewTitleLabel;
    @FXML private ScrollPane contentArea;
    @FXML private VBox navBox;
    @FXML private JFXButton dashboardButton;
    @FXML private JFXButton roomsButton;
    @FXML private JFXButton messagesButton;
    @FXML private JFXButton friendsButton;
    @FXML private JFXButton fileVaultButton;
    @FXML private JFXButton settingsButton;

    @FXML
    public void initialize() {
        instance = this;
        handleDashboard();
        dashboardButton.setOnAction(event -> handleDashboard());
        roomsButton.setOnAction(event -> handleRooms());
        messagesButton.setOnAction(event -> handleMessages());
        friendsButton.setOnAction(event -> handleFriends());
        fileVaultButton.setOnAction(event -> handleFileVault());
        settingsButton.setOnAction(event -> handleSettings());
    }

    public static MainController getInstance() {
        return instance;
    }

    private void handleDashboard() { setActiveButton(dashboardButton); viewTitleLabel.setText("Dashboard"); loadView("DashboardView.fxml"); }
    public void handleRooms() { setActiveButton(roomsButton); viewTitleLabel.setText("Rooms"); loadView("RoomsView.fxml"); }
    private void handleMessages() { setActiveButton(messagesButton); viewTitleLabel.setText("Messages"); loadView("MessagesView.fxml"); }
    private void handleFriends() { setActiveButton(friendsButton); viewTitleLabel.setText("Friends"); loadView("FriendsView.fxml"); }
    public void handleFileVault() { setActiveButton(fileVaultButton); viewTitleLabel.setText("File Vault"); loadView("FileVaultView.fxml"); }
    private void handleSettings() { setActiveButton(settingsButton); viewTitleLabel.setText("Settings"); loadView("SettingsView.fxml"); }

    public void loadSecureRoomView() {
        loadFullScreenView("SecureRoomView.fxml", true);
    }

    public void loadJoinMeetView() {
        loadFullScreenView("JoinMeetView.fxml", false);
    }

    private void loadFullScreenView(String fxmlFile, boolean isSecureRoom) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("view/" + fxmlFile));
            Parent root = loader.load();

            if (mainPane != null && mainPane.getScene() != null) {
                Scene scene = mainPane.getScene();
                if (isSecureRoom) {
                    SecureRoomController controller = loader.getController();
                    controller.setReturnScene(scene);
                } else {
                    JoinMeetController controller = loader.getController();
                    controller.setReturnScene(scene);
                }
                scene.setRoot(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showErrorInContentArea(fxmlFile + " yüklenemedi.");
        }
    }

    private void loadView(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(MainApp.class.getResource("view/" + fxmlFile)));
            contentArea.setContent(root);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            showErrorInContentArea("Görünüm yüklenemedi: " + fxmlFile);
        }
    }

    private void showErrorInContentArea(String message) { Label errorLabel = new Label(message + "\nLütfen dosya yolunu ve içeriğini kontrol edin."); errorLabel.setStyle("-fx-text-fill: #ef4444; -fx-font-size: 16px; -fx-alignment: center;"); errorLabel.setWrapText(true); VBox errorBox = new VBox(errorLabel); errorBox.setAlignment(Pos.CENTER); contentArea.setContent(errorBox); }
    private void setActiveButton(JFXButton activeButton) { clearActiveButton(); activeButton.getStyleClass().add("active"); }
    private void clearActiveButton() { navBox.getChildren().forEach(node -> node.getStyleClass().remove("active")); }
}
