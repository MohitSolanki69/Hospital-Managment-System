package hospital.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SearchRoom extends JFrame {

    JTable table;
    JComboBox<String> availabilityFilter;

    SearchRoom() {
        setTitle("Search Rooms");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Title
        JLabel heading = new JLabel("Search Rooms by Availability");
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setFont(new Font("Tahoma", Font.BOLD, 22));
        heading.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(heading, BorderLayout.NORTH);

        // Table
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel with ComboBox and Button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        JLabel filterLabel = new JLabel("Show Rooms:");
        bottomPanel.add(filterLabel);

        availabilityFilter = new JComboBox<>(new String[]{"Available", "Occupied"});
        bottomPanel.add(availabilityFilter);

        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(new Color(109, 164, 170));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
        bottomPanel.add(searchBtn);

        JButton back = new JButton("Close");
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.setFont(new Font("Tahoma", Font.BOLD, 14));
        back.addActionListener(e -> dispose());
        bottomPanel.add(back);

        add(bottomPanel, BorderLayout.SOUTH);

        // Button Action
        searchBtn.addActionListener(e -> loadFilteredRooms());

        // Load default on start
        loadFilteredRooms();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadFilteredRooms() {
        try {
            conn c = new conn();
            String selectedAvailability = (String) availabilityFilter.getSelectedItem();
            String query = "SELECT room_no, availability FROM Room WHERE availability = '" + selectedAvailability + "'";
            ResultSet rs = c.statement.executeQuery(query);

            ResultSetMetaData metaData = rs.getMetaData();
            int colCount = metaData.getColumnCount();
            String[] colNames = new String[colCount];
            for (int i = 1; i <= colCount; i++) {
                colNames[i - 1] = metaData.getColumnName(i);
            }

            DefaultTableModel model = new DefaultTableModel(colNames, 0);
            while (rs.next()) {
                Object[] row = new Object[colCount];
                for (int i = 1; i <= colCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

            table.setModel(model);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading room data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SearchRoom();
    }
}
