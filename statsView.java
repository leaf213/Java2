import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.ArrayList;

public class statsView {
    public static VBox createStatsView(bookManager bookManager, String btnStyle, String btnHoverStyle) {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(950);
        container.setMaxHeight(650);
        container.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 15px; -fx-padding: 25px;");

        Text title = new Text("My Borrowing Record");
        title.setFill(Color.valueOf("#2d3748"));
        title.setFont(Font.font("Courier New", FontWeight.BOLD, 25));

        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(30);
        statsGrid.setVgap(15);
        statsGrid.setAlignment(Pos.CENTER);

        Label lblUserTitle = new Label("Current User:");
        lblUserTitle.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 16px; -fx-font-weight: bold;");
        Label lblUserVal = new Label("Not logged in");
        lblUserVal.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 16px; -fx-text-fill: #3498db; -fx-font-weight: bold;");

        Label lblCountTitle = new Label("Borrow Records:");
        lblCountTitle.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 16px; -fx-font-weight: bold;");
        Label lblCountVal = new Label("0");
        lblCountVal.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 16px; -fx-text-fill: #2ecc71; -fx-font-weight: bold;");

        statsGrid.add(lblUserTitle, 0, 0);
        statsGrid.add(lblUserVal, 1, 0);
        statsGrid.add(lblCountTitle, 0, 1);
        statsGrid.add(lblCountVal, 1, 1);

        TableView<borrowedBook> table = new TableView<>();
        table.setPrefHeight(260);
        table.setStyle("-fx-font-family: 'Courier New';");

        TableColumn<borrowedBook, String> colTitle = new TableColumn<>("Title");
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colTitle.setPrefWidth(240);

        TableColumn<borrowedBook, String> colIsbn = new TableColumn<>("ISBN");
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colIsbn.setPrefWidth(150);

        TableColumn<borrowedBook, String> colBorrowDate = new TableColumn<>("Borrow Date");
        colBorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        colBorrowDate.setPrefWidth(150);

        TableColumn<borrowedBook, String> colDueDate = new TableColumn<>("Due Date");
        colDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        colDueDate.setPrefWidth(150);

        TableColumn<borrowedBook, Integer> colDays = new TableColumn<>("Days");
        colDays.setCellValueFactory(new PropertyValueFactory<>("borrowDays"));
        colDays.setPrefWidth(80);

        table.getColumns().addAll(colTitle, colIsbn, colBorrowDate, colDueDate, colDays);

        Label customPlaceholder = new Label("No borrowing history yet");
        customPlaceholder.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 14px; -fx-text-fill: #718096; -fx-font-weight: bold;");
        table.setPlaceholder(customPlaceholder);

        Button btnRefresh = new Button("Refresh History");
        btnRefresh.setStyle(btnStyle);
        btnRefresh.setOnMouseEntered(e -> btnRefresh.setStyle(btnHoverStyle));
        btnRefresh.setOnMouseExited(e -> btnRefresh.setStyle(btnStyle));

        Runnable loadHistory = () -> {
            String currentUser = project.loggedInUser == null ? "" : project.loggedInUser.trim();
            if (currentUser.isEmpty()) {
                lblUserVal.setText("Not logged in");
                lblCountVal.setText("0");
                table.getItems().clear();
                table.setPlaceholder(new Label("Please log in from My Account first."));
                return;
            }

            lblUserVal.setText(currentUser);

            borrowManager bm = new borrowManager();
            ArrayList<borrowedBook> userBorrows = new ArrayList<>();
            for (borrowedBook bb : bm.borrowedBooks) {
                if (bb.getBorrowerName() != null && bb.getBorrowerName().trim().equalsIgnoreCase(currentUser)) {
                    userBorrows.add(bb);
                }
            }

            table.getItems().setAll(userBorrows);
            lblCountVal.setText(String.valueOf(userBorrows.size()));

            if (userBorrows.isEmpty()) {
                table.setPlaceholder(new Label("No borrowing history found for " + currentUser));
            } else {
                table.setPlaceholder(new Label(""));
            }
        };

        btnRefresh.setOnAction(e -> loadHistory.run());
        loadHistory.run();

        container.getChildren().addAll(title, statsGrid, table, btnRefresh);
        return container;
    }
}
