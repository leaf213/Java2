package ManageData;
import java.io.*;
import java.util.ArrayList;

import src.ClassFolder.ReturnedBook;

import java.time.LocalDate;

public class historyDataFile {
    private static final String FILE_NAME = "ManageData/data/history_borrows.csv";

    public static ArrayList<ReturnedBook> loadHistory() {
        ArrayList<ReturnedBook> list = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            try {
                file.createNewFile();
                
                //book1
                list.add(new ReturnedBook(
                    "Snow Country", "Yasunari Kawabata", "9787544248662", "Literature", 1, "john", LocalDate.now().minusDays(20).toString(), LocalDate.now().minusDays(15).toString()
                ));
                //book2
                list.add(new ReturnedBook(
                    "Life Is Worth Living", "Tsuneko Nakamura", "9787547734315", "Literature", 1, "john", LocalDate.now().minusDays(30).toString(), LocalDate.now().minusDays(22).toString()
                ));

                saveHistory(list);
                return list;
            } catch (IOException e) {
                System.err.println("Error creating history file: " + e.getMessage());
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 8) {
                    list.add(new ReturnedBook(
                        parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(),
                        Integer.parseInt(parts[4].trim()), parts[5].trim(), parts[6].trim(), parts[7].trim()
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading history data: " + e.getMessage());
        }

        return list;
    }

    public static void saveHistory(ArrayList<ReturnedBook> list) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (ReturnedBook h : list) {
                writer.println(h.getTitle() + "," + h.getAuthor() + "," + h.getIsbn() + ","
                        + h.getCategory() + "," + h.getQuantity() + "," + h.getBorrowerName() + ","
                        + h.getBorrowDate() + "," + h.getReturnDate());
            }
        } catch (IOException e) {
            System.err.println("Error saving history data: " + e.getMessage());
        }
    }
}
