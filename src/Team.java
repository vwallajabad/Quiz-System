import java.net.URL;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Team {
    private Player[] players;
    private int totalPoints;
    private String name;

    public Team(String name, Player[] players) {
        this.name = name;
        this.players = players;
        this.totalPoints = 0;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void calculateTotalPoints() {
        for (Player player : players) {
            totalPoints += player.getPoints();
        }
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public String getName() {
        return name;
    }

    public int size() {
        return players.length;
    }

    public void store() {
        try {
            URL resourceURL = getClass().getClassLoader().getResource("team_reports/");
            if (resourceURL == null) {
                throw new IOException("Resource directory not found");
            }

            File resourceDir = new File(resourceURL.toURI());

            File outputFile = new File(resourceDir, name + ".txt");

            try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
                for (Player player : players) {
                    String name_points = player.toString();
                    writer.println(name_points);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFromFile() {
        List<Player> tempPlayers = new ArrayList<>();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("team_reports/" + name + ".txt");

        if (inputStream == null) {
            System.out.println("File not found!");
            return;
        }

        try (Scanner scanner = new Scanner(inputStream)) {
            String name;
            int points;
            String type;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\t");
                if (parts.length == 3) {
                    name = parts[0].split(":")[1];
                    points = Integer.parseInt(parts[1].split(":")[1]);
                    type = parts[2].split(":")[1];
                    tempPlayers.add(new Player(name, points, type));
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }
            players = tempPlayers.toArray(new Player[tempPlayers.size()]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        String result = "";
        for (Player player : players) {
            result += player.toString() + "\n";
        }
        return result;
    }
}