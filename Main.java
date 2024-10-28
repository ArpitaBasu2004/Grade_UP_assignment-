import java.util.*;
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

    public abstract void addRaceResult(int position);  // Abstract method to be implemented in subclass
}


class Formula1Driver extends Driver {
    private int firstPositions;
    private int secondPositions;
    private int thirdPositions;
    private int points;
    private int racesParticipated;

    public Formula1Driver(String name, String location, String team) {
        super(name, location, team);
        this.firstPositions = 0;
        this.secondPositions = 0;
        this.thirdPositions = 0;
        this.points = 0;
        this.racesParticipated = 0;
    }

    @Override
    public void addRaceResult(int position) {
        if (position < 1 || position > 10) return;  // Only top 10 positions earn points

        racesParticipated++;
        switch (position) {
            case 1 -> {
                firstPositions++;
                points += 25;
            }
            case 2 -> {
                secondPositions++;
                points += 18;
            }
            case 3 -> {
                thirdPositions++;
                points += 15;
            }
            case 4 -> points += 12;
            case 5 -> points += 10;
            case 6 -> points += 8;
            case 7 -> points += 6;
            case 8 -> points += 4;
            case 9 -> points += 2;
            case 10 -> points += 1;
        }
    }

    // Getters for statistics
    public int getFirstPositions() {
        return firstPositions;
    }

    public int getSecondPositions() {
        return secondPositions;
    }

    public int getThirdPositions() {
        return thirdPositions;
    }

    public int getPoints() {
        return points;
    }

    public int getRacesParticipated() {
        return racesParticipated;
    }

    @Override
    public String toString() {
        return String.format("Driver: %s | Team: %s | Points: %d | Races: %d | 1st: %d | 2nd: %d | 3rd: %d",
                getName(), getTeam(), points, racesParticipated, firstPositions, secondPositions, thirdPositions);
    }
}


public class Main {
    public static void main(String[] args) {
        Formula1Driver driver = new Formula1Driver("Lewis Hamilton", "UK", "Mercedes");
        driver.addRaceResult(1);
        driver.addRaceResult(2);
        driver.addRaceResult(5);

        System.out.println(driver.toString());
    }
}

