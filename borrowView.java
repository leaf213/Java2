
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.time.LocalDate;

import src.ClassFolder.Book;
import BorrowBook.bookManager;
import BorrowBook.borrowException;
import BorrowBook.borrowManager;

public class borrowView {
    public static VBox createView(bookManager bookManager, String btnStyle, String btnHoverStyle) {
        VBox container = new VBox(15);
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(700);
        container.setMaxHeight(580);
        container.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 25px; -fx-padding: 20px;");

        Text title = new Text("Borrow Book Form");
        title.setFill(Color.valueOf("#2d3748"));
        title.setFont(Font.font("Courier New", FontWeight.BOLD, 25));

        // style
        String labelStyle = "-fx-font-family: 'Courier New'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2d3748;";
        String fldStyle = "-fx-font-family: 'Courier New'; -fx-font-size: 16px;";
        String feedbackStyle = "-fx-font-family: 'Courier New'; -fx-font-size: 16px;";

        //label book
        Label lblBook = new Label("Select Book:");
        lblBook.setStyle(labelStyle);

        ComboBox<Book> cmbBook = new ComboBox<>();
        cmbBook.setPromptText("SELECT A BOOK");
        cmbBook.setPrefWidth(400);
        cmbBook.setStyle(fldStyle);
        cmbBook.getItems().addAll(bookManager.bookList);

        cmbBook.setButtonCell(bookCell());
        cmbBook.setCellFactory(param -> bookCell());

        //label isbn
        Label lblIsbn = new Label("ISBN:");
        lblIsbn.setStyle(labelStyle);
        TextField txtIsbn = new TextField();
        txtIsbn.setEditable(false);
        txtIsbn.setPrefWidth(320);
        txtIsbn.setStyle(fldStyle);

        Label lblStock = new Label("Stock:");
        lblStock.setStyle(labelStyle);
        TextField txtStock = new TextField();
        txtStock.setEditable(false);
        txtStock.setPrefWidth(320);
        txtStock.setStyle(fldStyle);

        //label name
        Label lblName = new Label("Borrower Name:");
        lblName.setStyle(labelStyle);
        TextField txtName = new TextField();
        txtName.setPromptText("Enter your full name...");
        txtName.setPrefWidth(320);
        txtName.setStyle(fldStyle);

        //label days
        Label lblDays = new Label("Borrow Duration (days):");
        lblDays.setStyle(labelStyle);
        TextField txtDays = new TextField("7");
        txtDays.setPrefWidth(320);
        txtDays.setStyle(fldStyle);

        // when the user picks a book, fill in its ISBN and available stock
        cmbBook.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                txtIsbn.clear();
                txtStock.clear();
            } else {
                txtIsbn.setText(newVal.getIsbn());
                txtStock.setText(String.valueOf(newVal.getQuantity()));
            }
        });

        //button borrow
        Button btnBorrow = new Button("Confirm Borrow");
        //button clear
        Button btnClear = new Button("Clear Form");

        btnBorrow.setStyle(btnStyle);
        btnBorrow.setOnMouseEntered(e -> btnBorrow.setStyle(btnHoverStyle));
        btnBorrow.setOnMouseExited(e -> btnBorrow.setStyle(btnStyle));

        btnClear.setStyle(btnStyle);
        btnClear.setOnMouseEntered(e -> btnClear.setStyle(btnHoverStyle));
        btnClear.setOnMouseExited(e -> btnClear.setStyle(btnStyle));

        //textarea feedback
        TextArea txtFeedback = new TextArea();
        txtFeedback.setMaxWidth(540);
        txtFeedback.setMaxHeight(70);
        txtFeedback.setEditable(false);
        txtFeedback.setWrapText(true);
        txtFeedback.setStyle(feedbackStyle);
        txtFeedback.setPromptText("System feedback will appear here...");

        //status
        btnClear.setOnAction(e -> {
            cmbBook.setValue(null);
            txtIsbn.clear();
            txtStock.clear();
            txtName.clear();
            txtDays.setText("7");
            txtFeedback.clear();
        });

        btnBorrow.setOnAction(e -> {
            Book selected = cmbBook.getValue();
            String nameInput = txtName.getText().trim();
            String daysText = txtDays.getText().trim();

            if (selected == null) {
                txtFeedback.setStyle(feedbackStyle + " -fx-text-fill: red;");
                txtFeedback.setText("Error: Please select a book from the list!");
                return;
            }
            if (nameInput.isEmpty()) {
                txtFeedback.setStyle(feedbackStyle + " -fx-text-fill: red;");
                txtFeedback.setText("Error: Please enter the borrower's full name!");
                return;
            }

            int days;
            try {
                days = Integer.parseInt(daysText);
            } catch (NumberFormatException ex) {
                txtFeedback.setStyle(feedbackStyle + " -fx-text-fill: red;");
                txtFeedback.setText("Error: Borrow duration must be a whole number between 1 and 7!");
                return;
            }

            try {
                // borrowBook() validates stock/duration, updates both ArrayLists
                // and persists both CSV files (books + borrows)
                borrowManager bm = new borrowManager();

                String resultMsg = bm.borrowBook(selected.getIsbn(), nameInput, days, bookManager);

                String dueDate = LocalDate.now().plusDays(days).toString();
                int remaining = selected.getQuantity();   // already reduced by borrowBook()

                txtFeedback.setStyle(feedbackStyle + " -fx-text-fill: green;");
                txtFeedback.setText(resultMsg + "\nDue date: " + dueDate + "\nRemaining stock: " + remaining);

                // refresh the ComboBox/Stock fields with the updated inventory
                cmbBook.getItems().setAll(bookManager.bookList);
                cmbBook.setValue(selected);
                txtIsbn.setText(selected.getIsbn());
                txtStock.setText(String.valueOf(remaining));
                txtName.clear();
                txtDays.setText("7");

            } catch (borrowException ex) {
                txtFeedback.setStyle(feedbackStyle + " -fx-text-fill: red;");
                txtFeedback.setText(ex.getMessage());
            } catch (IllegalArgumentException ex) {
                txtFeedback.setStyle(feedbackStyle + " -fx-text-fill: red;");
                txtFeedback.setText(ex.getMessage());
            }
        });

        // --- assemble the form ---
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);
        grid.setAlignment(Pos.CENTER);
        grid.add(lblBook, 0, 0);  grid.add(cmbBook, 1, 0);
        grid.add(lblIsbn, 0, 1);  grid.add(txtIsbn, 1, 1);
        grid.add(lblStock, 0, 2); grid.add(txtStock, 1, 2);
        grid.add(lblName, 0, 3);  grid.add(txtName, 1, 3);
        grid.add(lblDays, 0, 4);  grid.add(txtDays, 1, 4);

        HBox btnBox = new HBox(20);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().addAll(btnBorrow, btnClear);

        container.getChildren().addAll(title, grid, btnBox, txtFeedback);
        return container;
    }

    /** Displays "Title - Author (ISBN: xxx)" for each book in the ComboBox. */
    private static ListCell<Book> bookCell() {
        return new ListCell<Book>() {
            @Override
            protected void updateItem(Book book, boolean empty) {
                super.updateItem(book, empty);
                if (empty || book == null) {
                    setText(null);
                } else {
                    setText(book.getTitle() + " - " + book.getAuthor() + " (ISBN: " + book.getIsbn() + ")");
                }
            }
        };
    }
}
