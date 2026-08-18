package src.ClassFolder;
public class Member {

    private String memberId;
    private String name;
    private String phone;
    private String email;
    private String registerDate;

    public Member(String memberId, String name, String phone, String email, String registerDate) {
        this.memberId = memberId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.registerDate = registerDate;
    }

    // getters
    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    String displayInfo() {
        return "Member ID: " + memberId
                + "\nName: " + name
                + "\nPhone: " + phone
                + "\nEmail: " + email
                + "\nRegistered: " + registerDate;
    }
}
