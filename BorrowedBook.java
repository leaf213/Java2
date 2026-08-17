class BorrowedBook extends Book {
    private String borrowerName;
    private String borrowDate;
    private int borrowDays;
    private String dueDate;

    public BorrowedBook(String title, String author, String isbn, String category, int quantity, String borrowerName, String borrowDate, int borrowDays, String dueDate) {
        super(title, author, isbn, category, quantity);
        this.borrowerName = borrowerName;
        this.borrowDate = borrowDate;
        this.borrowDays = borrowDays;
        this.dueDate = dueDate;
    }

    // JavaBean-style getters required by JavaFX PropertyValueFactory
    public String getBorrowerName() {
        return borrowerName;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public int getBorrowDays() {
        return borrowDays;
    }

    public String getDueDate() {
        return dueDate;
    }

    // Also provide setters for completeness (optional but recommended)
    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setBorrowDays(int borrowDays) {
        this.borrowDays = borrowDays;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
