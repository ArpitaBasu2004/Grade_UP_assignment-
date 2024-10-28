import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Formula1ChampionshipGUI extends JFrame {
    private final Formula1ChampionshipManager manager;
    private final DefaultTableModel driverTableModel;
    private final JTextArea raceResultsArea;

    public Formula1ChampionshipGUI(Formula1ChampionshipManager manager) {
        this.manager = manager;

        setTitle("Formula 1 Championship Manager");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set up the driver table
        driverTableModel = new DefaultTableModel(new Object[]{"Driver", "Team", "Points", "1st Positions"}, 0);
        JTable driverTable = new JTable(driverTableModel);
        JScrollPane driverScrollPane = new JScrollPane(driverTable);

        // Buttons for various actions
        JButton displayDriversButton = new JButton("Display Drivers");
        displayDriversButton.addActionListener(e -> displayDrivers());

        JButton randomRaceButton = new JButton("Generate Random Race");
        randomRaceButton.addActionListener(e -> generateRandomRace());

        // Text area for race results
        raceResultsArea = new JTextArea(10, 30);
        JScrollPane raceResultsScrollPane = new JScrollPane(raceResultsArea);

        // Add components to the frame
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(displayDriversButton);
        buttonPanel.add(randomRaceButton);

        add(driverScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.NORTH);
        add(raceResultsScrollPane, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void displayDrivers() {
        driverTableModel.setRowCount(0); // Clear the table
        List<Formula1Driver> drivers = manager.getDrivers();
        drivers.sort((d1, d2) -> Integer.compare(d2.getPoints(), d1.getPoints())); // Sort by points

        for (Formula1Driver driver : drivers) {
            driverTableModel.addRow(new Object[]{
                    driver.getName(), driver.getTeam(), driver.getPoints(), driver.getFirstPositions()
            });
        }
    }

    private void generateRandomRace() {
        raceResultsArea.setText("Generating Random Race...\n");
        List<Formula1Driver> drivers = new ArrayList<>(manager.getDrivers());
        Collections.shuffle(drivers); // Randomize driver positions

        for (int i = 0; i < drivers.size() && i < 10; i++) {
            Formula1Driver driver = drivers.get(i);
            driver.addRaceResult(i + 1);
            raceResultsArea.append("Driver: " + driver.getName() + " finished in position " + (i + 1) + "\n");
        }

        displayDrivers(); // Update the driver table after the race
    }

    public static void main(String[] args) {
        Formula1ChampionshipManager manager = new Formula1ChampionshipManager();
        manager.loadFromFile();

        SwingUtilities.invokeLater(() -> new Formula1ChampionshipGUI(manager));
    }
}
