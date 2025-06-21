package exhibition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;

public class ExhibitionForm extends JFrame {
    private JTextField regIDField, nameField, facultyField, titleField, contactField, emailField, imagePathField;
    private JLabel imageLabel;
    private JButton registerBtn, searchBtn, updateBtn, deleteBtn, clearBtn, exitBtn, uploadImageBtn;

    public ExhibitionForm() {
        setTitle("Exhibition Registration System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JLabel regIDLabel = new JLabel("Registration ID:");
        JLabel nameLabel = new JLabel("Student Name:");
        JLabel facultyLabel = new JLabel("Faculty:");
        JLabel titleLabel = new JLabel("Project Title:");
        JLabel contactLabel = new JLabel("Contact Number:");
        JLabel emailLabel = new JLabel("Email Address:");
        JLabel imagePathLabel = new JLabel("Image Path:");
        JLabel imagePreviewLabel = new JLabel("Image Preview:");

        regIDField = new JTextField(20);
        nameField = new JTextField(20);
        facultyField = new JTextField(20);
        titleField = new JTextField(20);
        contactField = new JTextField(20);
        emailField = new JTextField(20);
        imagePathField = new JTextField(20);
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(100, 100));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        uploadImageBtn = new JButton("Upload Image");
        registerBtn = new JButton("Register");
        searchBtn = new JButton("Search");
        updateBtn = new JButton("Update");
        deleteBtn = new JButton("Delete");
        clearBtn = new JButton("Clear");
        exitBtn = new JButton("Exit");

        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                .addComponent(regIDLabel)
                .addComponent(nameLabel)
                .addComponent(facultyLabel)
                .addComponent(titleLabel)
                .addComponent(contactLabel)
                .addComponent(emailLabel)
                .addComponent(imagePathLabel)
                .addComponent(imagePreviewLabel)
                .addComponent(registerBtn))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(regIDField)
                .addComponent(nameField)
                .addComponent(facultyField)
                .addComponent(titleField)
                .addComponent(contactField)
                .addComponent(emailField)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(imagePathField)
                    .addComponent(uploadImageBtn))
                .addComponent(imageLabel)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(searchBtn)
                    .addComponent(updateBtn)
                    .addComponent(deleteBtn)
                    .addComponent(clearBtn)
                    .addComponent(exitBtn)))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(regIDLabel)
                .addComponent(regIDField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(nameLabel)
                .addComponent(nameField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(facultyLabel)
                .addComponent(facultyField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(titleLabel)
                .addComponent(titleField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(contactLabel)
                .addComponent(contactField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(emailLabel)
                .addComponent(emailField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(imagePathLabel)
                .addComponent(imagePathField)
                .addComponent(uploadImageBtn))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(imagePreviewLabel)
                .addComponent(imageLabel))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(registerBtn)
                .addComponent(searchBtn)
                .addComponent(updateBtn)
                .addComponent(deleteBtn)
                .addComponent(clearBtn)
                .addComponent(exitBtn))
        );

        add(panel);
        addListeners();
        setVisible(true);
    }

    private void addListeners() {
        uploadImageBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                imagePathField.setText(file.getAbsolutePath());
                imageLabel.setIcon(new ImageIcon(new ImageIcon(file.getAbsolutePath()).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
            }
        });

        registerBtn.addActionListener(e -> {
            try (Connection conn = DBConnect.getConnection()) {
                String sql = "INSERT INTO Participants VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, regIDField.getText());
                pst.setString(2, nameField.getText());
                pst.setString(3, facultyField.getText());
                pst.setString(4, titleField.getText());
                pst.setString(5, contactField.getText());
                pst.setString(6, emailField.getText());
                pst.setString(7, imagePathField.getText());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Registered Successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        searchBtn.addActionListener(e -> {
            try (Connection conn = DBConnect.getConnection()) {
                String sql = "SELECT * FROM Participants WHERE RegistrationID=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, regIDField.getText());
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    nameField.setText(rs.getString("StudentName"));
                    facultyField.setText(rs.getString("Faculty"));
                    titleField.setText(rs.getString("ProjectTitle"));
                    contactField.setText(rs.getString("ContactNumber"));
                    emailField.setText(rs.getString("EmailAddress"));
                    imagePathField.setText(rs.getString("ImagePath"));
                    File imgFile = new File(rs.getString("ImagePath"));
                    if (imgFile.exists()) {
                        imageLabel.setIcon(new ImageIcon(new ImageIcon(imgFile.getAbsolutePath()).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Record not found!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        updateBtn.addActionListener(e -> {
            try (Connection conn = DBConnect.getConnection()) {
                String sql = "UPDATE Participants SET StudentName=?, Faculty=?, ProjectTitle=?, ContactNumber=?, EmailAddress=?, ImagePath=? WHERE RegistrationID=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, nameField.getText());
                pst.setString(2, facultyField.getText());
                pst.setString(3, titleField.getText());
                pst.setString(4, contactField.getText());
                pst.setString(5, emailField.getText());
                pst.setString(6, imagePathField.getText());
                pst.setString(7, regIDField.getText());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Updated Successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        deleteBtn.addActionListener(e -> {
            try (Connection conn = DBConnect.getConnection()) {
                String sql = "DELETE FROM Participants WHERE RegistrationID=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, regIDField.getText());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Deleted Successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        clearBtn.addActionListener(e -> {
            regIDField.setText(""); nameField.setText("");
            facultyField.setText(""); titleField.setText("");
            contactField.setText(""); emailField.setText("");
            imagePathField.setText(""); imageLabel.setIcon(null);
        });

        exitBtn.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExhibitionForm::new);
    }
}