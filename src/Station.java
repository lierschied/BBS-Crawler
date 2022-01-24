public class Station {
    private int id;
    private String name;
    private String zustand;

    public Station(int id, String name, String zustand) {
        this.id = id;
        this.name = name;
        this.zustand = zustand;
    }

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

    public String getZustand() {
        return zustand;
    }

    public void setZustand(String zustand) {
        this.zustand = zustand;
    }
}
