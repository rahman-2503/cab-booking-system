import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.sql.*;

public class CabBookingLoginGUI {
    private static List<String> bookingHistory = new ArrayList<>();
    private static String selectedSource;
    private static String selectedDestination;
    private static String selectedVehicle;
    private static String selectedVariant;
    private static String selectedTime;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowSignUpGUI();
        });
    }

    private static void createAndShowSignUpGUI() {
        JFrame signUpFrame = new JFrame("Cab Booking Sign Up");
        signUpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        signUpFrame.setSize(400, 250);
        signUpFrame.setLayout(new GridLayout(6, 2));

        JLabel usernameLabel = new JLabel("Create Username:");
        JTextField usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Set Password:");
        JPasswordField passwordField = new JPasswordField();

        JLabel mobileLabel = new JLabel("Mobile Number:");
        JTextField mobileField = new JTextField();

        JLabel emailLabel = new JLabel("Email Address:");
        JTextField emailField = new JTextField();

        JButton signUpButton = new JButton("Sign Up");

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String mobileNumber = mobileField.getText();
                String emailAddress = emailField.getText();
                try {
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:port number:XE", "system",
                            "password");
                    Statement stmt = con.createStatement();
                    System.out.println(con);
                    stmt.executeUpdate(
                            "CREATE TABLE signup(username VARCHAR(20), password VARCHAR(20), mobileNumber varchar(12), emailAddress VARCHAR(20))");
                    String insertQuery = String.format("INSERT INTO signup VALUES('%s', '%s', '%s', '%s')", username,
                            password, mobileNumber, emailAddress);
                    stmt.executeUpdate(insertQuery);
                    con.close();

                } catch (Exception d) {
                    {
                        System.out.println(d);
                    }

                }

                // Implement sign-up logic here (store user data or perform validation)
                if (!username.isEmpty() && password.length() > 0 && !mobileNumber.isEmpty()
                        && !emailAddress.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Sign Up successful!");
                    signUpFrame.dispose(); // Close the sign-up window
                    createAndShowLoginGUI();
                } else {
                    JOptionPane.showMessageDialog(null, "Please fill in all the fields.");
                }
            }
        });

        signUpFrame.add(usernameLabel);
        signUpFrame.add(usernameField);
        signUpFrame.add(passwordLabel);
        signUpFrame.add(passwordField);
        signUpFrame.add(mobileLabel);
        signUpFrame.add(mobileField);
        signUpFrame.add(emailLabel);
        signUpFrame.add(emailField);
        signUpFrame.add(new JLabel()); // Placeholder for spacing
        signUpFrame.add(signUpButton);

        signUpFrame.setVisible(true);
    }

    private static void createAndShowLoginGUI() {
        JFrame loginFrame = new JFrame("Cab Booking Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 150);
        loginFrame.setLayout(new GridLayout(4, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton signInButton = new JButton("SIGN IN");

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String value = "";
                try {
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:port number:XE", "system",
                            "password");
                    Statement stmt = con.createStatement();
                    System.out.println(con);
                    String getQuery = String.format("select password from signup where username = '%s'", username);
                    ResultSet getPassword = stmt.executeQuery(getQuery);
                    // Convert the ResultSet to a String
                    StringBuilder resultString = new StringBuilder();
                    while (getPassword.next()) {
                        value = getPassword.getString("password");
                        resultString.append(value);
                    }
                } catch (Exception wp) {
                    System.out.println(wp);
                }
                // Implement authentication logic here (replace with your authentication logic)
                //if (!username.isEmpty() && password.length() > 0) {
                    if (password.equals(value)) {
                        JOptionPane.showMessageDialog(null, "Login successful!");
                        loginFrame.dispose(); // Close the login window
                        displayMainMenu();
                    } else {
                        JOptionPane.showMessageDialog(null, "Login failed. Invalid username or password.");
                    }
                //}
            }
        });

        loginFrame.add(usernameLabel);
        loginFrame.add(usernameField);
        loginFrame.add(passwordLabel);
        loginFrame.add(passwordField);
        loginFrame.add(signInButton);
        loginFrame.setVisible(true);
    }

    private static void displayMainMenu() {
        JFrame mainMenuFrame = new JFrame("Cab Booking System");
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setSize(300, 150);
        mainMenuFrame.setLayout(new GridLayout(3, 1));

        JButton bookRideButton = new JButton("Book a Ride");
        JButton viewHistoryButton = new JButton("View Booking History");
        JButton exitButton = new JButton("Exit");

        bookRideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookRide();
            }
        });

        viewHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String history = getBookingHistory();
                if (history.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No booking history available.");
                } else {
                    JOptionPane.showMessageDialog(null, "Booking History:\n" + history);
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:port number:XE", "system",
                            "password");
                    Statement stmt = con.createStatement();
                    System.out.println(con);
                    stmt.executeUpdate("drop table signup");
                    con.close();
                } catch (Exception dr) {
                    System.out.println(dr);
                }
                mainMenuFrame.dispose(); // Close the main menu window
            }
        });

        mainMenuFrame.add(bookRideButton);
        mainMenuFrame.add(viewHistoryButton);
        mainMenuFrame.add(exitButton);

        mainMenuFrame.setVisible(true);
    }

    private static void bookRide() {
        JFrame rideFrame = new JFrame("Book a Ride");
        rideFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        rideFrame.setSize(400, 250);
        rideFrame.setLayout(new GridLayout(11, 2));

        JLabel sourceLabel = new JLabel("Select Source:");
        JComboBox<String> sourceComboBox = new JComboBox<>(new String[] {
                "Kottapalli, India", "Vemalwada", "Peddapalli", "Sirsilla", "Jagtial", "Siddipet", "Lakshettipet",
                "Ramagundam", "Naspur", "Mancheral", "Manthani", "Koratla", "Warangal", "Kamareddi", "Narsingi"
        });

        JLabel destinationLabel = new JLabel("Select Destination:");
        JComboBox<String> destinationComboBox = new JComboBox<>(new String[] {
                "Kottapalli, India", "Vemalwada", "Peddapalli", "Sirsilla", "Jagtial", "Siddipet", "Lakshettipet",
                "Ramagundam", "Naspur", "Mancheral", "Manthani", "Koratla", "Warangal", "Kamareddi", "Narsingi"
        });

        JLabel vehicleLabel = new JLabel("Select Vehicle:");
        JComboBox<String> vehicleComboBox = new JComboBox<>(new String[] { "Bike", "Auto", "Car" });

        JLabel variantLabel = new JLabel("Select Variant:");
        JComboBox<String> variantComboBox = new JComboBox<>();

        JLabel timeLabel = new JLabel("Select Time (AM/PM):");
        JTextField timeField = new JTextField();

        JButton calculateButton = new JButton("Calculate Price");
        JButton cancelButton = new JButton("Cancel");

        sourceComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedSource = (String) sourceComboBox.getSelectedItem();
            }
        });

        destinationComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedDestination = (String) destinationComboBox.getSelectedItem();
            }
        });

        vehicleComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedVehicle = (String) vehicleComboBox.getSelectedItem();
                switch (selectedVehicle) {
                    case "Bike":
                        variantComboBox.setModel(new DefaultComboBoxModel<>(new String[] { "EV Bikes", "Petrol" }));
                        break;
                    case "Auto":
                        variantComboBox.setModel(new DefaultComboBoxModel<>(new String[] { "LPG Auto", "Diesel" }));
                        break;
                    case "Car":
                        variantComboBox.setModel(
                                new DefaultComboBoxModel<>(new String[] { "4 Seater", "7 Seater", "9 Seater" }));
                        break;
                }
            }
        });

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedVariant = (String) variantComboBox.getSelectedItem();
                selectedTime = timeField.getText();
                double price = generateRandomPrice();

                int confirmation = JOptionPane.showConfirmDialog(null,
                        "Source: " + selectedSource + "\n" +
                                "Destination: " + selectedDestination + "\n" +
                                "Vehicle: " + selectedVehicle + "\n" +
                                "Variant: " + selectedVariant + "\n" +
                                "Time: " + selectedTime + "\n" +
                                "Price: ₹" + price + "\n" +
                                "Confirm booking?",
                        "Confirm Booking", JOptionPane.YES_NO_OPTION);

                if (confirmation == JOptionPane.YES_OPTION) {
                    String bookingDetails = "Source: " + selectedSource + "\n" +
                            "Destination: " + selectedDestination + "\n" +
                            "Vehicle: " + selectedVehicle + "\n" +
                            "Variant: " + selectedVariant + "\n" +
                            "Time: " + selectedTime + "\n" +
                            "Price: ₹" + price;
                    bookingHistory.add(bookingDetails);
                    JOptionPane.showMessageDialog(null, "Booking successful!\n" + bookingDetails);
                    rideFrame.dispose(); // Close the ride booking window
                    displayDriverDetails();
                }
            }
        });
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:port number:XE", "system",
                    "password");
            Statement stmt = con.createStatement();
            System.out.println(con);
            stmt.executeUpdate(
                    "CREATE TABLE booking(Source VARCHAR(30), Destination VARCHAR(30), Vehicle varchar(30))");
            String insertQuery = String.format("INSERT INTO booking VALUES('%s', '%s', '%s')",
                    selectedSource,
                    selectedDestination, selectedVehicle);
            stmt.executeUpdate(insertQuery);
            con.close();

        } catch (Exception d) {
            {
                System.out.println(d);
            }

        }

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rideFrame.dispose(); // Close the ride booking window
            }
        });

        rideFrame.add(sourceLabel);
        rideFrame.add(sourceComboBox);
        rideFrame.add(destinationLabel);
        rideFrame.add(destinationComboBox);
        rideFrame.add(vehicleLabel);
        rideFrame.add(vehicleComboBox);
        rideFrame.add(variantLabel);
        rideFrame.add(variantComboBox);
        rideFrame.add(timeLabel);
        rideFrame.add(timeField);
        rideFrame.add(new JLabel()); // Placeholder for spacing
        rideFrame.add(calculateButton);
        rideFrame.add(new JLabel()); // Placeholder for spacing
        rideFrame.add(cancelButton);

        rideFrame.setVisible(true);
    }

    private static void displayDriverDetails() {
        JFrame driverFrame = new JFrame("Driver Details");
        driverFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        driverFrame.setSize(400, 150);
        driverFrame.setLayout(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("Name of the Driver:");
        JTextField nameField = new JTextField();

        JLabel contactLabel = new JLabel("Contact Number:");
        JTextField contactField = new JTextField();

        JLabel vehicleNumberLabel = new JLabel("Vehicle Number:");
        JTextField vehicleNumberField = new JTextField();

        JButton closeButton = new JButton("Close");

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                driverFrame.dispose(); // Close the driver details window
            }
        });

        driverFrame.add(nameLabel);
        driverFrame.add(nameField);
        driverFrame.add(contactLabel);
        driverFrame.add(contactField);
        driverFrame.add(vehicleNumberLabel);
        driverFrame.add(vehicleNumberField);
        driverFrame.add(new JLabel()); // Placeholder for spacing
        driverFrame.add(closeButton);

        driverFrame.setVisible(true);
    }

    private static String getBookingHistory() {
        if (bookingHistory.isEmpty()) {
            return "";
        }
        StringBuilder history = new StringBuilder();
        for (String entry : bookingHistory) {
            history.append(entry).append("\n\n");
        }
        return history.toString();
    }

    private static double generateRandomPrice() {
        // Generate a random price for the ride
        Random rand = new Random();
        return 50 + rand.nextInt(1000); // Random price between 50 and 1049
    }
}