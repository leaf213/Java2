import java.io.*;
import java.util.ArrayList;

public class bookDataFile {
    private static final String FILE_NAME = "books_data.csv";
    //load book data from csv file
    public static ArrayList<Book> loadBooks() {
        ArrayList<Book> bookList = new ArrayList<>();
        File file = new File(FILE_NAME);

        //if file not exist then create file
        if (! file.exists()) {
            try{
                file.createNewFile();
            } catch(IOException e) {
                System.err.println("Error creating new file: " + e.getMessage());
            }

            Book [] books = {
                new Book("Harry Potter Philosopher Stone", "J.K.Rowling", "9780747532699", "Fantasy", 1), 
                new Book("Harry Potter Chamber of Secrets", "J.K.Rowling", "0747538492", "Fantasy", 1),
                new Book("Harry Potter Goblet of Fire", "J.K.Rowling", "0747550794", "Fantasy", 1),
                new Book("Tsubaki Stationary Store", "Ito Ogawa", "9798217047314", "Literacture", 1),
                new Book("Tsubaki Stationary Store", "Ito Ogawa", "9781529994865", "Literacture", 1),
                new Book("Journey Under the Midnight Sun", "Keigo Higashino", "9787544258609", "Fantasy", 2),
                new Book("Snow Country", "Yasunari Kawabata", "9787544248662", "Literature", 2),
                new Book("Life Is Worth Living", "Tsuneko Nakamura", "9787547734315", "Literature", 2),
                new Book("Summer, Fireworks, and My Corpse", "Otsuichi", "9787544296274", "Mystery", 2)
            };

            for(Book b : books) {
                bookList.add(b);
            }

            //write the list into file
            saveBooks(bookList);
            //return the list
            return bookList;
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String title = parts[0].trim();
                    String author = parts[1].trim();
                    String isbn = parts[2].trim();
                    String category = parts[3].trim();
                    int quantity = Integer.parseInt(parts[4].trim());
                    bookList.add(new Book(title, author, isbn, category, quantity));
                }
            }
        } catch(IOException e) {
            System.err.println("Error reading book data: " + e.getMessage());
        }

        return bookList;
    }

    public static void saveBooks(ArrayList<Book> bookList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Book b : bookList) {
                String line = b.getTitle() + "," +
                              b.getAuthor() + "," +
                              b.getIsbn() + "," +
                              b.getCategory() + "," +
                              b.getQuantity();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving book data: " + e.getMessage());
        }
    }
}