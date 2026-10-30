
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class CampusEventRegistration extends JFrame {

    JTextField nameField, rollField, searchField;
    JComboBox<String> eventBox;
    JLabel seatsLabel;
    JTable table;
    DefaultTableModel model;

    ArrayList<String[]> registrations = new ArrayList<>();

    String[] events = {
        "Technical Fest",
        "Cultural Fest",
        "Sports Meet"
    };

    int[] capacity = {3, 3, 3};

    Color purple = new Color(102, 80, 143);
    Color lavender = new Color(240, 236, 248);
    Color white = Color.WHITE;

    public CampusEventRegistration() {

        setTitle("Campus Event Registration System");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(lavender);
        setLayout(new BorderLayout(10, 10));

        // Heading
        JLabel heading = new JLabel(
            "Campus Event Registration System",
            JLabel.CENTER
        );

        heading.setFont(new Font("Arial", Font.BOLD, 23));
        heading.setForeground(white);
        heading.setBackground(purple);
        heading.setOpaque(true);
        heading.setBorder(
            BorderFactory.createEmptyBorder(18, 10, 18, 10)
        );

        add(heading, BorderLayout.NORTH);

        // Registration Form
        JPanel form = new JPanel(new GridLayout(5, 2, 10, 15));
        form.setBackground(white);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Student Registration"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        form.add(new JLabel("Student Name:"));

        nameField = new JTextField(15);
        form.add(nameField);

        form.add(new JLabel("Roll Number:"));

        rollField = new JTextField(15);
        form.add(rollField);

        form.add(new JLabel("Select Event:"));

        eventBox = new JComboBox<>(events);
        form.add(eventBox);

        form.add(new JLabel("Available Seats:"));

        seatsLabel = new JLabel();
        seatsLabel.setForeground(purple);
        seatsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        form.add(seatsLabel);

        JButton registerButton = new JButton("Register");
        JButton clearButton = new JButton("Clear");

        styleButton(registerButton);
        styleButton(clearButton);

        form.add(registerButton);
        form.add(clearButton);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(lavender);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
        leftPanel.add(form, BorderLayout.NORTH);

        add(leftPanel, BorderLayout.WEST);

        // Participants Table
        model = new DefaultTableModel(
            new String[]{"Student Name", "Roll Number", "Event"}, 0
        );

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setBackground(white);

        table.getTableHeader().setBackground(purple);
        table.getTableHeader().setForeground(white);
        table.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(
            BorderFactory.createTitledBorder("Registered Participants")
        );

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(lavender);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        // Search Panel
        JPanel searchPanel = new JPanel();

        searchPanel.setBackground(lavender);

        searchField = new JTextField(18);

        JButton searchButton = new JButton("Search");
        JButton showAllButton = new JButton("Show All");

        styleButton(searchButton);
        styleButton(showAllButton);

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);

        add(searchPanel, BorderLayout.SOUTH);

        // Button Actions
        eventBox.addActionListener(e -> updateSeats());

        registerButton.addActionListener(e -> registerStudent());

        clearButton.addActionListener(e -> {
            nameField.setText("");
            rollField.setText("");
        });

        searchButton.addActionListener(e -> searchStudent());

        showAllButton.addActionListener(e -> displayAll());

        updateSeats();

        setVisible(true);
    }

    // Button Styling
    void styleButton(JButton button) {

        button.setBackground(purple);
        button.setForeground(white);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setFocusPainted(false);
    }

    // Update Available Seats
    void updateSeats() {

        int index = eventBox.getSelectedIndex();

        int remaining = capacity[index] - countRegistrations(index);

        seatsLabel.setText(remaining + " seats available");
    }

    // Count Registrations
    int countRegistrations(int eventIndex) {

        int count = 0;

        for (String[] r : registrations) {

            if (r[2].equals(events[eventIndex])) {
                count++;
            }
        }

        return count;
    }

    // Register Student
    void registerStudent() {

        String name = nameField.getText().trim();
        String roll = rollField.getText().trim();
        String event = (String) eventBox.getSelectedItem();

        int index = eventBox.getSelectedIndex();

        if (name.isEmpty() || roll.isEmpty()) {

            JOptionPane.showMessageDialog(
                this, "Please enter all details!"
            );

            return;
        }

        // Prevent Duplicate Registration
        for (String[] r : registrations) {

            if (r[1].equalsIgnoreCase(roll) &&
                r[2].equals(event)) {

                JOptionPane.showMessageDialog(
                    this, "Already registered for this event!"
                );

                return;
            }
        }

        // Check Event Capacity
        if (countRegistrations(index) >= capacity[index]) {

            JOptionPane.showMessageDialog(
                this, "Sorry! No seats available."
            );

            return;
        }

        // Add Registration
        String[] data = {name, roll, event};

        registrations.add(data);

        displayAll();
        updateSeats();

        JOptionPane.showMessageDialog(
            this, "Registration Successful!"
        );

        nameField.setText("");
        rollField.setText("");
    }

    // Display All Participants
    void displayAll() {

        model.setRowCount(0);

        for (String[] r : registrations) {
            model.addRow(r);
        }
    }

    // Search Registrations
    void searchStudent() {

        String search = searchField.getText().trim().toLowerCase();

        model.setRowCount(0);

        for (String[] r : registrations) {

            if (r[0].toLowerCase().contains(search) ||
                r[1].toLowerCase().contains(search) ||
                r[2].toLowerCase().contains(search)) {

                model.addRow(r);
            }
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
            new CampusEventRegistration()
        );
    }
}
