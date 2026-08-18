
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import src.ClassFolder.Member;
import ManageData.memberDataFile;

import java.time.LocalDate;

import BorrowBook.bookManager;

public class memberView {

    public static ScrollPane createView(bookManager bookManager, String btnStyle, String btnHoverStyle) {
        // explicit -fx-text-fill keeps the labels readable (dark text) even
        // inside the ScrollPane, where the theme's derived text color can
        // otherwise resolve to white.
        String labelStyle = "-fx-font-family: 'Courier New'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #2d3748;";
        String fldStyle = "-fx-font-family: 'Courier New'; -fx-font-size: 14px; -fx-background-color: #f8fafc; -fx-background-radius: 8px; -fx-border-color: #cbd5e0; -fx-border-radius: 8px; -fx-padding: 8px;";
        String feedbackStyle = "-fx-font-family: 'Courier New'; -fx-font-size: 14px; -fx-background-color: #f8fafc; -fx-background-radius: 8px; -fx-border-color: #cbd5e0; -fx-border-radius: 8px;";

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // outer centering box (the ScrollPane needs exactly one child)
        VBox outer = new VBox();
        outer.setAlignment(Pos.CENTER);
        outer.setFillWidth(false);
        outer.setPadding(new Insets(30));

        // main account card
        VBox card = new VBox(25);
        card.setAlignment(Pos.CENTER);
        card.setMinWidth(650);
        card.setMaxWidth(900);
        card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.92); -fx-background-radius: 15px; -fx-padding: 20px;");

        Text title = new Text("My Library Account");
        title.setFill(Color.valueOf("#2d3748"));
        title.setFont(Font.font("Courier New", FontWeight.BOLD, 25));

        // --- account form ---
        Label lblName = new Label("Your Name:");
        lblName.setStyle(labelStyle);
        TextField txtName = new TextField();
        txtName.setPromptText("Enter your name...");
        txtName.setPrefWidth(450);
        txtName.setStyle(fldStyle);

        Label lblPhone = new Label("Phone (optional):");
        lblPhone.setStyle(labelStyle);
        TextField txtPhone = new TextField();
        txtPhone.setPromptText("Phone number");
        txtPhone.setPrefWidth(450);
        txtPhone.setStyle(fldStyle);

        Label lblEmail = new Label("Email (optional):");
        lblEmail.setStyle(labelStyle);
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email address");
        txtEmail.setPrefWidth(450);
        txtEmail.setStyle(fldStyle);

        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);
        grid.add(lblName, 0, 0);  grid.add(txtName, 1, 0);
        grid.add(lblPhone, 0, 1); grid.add(txtPhone, 1, 1);
        grid.add(lblEmail, 0, 2); grid.add(txtEmail, 1, 2);

        // --- buttons ---
        Button btnLogin = new Button("Login / Register");
        Button btnClear = new Button("Clear Form");

        btnLogin.setStyle(btnStyle);
        btnLogin.setOnMouseEntered(e -> btnLogin.setStyle(btnHoverStyle));
        btnLogin.setOnMouseExited(e -> btnLogin.setStyle(btnStyle));

        btnClear.setStyle(btnStyle);
        btnClear.setOnMouseEntered(e -> btnClear.setStyle(btnHoverStyle));
        btnClear.setOnMouseExited(e -> btnClear.setStyle(btnStyle));

        HBox btnBox = new HBox(25);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().addAll(btnLogin, btnClear);

        // --- feedback ---
        TextArea txtFeedback = new TextArea();
        txtFeedback.setMaxWidth(680);
        txtFeedback.setMaxHeight(80);
        txtFeedback.setEditable(false);
        txtFeedback.setWrapText(true);
        txtFeedback.setStyle(feedbackStyle);
        txtFeedback.setPromptText("System feedback will appear here...");

        btnLogin.setOnAction(e -> {
            String name = txtName.getText().trim();
            Project.loggedInUser = name;
            if (name.isEmpty()) {
                txtFeedback.setStyle(feedbackStyle + " -fx-text-fill: red;");
                txtFeedback.setText("Error: Please enter your name!");
                return;
            }

            Project.loggedInUser = name;

            Member[] members = memberDataFile.loadMembers();
            Member found = null;
            for (Member m : members) {
                if (m != null && m.getName().equalsIgnoreCase(name)) {
                    found = m;
                    break;
                }
            }

            if (found == null) {
                // not registered yet -> create a new member profile
                String newId = memberDataFile.nextMemberId(members);
                Member newMember = new Member(
                        newId, name,
                        txtPhone.getText().trim(),
                        txtEmail.getText().trim(),
                        LocalDate.now().toString());
                Member[] updated = memberDataFile.growMemberArray(members);
                updated[updated.length - 1] = newMember;
                memberDataFile.saveMembers(updated);

                txtFeedback.setStyle(feedbackStyle + " -fx-text-fill: green;");
                txtFeedback.setText("Welcome, " + name + "! (Member ID: " + newId + ")");
            } else {
                txtFeedback.setStyle(feedbackStyle + " -fx-text-fill: green;");
                txtFeedback.setText("Welcome back, " + found.getName() + "! (Member ID: " + found.getMemberId() + ")");
            }
        });

        btnClear.setOnAction(e -> {
            txtName.clear();
            txtPhone.clear();
            txtEmail.clear();
            txtFeedback.clear();
            Project.loggedInUser = "";
        });

        card.getChildren().addAll(title, grid, btnBox, txtFeedback);
        outer.getChildren().add(card);
        scrollPane.setContent(outer);
        return scrollPane;
    }
}
