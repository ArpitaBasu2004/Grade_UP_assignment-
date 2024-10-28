import java.io.*;
import java.util.*;

interface ChampionshipManager {
    void addDriver(Formula1Driver driver);
    void removeDriver(String team);
    Formula1Driver getDriver(String team);
    void displayDriverStatistics(String team);
    void displayChampionshipTable();
    void addRace(Map<String, Integer> raceResults, Date date);
    void saveToFile();
    void loadFromFile();
}

abstract class Driver {
    private String name;
    private String location;
    private String team;

    public Driver(String name, String location, String team) {
        this.name = name;
        this.location = location;
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getTeam() {
        return team;
    }

    public abstract void addRaceResult(int position);
}

class Formula1Driver extends Driver implements Serializable {
    private int firstPositions = 0;
    private int secondPositions = 0;
    private int thirdPositions = 0;
    private int points = 0;
    private int racesParticipated = 0;

    public Formula1Driver(String name, String location, String team) {
        super(name, location, team);
    }

    @Override
    public void addRaceResult(int position) {
        racesParticipated++;
        if (position == 1) {
            firstPositions++;
            points += 25;
        } else if (position == 2) {
            secondPositions++;
            points += 18;
        } else if (position == 3) {
            thirdPositions++;
            points += 15;
        } else if (position >= 4 && position <= 10) {
            points += switch (position) {
                case 4 -> 12;
                case 5 -> 10;
                case 6 -> 8;
                case 7 -> 6;
                case 8 -> 4;
                case 9 -> 2;
                case 10 -> 1;
                default -> 0;
            };
        }
    }

    public int getFirstPositions() {
        return firstPositions;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return String.format("Driver: %s | Team: %s | Points: %d | Races: %d | 1st: %d | 2nd: %d | 3rd: %d",
                getName(), getTeam(), points, racesParticipated, firstPositions, secondPositions, thirdPositions);
    }
}

class Formula1ChampionshipManager implements ChampionshipManager {
    private List<Formula1Driver> drivers = new ArrayList<>();
    private final String FILE_PATH = "f1championship.dat";

    @Override
    public void addDriver(Formula1Driver driver) {
        drivers.add(driver);
    }

    @Override
    public void removeDriver(String team) {
        drivers.removeIf(driver -> driver.getTeam().equalsIgnoreCase(team));
    }

    @Override
    public Formula1Driver getDriver(String team) {
        return drivers.stream().filter(driver -> driver.getTeam().equalsIgnoreCase(team)).findFirst().orElse(null);
    }

    @Override
    public void displayDriverStatistics(String team) {
        Formula1Driver driver = getDriver(team);
        if (driver != null) {
            System.out.println(driver);
        } else {
            System.out.println("Driver not found.");
        }
    }

    @Override
    public void displayChampionshipTable() {
        drivers.stream()
                .sorted(Comparator.comparingInt(Formula1Driver::getPoints).reversed()
                        .thenComparing(Formula1Driver::getFirstPositions, Comparator.reverseOrder()))
                .forEach(System.out::println);
    }

    @Override
    public void addRace(Map<String, Integer> raceResults, Date date) {
        raceResults.forEach((team, position) -> {
            Formula1Driver driver = getDriver(team);
            if (driver != null) driver.addRaceResult(position);
        });
    }

    @Override
    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(drivers);
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving data.");
        }
    }

    @Override
    public void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            drivers = (List<Formula1Driver>) ois.readObject();
            System.out.println("Data loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data.");
        }
    }
}

public class Formula1ChampionshipApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Formula1ChampionshipManager manager = new Formula1ChampionshipManager();

    public static void main(String[] args) {
        manager.loadFromFile();
        while (true) {
            System.out.println("\n1. Add Driver\n2. Remove Driver\n3. Change Driver\n4. Display Driver Stats\n5. Display Championship Table\n6. Add Race\n7. Save Data\n8. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1 -> addDriver();
                case 2 -> removeDriver();
                case 3 -> changeDriver();
                case 4 -> displayDriverStats();
                case 5 -> manager.displayChampionshipTable();
                case 6 -> addRace();
                case 7 -> manager.saveToFile();
                case 8 -> System.exit(0);
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addDriver() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter location: ");
        String location = scanner.nextLine();
        System.out.print("Enter team: ");
        String team = scanner.nextLine();

        Formula1Driver driver = new Formula1Driver(name, location, team);
        manager.addDriver(driver);
        System.out.println("Driver added.");
    }

    private static void removeDriver() {
        System.out.print("Enter team to remove: ");
        String team = scanner.nextLine();
        manager.removeDriver(team);
        System.out.println("Driver removed.");
    }

    private static void changeDriver() {
        System.out.print("Enter existing team: ");
        String team = scanner.nextLine();
        System.out.print("Enter new driver's name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new driver's location: ");
        String location = scanner.nextLine();

        manager.removeDriver(team);
        manager.addDriver(new Formula1Driver(name, location, team));
        System.out.println("Driver changed.");
    }

    private static void displayDriverStats() {
        System.out.print("Enter team to display: ");
        String team = scanner.nextLine();
        manager.displayDriverStatistics(team);
    }

    private static void addRace() {
        Map<String, Integer> raceResults = new HashMap<>();
        System.out.println("Enter race results (team and position):");
        while (true) {
            System.out.print("Enter team (or 'done' to finish): ");
            String team = scanner.nextLine();
            if (team.equalsIgnoreCase("done")) break;
            System.out.print("Enter position: ");
            int position = scanner.nextInt();
            scanner.nextLine();  // Consume newline
            raceResults.put(team, position);
        }
        manager.addRace(raceResults, new Date());
        System.out.println("Race added.");
    }
}
