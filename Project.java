
import javafx.application.Application;
import javafx.stage.Stage;
import BorrowBook.bookManager;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Button;

public class Project extends Application {
    private bookManager bookManager = new bookManager();

    public static String loggedInUser = "";

    @Override
    public void start(Stage primaryStage) throws Exception {
        //main menu
        //background part
        //background image
        Image imagebgd = new Image("src/img/background.jpg");
        ImageView imageView = new ImageView(imagebgd);
        imageView.setPreserveRatio(false);

        //background layout shape
        Rectangle rectanglebgd = new Rectangle(1000, 700);
        rectanglebgd.setFill(Color.rgb(0, 0, 0, 0.5));

        StackPane rootLayout = new StackPane();

        //background scale window size
        imageView.fitWidthProperty().bind(rootLayout.widthProperty());
        imageView.fitHeightProperty().bind(rootLayout.heightProperty());

        rectanglebgd.widthProperty().bind(rootLayout.widthProperty());
        rectanglebgd.heightProperty().bind(rootLayout.heightProperty());

        //topbar part
        //topbar layout
        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(15, 25, 15, 25));
        topBar.setPrefHeight(90);
        topBar.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7);");

        //book icon image
        Image bookIcon = new Image("src/img/book.png");
        ImageView bookIconView = new ImageView(bookIcon);
        bookIconView.setFitWidth(45);
        bookIconView.setFitHeight(45);
        bookIconView.setPreserveRatio(true);

        //textSystemMenu
        Text textSystemMenu = new Text("System Menu --");
        textSystemMenu.setFill(Color.BLACK);
        textSystemMenu.setFont(Font.font("Courier New", FontWeight.BOLD, 20));
        textSystemMenu.setStyle("-fx-cursor: hand;");
        textSystemMenu.setOnMouseEntered(e -> textSystemMenu.setFill(Color.valueOf("#4d6177")));
        textSystemMenu.setOnMouseExited(e -> textSystemMenu.setFill(Color.BLACK));

        //make the text and image move to left and right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        //button style
        String btnstyle = "-fx-background-color: #4d6177;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Courier New';" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 15px;" +
                "-fx-background-radius: 8px;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 8 20 8 20;";

        String btnHoverStyle = "-fx-background-color: #7b90a4;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Courier New';" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 15px;" +
                "-fx-background-radius: 8px;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 8 20 8 20;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 3);";

        //button catalog
        Button buttonCatalog = new Button("Catalog");

        //button donate
        Button buttonDonate = new Button("Donate");

        //button borrow
        Button buttonBorrow = new Button("Borrow");

        //button return
        Button buttonReturn = new Button("Return");

        //button dashboard
        Button buttonDashboard = new Button("Dashboard");

        //button my account
        Button buttonMyAccount = new Button("My Account");

        //button quit
        Button buttonQuit = new Button("Quit");

        //both button style
        Button[] menuButtons = {buttonCatalog, buttonDonate, buttonBorrow, buttonReturn, buttonDashboard, buttonMyAccount, buttonQuit};
        for (Button btn : menuButtons) {
            btn.setStyle(btnstyle);
            btn.setOnMouseEntered(e -> btn.setStyle(btnHoverStyle));
            btn.setOnMouseExited(e -> btn.setStyle(btnstyle));
        }

        //menu content
        StackPane centerContentArea = new StackPane();
        centerContentArea.setPadding(new Insets(20));

        //prompt text
        Text welcomeText = new Text("Welcome to Book Donation & Management System \nPlease select a menu above.");
        welcomeText.setFill(Color.WHITE);
        welcomeText.setFont(Font.font("Courier New", FontWeight.BOLD, 30));
        centerContentArea.getChildren().addAll(welcomeText);

        //menu text click events
        textSystemMenu.setOnMouseClicked(e -> {
            centerContentArea.getChildren().clear();
            centerContentArea.getChildren().add(welcomeText);
        });

        //main menu pane
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topBar);
        mainLayout.setCenter(centerContentArea);
        rootLayout.getChildren().addAll(imageView, rectanglebgd, mainLayout);

        //catalog part
        buttonCatalog.setOnAction(e -> {
            centerContentArea.getChildren().clear();
            centerContentArea.getChildren().addAll(catalogView.createView(bookManager));
        });

        //donation part
        buttonDonate.setOnAction(e -> {
            centerContentArea.getChildren().clear();
            centerContentArea.getChildren().addAll(donateView.craeteView(bookManager, btnstyle, btnHoverStyle));
        });

        //borrow part
        buttonBorrow.setOnAction(e -> {
            centerContentArea.getChildren().clear();
            centerContentArea.getChildren().addAll(borrowView.createView(bookManager, btnstyle, btnHoverStyle));
        });

        //return part
        buttonReturn.setOnAction(e -> {
            centerContentArea.getChildren().clear();
            centerContentArea.getChildren().addAll(returnView.createReturnView(bookManager, btnstyle, btnHoverStyle));
        });

        //dashboard / stats part
        buttonDashboard.setOnAction(e -> {
            centerContentArea.getChildren().clear();
            centerContentArea.getChildren().addAll(statsView.createStatsView(bookManager, btnstyle, btnHoverStyle));
        });

        //my account part
        buttonMyAccount.setOnAction(e -> {
            centerContentArea.getChildren().clear();
            try {
                centerContentArea.getChildren().addAll(memberView.createView(bookManager, btnstyle, btnHoverStyle));
            } catch (Exception ex) {
                ex.printStackTrace();
                Text errorText = new Text("Error opening My Account: " + ex.getMessage());
                errorText.setFill(Color.RED);
                errorText.setFont(Font.font("Courier New", FontWeight.BOLD, 16));
                centerContentArea.getChildren().add(errorText);
            }
        });

        //quit part
        buttonQuit.setOnAction(e -> {
            quitAction.handleQuit();
        });

        //combine topbar button
        topBar.getChildren().addAll(bookIconView, textSystemMenu, spacer, buttonCatalog, buttonDonate, buttonBorrow, buttonReturn, buttonDashboard, buttonMyAccount, buttonQuit);

        Scene scene = new Scene(rootLayout, 1200, 700);

        primaryStage.setFullScreen(true);
        primaryStage.setTitle("Book Donation & Management system");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}