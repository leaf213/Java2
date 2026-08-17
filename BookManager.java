import java.util.ArrayList;

class bookManager {
    public ArrayList <Book> bookList;

    public bookManager() {
        this.bookList = bookDataFile.loadBooks();
    }

    public static String sanitizeIsbn(String raw) {
        if(raw == null) {
            return "";
        }

        String cleaned = raw.trim().replaceAll("[\\s-]", "");
        if(cleaned.matches("\\d{9}[Xx]")) {
            return cleaned.toUpperCase();
        }

        return cleaned.replaceAll("[^0-9]", "");
    }

    public static boolean isValidIsbn(String isbn) {
        String cleaned = sanitizeIsbn(isbn);
        return cleaned.length() == 10 || cleaned.length() == 13;
    }

    public String addOrUpdateBook(String title, String author, String isbn, String category) {
        for (Book b : bookList) {
            if (b.getIsbn().equals(isbn)) {
                b.setQuantity(b.getQuantity() + 1);
                bookDataFile.saveBooks(bookList);
                return "Book already exists! Updated stock for ISBN: " + isbn + " (Total Qty: " + b.getQuantity() + ")";
            }
        }

        bookList.add(new Book(title, author, isbn, category, 1));
        return "Successfully donated and added: " + title;
    }

    public int findBookIndex(String isbn) {
        isbn = sanitizeIsbn(isbn);
        for(int i = 0; i < bookList.size(); i++) {
            if(bookList.get(i).getIsbn().equals(isbn)) {
                return i;
            }
        }

        return -1;
    }
}