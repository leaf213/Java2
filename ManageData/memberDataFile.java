package ManageData;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

import src.ClassFolder.Member;

public class memberDataFile {

    // the file where the reader profiles are stored
    private static final String FILE_NAME = "ManageData/data/members_data.csv";

    public static Member[] loadMembers() {
        File file = new File(FILE_NAME);

        // no file yet means no members have registered - create it empty
        if (! file.exists()) {
            try{
                file.createNewFile();
                Member [] initialMembers = {
                    new Member("M001", "John", "0123456789", "john@gmail.com", LocalDate.now().toString())
                };
                saveMembers(initialMembers);
                return initialMembers;
            } catch(IOException ex) {
                System.err.println("Error creating initial members file: " + ex.getMessage());
            }
        }

        Member[] members = new Member[0];

        try{
            int count = 0;
            Scanner counter = new Scanner(file);
            while (counter.hasNextLine()) {
                if (!counter.nextLine().trim().isEmpty()) {
                    count++;
                }
            }
            counter.close();

            // STEP 2: create an array that is exactly the right size
            members = new Member[count];

            // STEP 3: second pass - read each line and turn it into a Member
            Scanner reader = new Scanner(file);
            int index = 0;
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();

                // skip empty lines
                if (line.isEmpty()) {
                    continue;
                }

                // each line is: MemberID,Name,Phone,Email,RegisterDate
                String[] parts = line.split(",");

                // only use the line if it really has 5 parts
                if (parts.length == 5) {
                    members[index] = new Member(
                            parts[0],   // member id
                            parts[1],   // name
                            parts[2],   // phone
                            parts[3],   // email
                            parts[4]);  // register date
                    index++;
                }
            }
            reader.close();

            // some lines may have been skipped (malformed) - trim the array
            // so there are no null holes left at the end
            if (index < members.length) {
                Member[] trimmed = new Member[index];
                System.arraycopy(members, 0, trimmed, 0, index);
                members = trimmed;
            }
        } catch (IOException e) {
            System.err.println("Error reading members file: " + e.getMessage());
        }

        return members;
    }

    public static void saveMembers(Member[] members) {
        PrintWriter writer = null;

        try {
            // PrintWriter can create the file if it does not exist
            writer = new PrintWriter(FILE_NAME);

            // write one line per member
            for (int i = 0; i < members.length; i++) {
                Member m = members[i];
                writer.println(m.getMemberId() + ","
                        + m.getName() + ","
                        + m.getPhone() + ","
                        + m.getEmail() + ","
                        + m.getRegisterDate());
            }

        } catch (IOException e) {
            System.err.println("Error saving members file: " + e.getMessage());
        } finally {
            // always close the file, even if an error happened
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static String nextMemberId(Member[] members) {
        int maxNumber = 0;

        for (int i = 0; i < members.length; i++) {
            // skip empty slots defensively
            if (members[i] == null) {
                continue;
            }
            String id = members[i].getMemberId();
            // IDs look like "M001" - strip the "M" and read the number
            if (id != null && id.length() > 1) {
                try {
                    int number = Integer.parseInt(id.substring(1));
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                } catch (NumberFormatException e) {
                    // ignore IDs that are not "M<number>"
                }
            }
        }

        // pad with leading zeros, e.g. M001, M002, ... M099, M100
        return "M" + String.format("%03d", maxNumber + 1);
    }

    public static Member[] growMemberArray(Member[] oldArray) {
        Member[] newArray = new Member[oldArray.length + 1];
        for (int i = 0; i < oldArray.length; i++) {
            newArray[i] = oldArray[i];
        }
        return newArray;
    }
}
