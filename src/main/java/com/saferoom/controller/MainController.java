package com.saferoom.controller;

import com.jfoenix.controls.JFXButton;
import com.saferoom.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.util.Objects;

public class MainController {

    private static MainController instance;

    @FXML private BorderPane mainPane;
    @FXML private Label viewTitleLabel;
    @FXML private ScrollPane contentArea;
    @FXML private VBox navBox;
    @FXML private VBox dashboardButton;
    @FXML private VBox roomsButton;
    @FXML private VBox messagesButton;
    @FXML private VBox friendsButton;
    @FXML private VBox fileVaultButton;
    @FXML private Pane userAvatarContainer;
    @FXML private Label userAvatar;
    @FXML private Pane statusDot;
    @FXML private HBox headerBar;
    @FXML private JFXButton minButton;
    @FXML private JFXButton maxButton;
    @FXML private JFXButton closeButton;
    
    // User status types
    public enum UserStatus {
        ONLINE("status-online"),
        IDLE("status-idle"),
        DND("status-dnd"),
        OFFLINE("status-offline");
        
        private final String styleClass;
        
        UserStatus(String styleClass) {
            this.styleClass = styleClass;
        }
        
        public String getStyleClass() {
            return styleClass;
        }
    }
    
    private UserStatus currentStatus = UserStatus.ONLINE;
    private double dragOffsetX;
    private double dragOffsetY;
    private ContextMenu userMenu;
    private List<MenuItem> mainMenuItems = new ArrayList<>();
    private List<MenuItem> statusMenuItems = new ArrayList<>();
    private boolean showingStatusSheet = false;
    private final String currentUserName = "Username"; // Replace when user model is available

