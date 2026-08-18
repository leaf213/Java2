package src.ClassFolder;
public class RecordItem {
    private String title;
    private String isbn;
    private String borrowDate;
    private String returnDate;
    private int quantity;

    public RecordItem(String title, String isbn, String borrowDate, String returnDate, int quantity) {
        this.title = title;
        this.isbn = isbn;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.quantity = quantity;
    }

    public String getTitle() { return title; }
    public String getIsbn() { return isbn; }
    public String getBorrowDate() { return borrowDate; }
    public String getReturnDate() { return returnDate; }
    public int getQuantity() { return quantity; }
}
