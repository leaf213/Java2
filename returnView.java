import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.ArrayList;

public class returnView {
    //panadol
    @SuppressWarnings("unchecked")
    public static VBox createReturnView(bookManager bookMgr, String btnStyle, String btnHoverStyle) {
        VBox container = new VBox(15);
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(850);
        container.setMaxHeight(550);
        container.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 15px; -fx-padding: 20px;");

        Text title = new Text("Book Return");
        title.setFill(Color.valueOf("#2d3748"));
        title.setFont(Font.font("Courier New", FontWeight.BOLD, 25));

        String labelStyle = "-fx-font-family: 'Courier New'; -fx-font-size: 14px; -fx-font-weight: bold;";
        String fldStyle = "-fx-font-family: 'Courier New'; -fx-font-size: 14px;";

        TableView<BorrowedBook> table = new TableView<>();
        table.setPrefHeight(220);
        table.setStyle("-fx-font-family: 'Courier New';");

        TableColumn<BorrowedBook, String> colTitle = new TableColumn<>("Title");
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colTitle.setPrefWidth(220);

        TableColumn<BorrowedBook, String> colIsbn = new TableColumn<>("ISBN");
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colIsbn.setPrefWidth(120);

        // Fixed: Use capitalized property names to match JavaBean getter conventions
        // BorrowDate -> getBorrowDate()
        TableColumn<BorrowedBook, String> colBorrowDate = new TableColumn<>("Borrow Date");
        colBorrowDate.setCellValueFactory(new PropertyValueFactory<>("BorrowDate"));
        colBorrowDate.setPrefWidth(100);

        // DueDate -> getDueDate()
        TableColumn<BorrowedBook, String> colDueDate = new TableColumn<>("Due Date");
        colDueDate.setCellValueFactory(new PropertyValueFactory<>("DueDate"));
        colDueDate.setPrefWidth(100);

        // BorrowDays -> getBorrowDays()
        TableColumn<BorrowedBook, Integer> colDays = new TableColumn<>("Days");
        colDays.setCellValueFactory(new PropertyValueFactory<>("BorrowDays"));
        colDays.setPrefWidth(70);

        table.getColumns().addAll(colTitle, colIsbn, colBorrowDate, colDueDate, colDays);

        Label customPlaceholder = new Label("No content");
        customPlaceholder.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 14px; -fx-text-fill: #718096; -fx-font-weight: bold;");
        table.setPlaceholder(customPlaceholder);

        TextField txtIsbn = new TextField();
        txtIsbn.setPromptText("Click a book above...");
        txtIsbn.setStyle(fldStyle);
        txtIsbn.setEditable(true);

        table.setRowFactory(e -> {
            TableRow<BorrowedBook> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    BorrowedBook clickedBook = row.getItem();
                    txtIsbn.setText(clickedBook.getIsbn());
                }
            });
            return row;
        });

        if (!project.loggedInUser.isEmpty()) {
            borrowManager bm = new borrowManager();
            ArrayList<BorrowedBook> userActiveBorrows = new ArrayList<>();
            for (BorrowedBook bb : bm.borrowedBooks) {
                if (bb.getBorrowerName() != null && bb.getBorrowerName().trim().equalsIgnoreCase(project.loggedInUser)) {
                    userActiveBorrows.add(bb);
                }
            }
            table.getItems().setAll(userActiveBorrows);
        }

        HBox actionBox = new HBox(15);
        actionBox.setAlignment(Pos.CENTER);

        Label lblIsbn = new Label("Enter ISBN to Return:");
        lblIsbn.setStyle(labelStyle);
        txtIsbn.setPrefWidth(180);

        Button btnReturn = new Button("Confirm Return");
        Button btnClear = new Button("Clear");

        txtIsbn.setPromptText("Click a book above...");
        txtIsbn.setPrefWidth(180);
        txtIsbn.setStyle(fldStyle);

        btnReturn.setStyle(btnStyle);
        btnReturn.setOnMouseEntered(e -> btnReturn.setStyle(btnHoverStyle));
        btnReturn.setOnMouseExited(e -> btnReturn.setStyle(btnStyle));

        btnClear.setStyle(btnStyle);
        btnClear.setOnMouseEntered(e -> btnClear.setStyle(btnHoverStyle));
        btnClear.setOnMouseExited(e -> btnClear.setStyle(btnStyle));

        TextArea txtFeedback = new TextArea();
        txtFeedback.setMaxWidth(650);
        txtFeedback.setMaxHeight(60);
        txtFeedback.setEditable(false);
        txtFeedback.setWrapText(true);
        txtFeedback.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 14px;");
        txtFeedback.setPromptText("System feedback will appear here...");

        btnClear.setOnAction(e -> {
            txtIsbn.clear();
            txtFeedback.clear();
        });

        btnReturn.setOnAction(e -> {
            try {
                String rawIsbn = txtIsbn.getText().trim();
                if (rawIsbn.isEmpty()) {
                    throw new IllegalArgumentException("Error: ISBN field cannot be empty!");
                }
                String isbnInput = bookManager.sanitizeIsbn(rawIsbn);
                if (! bookManager.isValidIsbn(isbnInput)) {
                    throw new IllegalArgumentException("Error: ISBN must be a standard 10 or 13 digit number (hyphens and spaces are allowed).");
                }

                // only an ISBN with an active borrow record may be returned;
                // BookManager rejects phantom returns without touching inventory
                borrowManager bm = new borrowManager();

                String resultMsg = bm.returnBook(isbnInput, bookMgr);
                ArrayList<BorrowedBook> userActiveBorrows = new ArrayList<>();
                for (BorrowedBook bb : bm.borrowedBooks) {
                    if (bb.getBorrowerName() != null && bb.getBorrowerName().trim().equalsIgnoreCase(project.loggedInUser)) {
                        userActiveBorrows.add(bb);
                    }
                }
                table.getItems().setAll(userActiveBorrows);

                txtFeedback.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 14px; -fx-text-fill: green;");
                txtFeedback.setText(resultMsg);
                txtIsbn.clear();

            } catch (borrowException | IllegalArgumentException ex) {
                txtFeedback.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 14px; -fx-text-fill: red;");
                txtFeedback.setText(ex.getMessage());
            }
        });

        actionBox.getChildren().addAll(lblIsbn, txtIsbn, btnReturn, btnClear);
        container.getChildren().addAll(title, table, actionBox, txtFeedback);
        return container;
    }
}
