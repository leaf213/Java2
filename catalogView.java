import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class catalogView {
    public static ScrollPane createView(bookManager bookManager) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        //title text
        Text title = new Text("Book Catelog & Inventory");
        title.setFill(Color.WHITE);
        title.setFont(Font.font("Courier New", FontWeight.BOLD, 25));

        //search field
        TextField searchField = new TextField();
        searchField.setPromptText("Search by title or author...");
        searchField.setMaxWidth(850);
        searchField.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 16px; -fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 8px; -fx-padding: 18px;");

        //show each book card
        VBox catalogBox = new VBox(20);
        catalogBox.setAlignment(Pos.TOP_CENTER);
        catalogBox.setPadding(new Insets(20));

        //put the book card
        VBox cardsContainer = new VBox(15);
        cardsContainer.setAlignment(Pos.TOP_CENTER);

        //filter book or author
        Runnable renderCatalog = () -> {
            cardsContainer.getChildren().clear();
            String keyword = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();

            //filter the condition book
            ArrayList<Book> filteredList = new ArrayList<>();
            for (Book b : bookManager.bookList) {
                if (keyword.isEmpty() ||
                    b.getTitle().toLowerCase().contains(keyword) ||
                    b.getAuthor().toLowerCase().contains(keyword)) {
                    filteredList.add(b);
                }
            }

            if (filteredList.isEmpty()) {
                Text emptyText = new Text("No matching books found.");
                emptyText.setFill(Color.LIGHTGRAY);
                emptyText.setFont(Font.font("Courier New", FontWeight.BOLD, 18));
                cardsContainer.getChildren().add(emptyText);
            } else {
                for (Book b : filteredList) {
                    //craete bookcard
                    HBox bookCard = new HBox(20);
                    bookCard.setAlignment(Pos.CENTER_LEFT);
                    bookCard.setPadding(new Insets(15));
                    bookCard.setStyle("-fx-background-color: rgba(255, 255, 255, 0.85); -fx-background-radius: 10px;");
                    bookCard.setMaxWidth(850);

                    //book image placeholder
                    ImageView bookImageView = new ImageView();
                    try {
                        bookImageView.setImage(new Image("bookCover.png"));
                    } catch (Exception e) {
                    }

                    bookImageView.setFitWidth(50);
                    bookImageView.setFitHeight(60);
                    bookImageView.setPreserveRatio(true);

                    // center content part
                    VBox infoBox = new VBox(5);

                    // book title text
                    Text bookTitle = new Text(b.getTitle());
                    bookTitle.setFont(Font.font("Courier New", FontWeight.BOLD, 18));
                    bookTitle.setFill(Color.valueOf("#506377"));

                    // author text
                    Text bookAuthor = new Text("Author: " + b.getAuthor());
                    bookAuthor.setFont(Font.font("Courier New", 14));
                    bookAuthor.setFill(Color.valueOf("#506377"));

                    // category text
                    Text bookCategory = new Text("Category: " + b.getCategory());
                    bookCategory.setFont(Font.font("Courier New", 14));
                    bookCategory.setFill(Color.valueOf("#506377"));

                    infoBox.getChildren().addAll(bookTitle, bookAuthor, bookCategory);

                    // separate spacer
                    Region cardSpacer = new Region();
                    HBox.setHgrow(cardSpacer, Priority.ALWAYS);

                    // right content part
                    VBox rightBox = new VBox(5);
                    rightBox.setAlignment(Pos.CENTER_RIGHT);

                    // quantity text
                    Text bookQty = new Text("Stock Qty: " + b.getQuantity());
                    bookQty.setFont(Font.font("Courier New", FontWeight.BOLD, 14));
                    bookQty.setFill(Color.valueOf("#506377"));

                    // isbn text
                    Text bookIsbn = new Text("ISBN: " + b.getIsbn());
                    bookIsbn.setFont(Font.font("Courier New", 14));
                    bookIsbn.setFill(Color.valueOf("#506377"));

                    rightBox.getChildren().addAll(bookQty, bookIsbn);

                    // combine card content
                    bookCard.getChildren().addAll(bookImageView, infoBox, cardSpacer, rightBox);
                    cardsContainer.getChildren().add(bookCard);
                }
            }
        };

        //search changing monitor
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            renderCatalog.run();
        });

        renderCatalog.run();

        //combine
        catalogBox.getChildren().addAll(title, searchField, cardsContainer);
        scrollPane.setContent(catalogBox);
        return scrollPane;        
    }
}