    @FXML
    public void initialize() {
        instance = this;
        handleDashboard();
        
        // Set up click handlers for the new VBox buttons
        dashboardButton.setOnMouseClicked(event -> handleDashboard());
        roomsButton.setOnMouseClicked(event -> handleRooms());
        messagesButton.setOnMouseClicked(event -> handleMessages());
        friendsButton.setOnMouseClicked(event -> handleFriends());
        fileVaultButton.setOnMouseClicked(event -> handleFileVault());
        // settings button removed from sidebar; accessible via user menu
        
        // Initialize user status
        setUserStatus(UserStatus.ONLINE);
        
        // Initialize user avatar with first letter of username
        if (userAvatar != null) {
            userAvatar.setText("U"); // You can replace this with actual username first letter
        }

        // Build user menu and bind to avatar container
        buildUserContextMenu();
        if (userAvatarContainer != null) {
            userAvatarContainer.setOnMouseClicked(e -> {
                if (userMenu != null) {
                    if (userMenu.isShowing()) {
                        userMenu.hide();
                    } else {
                        userMenu.show(userAvatarContainer, Side.BOTTOM, 0, 8);
                    }
                }
            });
        }
        // no secondary popup; we switch sheets inside the same menu

        // Enable dragging the undecorated window by the header
        if (headerBar != null) {
            headerBar.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                Stage stage = (Stage) headerBar.getScene().getWindow();
                dragOffsetX = event.getScreenX() - stage.getX();
                dragOffsetY = event.getScreenY() - stage.getY();
            });
            headerBar.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
                Stage stage = (Stage) headerBar.getScene().getWindow();
                stage.setX(event.getScreenX() - dragOffsetX);
                stage.setY(event.getScreenY() - dragOffsetY);
            });
        }

        // Window control buttons
        if (minButton != null) {
            minButton.setOnMouseClicked(e -> {
                Stage stage = (Stage) mainPane.getScene().getWindow();
                stage.setIconified(true);
            });
        }
        if (maxButton != null) {
            maxButton.setOnMouseClicked(e -> {
                Stage stage = (Stage) mainPane.getScene().getWindow();
                stage.setMaximized(!stage.isMaximized());
            });
        }
        if (closeButton != null) {
            closeButton.setOnMouseClicked(e -> {
                Stage stage = (Stage) mainPane.getScene().getWindow();
                stage.close();
            });
        }

        // Removed rounded corners and shadow as requested
    }

    public static MainController getInstance() {
        return instance;
    }

    private void handleDashboard() { setActiveButton(dashboardButton); viewTitleLabel.setText("Dashboard"); loadView("DashboardView.fxml"); }
    public void handleRooms() { setActiveButton(roomsButton); viewTitleLabel.setText("Rooms"); loadView("RoomsView.fxml"); }
    private void handleMessages() { setActiveButton(messagesButton); viewTitleLabel.setText("Messages"); loadView("MessagesView.fxml"); }
    private void handleFriends() { setActiveButton(friendsButton); viewTitleLabel.setText("Friends"); loadView("FriendsView.fxml"); }
    public void handleFileVault() { setActiveButton(fileVaultButton); viewTitleLabel.setText("File Vault"); loadView("FileVaultView.fxml"); }
    private void handleSettings() { clearActiveButton(); viewTitleLabel.setText("Settings"); loadView("SettingsView.fxml"); }
    
    public void setUserStatus(UserStatus status) {
        if (statusDot != null) {
            // Remove all status classes
            statusDot.getStyleClass().removeAll(
                UserStatus.ONLINE.getStyleClass(),
                UserStatus.IDLE.getStyleClass(),
                UserStatus.DND.getStyleClass(),
                UserStatus.OFFLINE.getStyleClass()
            );
            // Add the new status class
            statusDot.getStyleClass().add(status.getStyleClass());
            currentStatus = status;
            // Refresh user menu selection visuals if open
            boolean reopenUser = userMenu != null && userMenu.isShowing();
            if (userMenu != null) userMenu.hide();
            buildUserContextMenu();
            if (reopenUser && userAvatarContainer != null) {
                userMenu.show(userAvatarContainer, Side.BOTTOM, 0, 8);
                // restore sheet view
                userMenu.getItems().setAll(showingStatusSheet ? statusMenuItems : mainMenuItems);
            }
        }
    }
    
    public UserStatus getUserStatus() {
        return currentStatus;
    }

    private void buildUserContextMenu() {
        userMenu = new ContextMenu();
        userMenu.getStyleClass().add("user-menu");
        mainMenuItems.clear();
        statusMenuItems.clear();
        showingStatusSheet = false;

        // Header with avatar and name
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label avatar = new Label("U");
        avatar.getStyleClass().add("user-avatar-header");
        Label nameLabel = new Label(currentUserName);
        nameLabel.getStyleClass().add("user-name");
        header.getChildren().addAll(avatar, nameLabel);
        CustomMenuItem headerItem = new CustomMenuItem(header, false);

        // Status main row that opens a popup selection
        HBox statusMainRow = new HBox(10);
        statusMainRow.setAlignment(Pos.CENTER_LEFT);
        statusMainRow.getStyleClass().add("user-menu-item");
        Pane currentDot = new Pane();
        currentDot.getStyleClass().addAll("status-dot-menu", currentStatus.getStyleClass());
        Label statusLabel = new Label("Status");
        statusLabel.getStyleClass().add("user-menu-text");
        Pane grow1 = new Pane();
        HBox.setHgrow(grow1, javafx.scene.layout.Priority.ALWAYS);
        FontIcon arrow = new FontIcon("fas-chevron-right");
        arrow.getStyleClass().add("user-menu-arrow");
        statusMainRow.getChildren().addAll(currentDot, statusLabel, grow1, arrow);
        CustomMenuItem statusOpenItem = new CustomMenuItem(statusMainRow, false);
        statusMainRow.setOnMouseClicked(e -> {
            userMenu.getItems().setAll(statusMenuItems);
            showingStatusSheet = true;
        });

        // Other actions
        CustomMenuItem settingsItem = actionRow("Settings", "fas-cog", this::handleSettings);
        CustomMenuItem helpItem = actionRow("Help", "far-question-circle", () -> new Alert(AlertType.INFORMATION, "Help is coming soon.").showAndWait());

        mainMenuItems.add(headerItem);
        mainMenuItems.add(new SeparatorMenuItem());
        mainMenuItems.add(statusOpenItem);
        mainMenuItems.add(new SeparatorMenuItem());
        mainMenuItems.add(settingsItem);
        mainMenuItems.add(helpItem);
        userMenu.getItems().setAll(mainMenuItems);

        // Build status sheet (with Back)
        CustomMenuItem backItem = actionRow("Back", "fas-chevron-left", () -> {
            userMenu.getItems().setAll(mainMenuItems);
            showingStatusSheet = false;
        });
        statusMenuItems.add(backItem);
        statusMenuItems.add(new SeparatorMenuItem());
        statusMenuItems.add(statusRow("Online", UserStatus.ONLINE));
        statusMenuItems.add(statusRow("Idle", UserStatus.IDLE));
        statusMenuItems.add(statusRow("Do Not Disturb", UserStatus.DND));
        statusMenuItems.add(statusRow("Offline", UserStatus.OFFLINE));
    }

    private CustomMenuItem actionRow(String text, String iconLiteral, Runnable action) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("user-menu-item");
        FontIcon icon = new FontIcon(iconLiteral);
        icon.getStyleClass().add("user-menu-icon");
        Label label = new Label(text);
        label.getStyleClass().add("user-menu-text");
        row.getChildren().addAll(icon, label);
        row.setOnMouseClicked(e -> action.run());
        return new CustomMenuItem(row, false);
    }

    private CustomMenuItem statusRow(String text, UserStatus status) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("user-menu-item");
        Pane dot = new Pane();
        dot.getStyleClass().addAll("status-dot-menu", status.getStyleClass());
        Label label = new Label(text);
        label.getStyleClass().add("user-menu-text");
        FontIcon check = new FontIcon("fas-check");
        check.getStyleClass().add("check-icon");
        check.setVisible(currentStatus == status);
        row.getChildren().addAll(dot, label, new Pane());
        // keep check mark aligned right
        Pane grow = new Pane();
        HBox.setHgrow(grow, javafx.scene.layout.Priority.ALWAYS);
        row.getChildren().set(2, grow);
        row.getChildren().add(check);
        row.setOnMouseClicked(e -> setUserStatus(status));
        return new CustomMenuItem(row, false);
    }

    // removed secondary popup; we switch the content of the same menu instead

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
    private void setActiveButton(VBox activeButton) { clearActiveButton(); activeButton.getStyleClass().add("active"); }
    private void clearActiveButton() { navBox.getChildren().forEach(node -> node.getStyleClass().remove("active")); }
}
