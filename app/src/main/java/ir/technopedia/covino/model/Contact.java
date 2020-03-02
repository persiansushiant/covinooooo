package ir.technopedia.covino.model;

public class Contact {

    String name, phone, count;

    public Contact() {
    }

    public Contact(String name, String phone, String count) {
        this.name = name;
        this.phone = phone;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
