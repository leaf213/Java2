import java.time.LocalDate;
import java.util.ArrayList;

public class borrowManager {
    public ArrayList <BorrowedBook> borrowedBooks;

    public borrowManager() {
        this.borrowedBooks = borrowDataFile.loadBorrows();
    }

    public String borrowBook(String isbn, String borrowerName, int borrowDays, bookManager bookMgr) throws borrowException {
        bookManager.sanitizeIsbn(isbn);

        if (borrowerName == null || borrowerName.trim().isEmpty()) {
            throw new borrowException("Error: Borrower name cannot be empty!");
        }
    
        Member[] registeredMembers = memberDataFile.loadMembers();
        boolean isRegistered = false;
        for (Member m : registeredMembers) {
            if (m != null && m.getName().equalsIgnoreCase(borrowerName.trim())) {
                isRegistered = true;
                break;
            }
        }
    
        if (!isRegistered) {
            throw new borrowException("Access Denied: \"" + borrowerName + "\" is not registered! Please go to 'My Account' to register first.");
        }

        int index = bookMgr.findBookIndex(isbn);

        if(index == -1) {
            throw new borrowException("Book not found for ISBN: " + isbn);
        }

        if(borrowDays < 1 || borrowDays > 7) {
            throw new borrowException("Borrow duration must be between 1 and 7 days!");
        }

        Book targetBook = bookMgr.bookList.get(index);
        if(targetBook.getQuantity() <= 0) {
            throw new borrowException("Sorry, \""+ targetBook.getTitle() + "\" is out of stock right now.");
        }

        String borrowDate = LocalDate.now().toString();
        String dueDate = LocalDate.now().plusDays(borrowDays).toString();

        borrowedBooks.add(new BorrowedBook(
            targetBook.getTitle(), targetBook.getAuthor(), targetBook.getIsbn(), targetBook.getCategory(), 1, borrowerName, borrowDate, borrowDays, dueDate
        ));

        targetBook.setQuantity(targetBook.getQuantity() -1);

        bookDataFile.saveBooks(bookMgr.bookList);
        borrowDataFile.saveBorrows(borrowedBooks);

        return "Sucessfully borrowed: \"" + targetBook.getTitle() + "\" by " + borrowerName;
    }

    public String returnBook(String isbn, bookManager bookMgr) throws borrowException {
        bookManager.sanitizeIsbn(isbn);
        if(isbn.isEmpty()) {
            throw new borrowException("Error: ISBN field cannot be empty!");
        }

        BorrowedBook matchedBorrow = null;
        for(BorrowedBook bb : borrowedBooks) {
            if(bb.getIsbn().equals(isbn)) {
                matchedBorrow = bb;
                break;
            }
        }

        if(matchedBorrow == null) {
            throw new borrowException("Error: No active borrow record found for ISBN " + isbn + ". This book is not currently on loan.");
        }

        int index = bookMgr.findBookIndex(isbn);
        if(index == -1) {
            throw new borrowException("Error: Book with ISBN \" + isbn + \" is not registered in the system.");
        }

        Book targetBook = bookMgr.bookList.get(index);
        targetBook.setQuantity(targetBook.getQuantity() +1);
        borrowedBooks.remove(matchedBorrow);

        ArrayList<ReturnedBook> historyList = historyDataFile.loadHistory();
        historyList.add(new ReturnedBook(
            matchedBorrow.getTitle(),
            matchedBorrow.getAuthor(),
            matchedBorrow.getIsbn(),
            matchedBorrow.getCategory(),
            1,
            matchedBorrow.getBorrowerName(),
            matchedBorrow.getBorrowDate(),
            LocalDate.now().toString()
        ));
        historyDataFile.saveHistory(historyList);

        bookDataFile.saveBooks(bookMgr.bookList);
        borrowDataFile.saveBorrows(borrowedBooks);

        return "Success: \"" + targetBook.getTitle() + "\" returned by " + matchedBorrow.getBorrowerName();
    }
}
