
import BorrowBook.bookManager;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class donateView {
    public static VBox craeteView(bookManager bookMgr, String btnStyle, String btnHoverStyle) {
        VBox formContainer = new VBox(15);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setMaxWidth(550);
        formContainer.setMaxHeight(500);
        formContainer.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 15px; -fx-padding: 20px;");

        //donate title text
        Text donateTitle = new Text("Donate New Book Form");
        donateTitle.setFill(Color.valueOf("#2d3748"));
        donateTitle.setFont(Font.font("Courier New", FontWeight.BOLD, 25));

        //form grid layout
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        //label style
        String labelStyle = "-fx-font-family: 'Courier New'; -fx-font-size: 16px; -fx-font-weight: bold;";
            
        //field style
        String fldStyle = "-fx-font-family: 'Courier New'; -fx-font-size: 16px;";

        //title part
        Label lblTitle = new Label("Book Title:");
        lblTitle.setStyle(labelStyle);
        TextField txtTitle = new TextField();
        txtTitle.setStyle(fldStyle);

        //author part
        Label lblAuthor = new Label("Author:");
        lblAuthor.setStyle(labelStyle);
        TextField txtAuthor = new TextField();
        txtAuthor.setStyle(fldStyle);

        //ISBN part
        Label lblISBN = new Label("ISBN:");
        lblISBN.setStyle(labelStyle);
        TextField txtISBN = new TextField();
        txtISBN.setPromptText("Numbers only...");
        txtISBN.setStyle(fldStyle);

        //category part
        Label lblCategory = new Label("Category:");
        lblCategory.setStyle(labelStyle);
        ComboBox<String> cmbCategory = new ComboBox<>();
        cmbCategory.getItems().addAll("Fantasy", "Science Fiction", "Mystery", "Horror", "History", "Story", "Literature");
        cmbCategory.setPromptText("SELECT CATEGORY");
        cmbCategory.setStyle(fldStyle);

        //combine form content
        grid.add(lblTitle, 0, 0); grid.add(txtTitle, 1, 0);
        grid.add(lblAuthor, 0, 1); grid.add(txtAuthor, 1, 1);
        grid.add(lblISBN, 0, 2); grid.add(txtISBN, 1, 2);
        grid.add(lblCategory, 0, 3); grid.add(cmbCategory, 1, 3);

        //feedback text area
        TextArea txtFeedback = new TextArea();
        txtFeedback.setMaxWidth(450);
        txtFeedback.setMaxHeight(70);
        txtFeedback.setEditable(false);
        txtFeedback.setWrapText(true);
        txtFeedback.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 16px;");
        txtFeedback.setPromptText("System feedback will appear here...");

        //button box part
        HBox btnBox = new HBox(20);
        btnBox.setAlignment(Pos.CENTER);

        //button add
        Button btnAdd = new Button("Confirm Add");

        //button clear
        Button btnClear = new Button("Clear Form");

        //donation button part style
        Button [] DonationButtons = {btnAdd, btnClear};
        for (Button btn : DonationButtons) {
            btn.setStyle(btnStyle);
            btn.setOnMouseEntered(event -> btn.setStyle(btnHoverStyle));
            btn.setOnMouseExited(event -> btn.setStyle(btnStyle));
        }

        //button clear status
        btnClear.setOnAction(event -> {
            txtTitle.clear();
            txtAuthor.clear();
            txtISBN.clear();
            cmbCategory.setValue(null);
            txtFeedback.clear();
        });

        //button add status
        btnAdd.setOnAction(event -> {
            if (txtTitle.getText().isBlank() || txtAuthor.getText().isBlank() || 
                txtISBN.getText().isBlank() || cmbCategory.getValue() == null) {
                txtFeedback.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 16px; -fx-text-fill: red;");
                txtFeedback.setText("Error: Please fill in all text fields and select a category!");
                return;
            }

            //clear isbn front and back spaces and normalize hyphens/spaces
            String rawIsbn = txtISBN.getText().trim();
            if (rawIsbn.isEmpty()) {
                txtFeedback.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 16px; -fx-text-fill: red;");
                txtFeedback.setText("Error: ISBN cannot be empty!");
                return;
            }
            String isbnText = bookManager.sanitizeIsbn(rawIsbn);

            //check the isbn is a standard 10 or 13 digit number
            if (! bookManager.isValidIsbn(isbnText)) {
                txtFeedback.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 16px; -fx-text-fill: red;");
                txtFeedback.setText("Error: ISBN must be a standard 10 or 13 digit number (hyphens and spaces are allowed).");
                return;
            }

            //catch the content
            String titleStr = txtTitle.getText().trim();
            String authorStr = txtAuthor.getText().trim();
            String categoryStr = cmbCategory.getValue();

            //send the content (addOrUpdateBook persists the updated list)
            String resultMsg = bookMgr.addOrUpdateBook(titleStr, authorStr, isbnText, categoryStr);

            //feedback part
            txtFeedback.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 16px; -fx-text-fill: green;");
            txtFeedback.setText(resultMsg);

            //clear after success
            txtTitle.clear();
            txtAuthor.clear();
            txtISBN.clear();
            cmbCategory.setValue(null);
        });

        //combine all donation part
        btnBox.getChildren().addAll(btnAdd, btnClear);
        formContainer.getChildren().addAll(donateTitle, grid, btnBox, txtFeedback);
        return formContainer;
    }
}
