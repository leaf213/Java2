import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class borrowDataFile {
    private static final String FILE_NAME = "borrows_data.csv";

    public static ArrayList<BorrowedBook> loadBorrows() {
        ArrayList<BorrowedBook> list = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            try {
                file.createNewFile();
                
                BorrowedBook initialBorrow1 = new BorrowedBook(
                    "Harry Potter Philosopher Stone", 
                    "J.K.Rowling", 
                    "9780747532699", 
                    "Fantasy", 
                    1, 
                    "john",                  
                    LocalDate.now().toString(),   
                    7,                            
                    LocalDate.now().plusDays(7).toString() 
                );

                BorrowedBook initialBorrow2 = new BorrowedBook(
                    "Tsubaki Stationary Store", 
                    "Ito Ogawa", 
                    "9798217047314", 
                    "Literature", 
                    1, 
                    "john",                  
                    LocalDate.now().toString(),   
                    5,                            
                    LocalDate.now().plusDays(5).toString() 
                );

                list.add(initialBorrow1);
                list.add(initialBorrow2);
                saveBorrows(list);
                return list;
            } catch (IOException e) {
                System.err.println("Error creating initial borrows file: " + e.getMessage());
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",", -1); // Use -1 to keep trailing empty strings
                if (parts.length >= 9) {
                    try {
                        String title = parts[0].trim();
                        String author = parts[1].trim();
                        String isbn = parts[2].trim();
                        String category = parts[3].trim();
                        int quantity = Integer.parseInt(parts[4].trim());
                        String borrowerName = parts[5].trim();
                        String borrowDate = parts[6].trim();
                        int borrowDays = Integer.parseInt(parts[7].trim());
                        String dueDate = parts[8].trim();

                        list.add(new BorrowedBook(title, author, isbn, category, quantity, borrowerName, borrowDate, borrowDays, dueDate));
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping malformed line: " + line);
                    }
                } else {
                    System.err.println("Skipping line with incorrect number of fields: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading borrows data: " + e.getMessage());
        }

        return list;
    }

    public static void saveBorrows(ArrayList<BorrowedBook> list) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (BorrowedBook b : list) {
                writer.println(b.getTitle() + ","
                        + b.getAuthor() + ","
                        + b.getIsbn() + ","
                        + b.getCategory() + ","
                        + b.getQuantity() + ","
                        + b.getBorrowerName() + ","
                        + b.getBorrowDate() + ","
                        + b.getBorrowDays() + ","
                        + b.getDueDate());
            }
        } catch (IOException e) {
            System.err.println("Error saving borrows data: " + e.getMessage());
        }
    }
}
