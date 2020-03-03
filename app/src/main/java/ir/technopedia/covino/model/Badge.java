package ir.technopedia.covino.model;

public class Badge {
    int id;
    String name;
    String condition;
    String gotten;
public Badge(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getGotten() {
        return gotten;
    }

    public void setGotten(String gotten) {
        this.gotten = gotten;
    }

    public Badge(int id, String name, String condition, String gotten) {
        this.id = id;
        this.name = name;
        this.condition = condition;
        this.gotten = gotten;
    }
}
