package src.ClassFolder;
public class Book {
    private String title;
    private String author;
    private String isbn;
    private String category;
    private int quantity;
    
    public Book(String title, String author, String isbn, String category, int quantity) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String displayInfo() {
        return "Title: " + title + "\nAuthor: " + author + "\nISBN: " + isbn + "\nCategory: " + category + "\nQuantity: " + quantity;
    }
}