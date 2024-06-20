class Player {
    private String name;
    private int points;
    private String type;

    public Player(String name) {
        this.name = name;
        this.points = 0;
    }

    public Player(String name, int points, String type) {
        this.name = name;
        this.points = points;
        this.type = type;
    }

    public Player(String name, int points) {
        this.name = name;
        this.points = points;
    }

    public Player(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    @Override
    public String toString() {
        return "name:" + name + "\t" + "points:" + points + "\t" + "type:" + type;
    }
}
