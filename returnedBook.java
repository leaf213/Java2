public class returnedBook {
    private String title;
    private String author;
    private String isbn;
    private String category;
    private int quantity;
    private String borrowerName;
    private String borrowDate;
    private String returnDate;

    public returnedBook(String title, String author, String isbn, String category, int quantity, String borrowerName, String borrowDate, String returnDate) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.quantity = quantity;
        this.borrowerName = borrowerName;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public String getCategory() { return category; }
    public int getQuantity() { return quantity; }
    public String getBorrowerName() { return borrowerName; }
    public String getBorrowDate() { return borrowDate; }
    public String getReturnDate() { return returnDate; }
}