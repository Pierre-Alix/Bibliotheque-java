import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

public class Adherent extends JFrame {

    private JPanel contentPane;
    private JTextField textFieldNomAjout;
    private JTextField textFieldPrenomAjout;
    private JTextField textFieldEmailAjout;
    private JTextField textFieldNomModif;
    private JTextField textFieldPrenomModif;
    private JTextField textFieldEmailModif;
    private JComboBox<String> comboBoxAdherents;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Adherent frame = new Adherent();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Adherent() {
        setTitle("Gestion des Adhérents");
 
        setBounds(325, 200, 900, 400);
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // Partie Ajout d'adhérents
        JLabel lblAjouterAdherent = new JLabel("Ajouter un Adhérent");
        lblAjouterAdherent.setBounds(30, 20, 150, 20);
        contentPane.add(lblAjouterAdherent);

        JLabel lblNomAjout = new JLabel("Nom:");
        lblNomAjout.setBounds(30, 50, 50, 20);
        contentPane.add(lblNomAjout);

        textFieldNomAjout = new JTextField();
        textFieldNomAjout.setBounds(90, 50, 150, 20);
        contentPane.add(textFieldNomAjout);
        textFieldNomAjout.setColumns(10);

        JLabel lblPrenomAjout = new JLabel("Prénom:");
        lblPrenomAjout.setBounds(30, 80, 70, 20);
        contentPane.add(lblPrenomAjout);

        textFieldPrenomAjout = new JTextField();
        textFieldPrenomAjout.setBounds(90, 80, 150, 20);
        contentPane.add(textFieldPrenomAjout);
        textFieldPrenomAjout.setColumns(10);

        JLabel lblEmailAjout = new JLabel("Email:");
        lblEmailAjout.setBounds(30, 110, 70, 20);
        contentPane.add(lblEmailAjout);

        textFieldEmailAjout = new JTextField();
        textFieldEmailAjout.setBounds(90, 110, 150, 20);
        contentPane.add(textFieldEmailAjout);
        textFieldEmailAjout.setColumns(10);

        // Bouton pour ajouter un adhérent
        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        // Récupération des informations saisies
        String nom = textFieldNomAjout.getText();
        String prenom = textFieldPrenomAjout.getText();
        String email = textFieldEmailAjout.getText();
        
        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(null, "Email invalide, veuillez vous assurer qu'il contient un @");
        } else {

                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie", "root", "root")) {
                    // Vérifier si l'adhérent existe déjà
                    String checkQuery = "SELECT * FROM adherent WHERE nom = ? AND prenom = ? AND email = ?";
                    try (PreparedStatement checkStatement = conn.prepareStatement(checkQuery)) {
                        checkStatement.setString(1, nom);
                        checkStatement.setString(2, prenom);
                        checkStatement.setString(3, email);
                        ResultSet resultSet = checkStatement.executeQuery();

                        if (resultSet.next()) {
                            // Afficher un message d'erreur si l'adhérent existe déjà
                            JOptionPane.showMessageDialog(null, "Cet utilisateur existe déjà");
                        } else {
                            // Insérer l'adhérent dans la base de données
                            String insertQuery = "INSERT INTO adherent (nom, prenom, email) VALUES (?, ?, ?)";
                            try (PreparedStatement insertStatement = conn.prepareStatement(insertQuery)) {
                                insertStatement.setString(1, nom);
                                insertStatement.setString(2, prenom);
                                insertStatement.setString(3, email);

                                int rowsInserted = insertStatement.executeUpdate();
                                if (rowsInserted > 0) {
                                    JOptionPane.showMessageDialog(null, "L'adhérent a été ajouté avec succès !");
                                    // Réinitialiser les champs de texte après l'ajout
                                    textFieldNomAjout.setText("");
                                    textFieldPrenomAjout.setText("");
                                    textFieldEmailAjout.setText("");
                                    // Actualiser le menu déroulant
                                    comboBoxAdherents.removeAllItems(); // Supprimer les éléments existants
                                    fillComboBoxAdherents(); // Recharger les adhérents
                                } else {
                                    JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de l'adhérent.");
                                }
                            }
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erreur de connexion à la base de données: " + ex.getMessage());
                }
            }
        }
        });
        btnAjouter.setBounds(90, 170, 100, 25);
        contentPane.add(btnAjouter);

        // Étiquette pour le titre
        JLabel lblModifierAdherent = new JLabel("Modifier ou Supprimer un Adhérent");
        lblModifierAdherent.setBounds(350, 20, 270, 20); // Position et taille de l'étiquette
        contentPane.add(lblModifierAdherent);

        // Menu déroulant pour sélectionner l'adhérent à modifier ou supprimer
        comboBoxAdherents = new JComboBox<String>();
        comboBoxAdherents.setBounds(350, 50, 330, 20); // Position et taille du menu déroulant
        fillComboBoxAdherents(); // Remplir le menu déroulant avec les adhérents de la base de données
        contentPane.add(comboBoxAdherents);

        // Étiquettes et champs de texte pour les informations de modification
        JLabel lblNomModif = new JLabel("Nom:");
        lblNomModif.setBounds(350, 80, 70, 20);
        contentPane.add(lblNomModif);

        textFieldNomModif = new JTextField();
        textFieldNomModif.setBounds(425, 80, 255, 20);
        contentPane.add(textFieldNomModif);
        textFieldNomModif.setColumns(10);

        JLabel lblPrenomModif = new JLabel("Prénom:");
        lblPrenomModif.setBounds(350, 110, 70, 20);
        contentPane.add(lblPrenomModif);

        textFieldPrenomModif = new JTextField();
        textFieldPrenomModif.setBounds(425, 110, 255, 20);
        contentPane.add(textFieldPrenomModif);
        textFieldPrenomModif.setColumns(10);

        JLabel lblEmailModif = new JLabel("Email:");
        lblEmailModif.setBounds(350, 140, 70, 20);
        contentPane.add(lblEmailModif);

        textFieldEmailModif = new JTextField();
        textFieldEmailModif.setBounds(425, 140, 255, 20);
        contentPane.add(textFieldEmailModif);
        textFieldEmailModif.setColumns(10);

        // Bouton pour modifier un adhérent
        JButton btnModifier = new JButton("Modifier");
        btnModifier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = textFieldEmailModif.getText();
                if (!email.contains("@")) {
                JOptionPane.showMessageDialog(null, "Email invalide, veillez à ce qu'il y'ait un @");
                } else {
                // Récupération des informations saisies
                String nom = textFieldNomModif.getText();
                String prenom = textFieldPrenomModif.getText();
                email = textFieldEmailModif.getText();
                String selectedAdherent = (String) comboBoxAdherents.getSelectedItem();
                int adhnum = Integer.parseInt(selectedAdherent.split(":")[0].trim());
                
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie", "root", "root")) {
                    String updateQuery = "UPDATE adherent SET nom = ?, prenom = ?, email = ? WHERE adhnum = ?";
                    try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {
                        updateStatement.setString(1, nom);
                        updateStatement.setString(2, prenom);
                        updateStatement.setString(3, email);
                        updateStatement.setInt(4, adhnum);
                        
                        int rowsUpdated = updateStatement.executeUpdate();
                        if (rowsUpdated > 0) {
                            JOptionPane.showMessageDialog(null,"L'adhérent a été modifié avec succès !");
                            textFieldNomModif.setText(""); // Réinitialiser les champs de texte après la modification
                            textFieldPrenomModif.setText("");
                            textFieldEmailModif.setText("");
                            comboBoxAdherents.removeAllItems(); // Actualiser le menu déroulant
                            fillComboBoxAdherents(); // Recharger les adhérents
                        } else {
                            JOptionPane.showMessageDialog(null,"Aucun adhérent n'a été modifié.");
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,"Erreur de connexion à la base de données: " + ex.getMessage());
                }
            }
         }
        });
        btnModifier.setBounds(350, 170, 100, 25);
        contentPane.add(btnModifier);

        // Bouton pour supprimer un adhérent
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedAdherent = (String) comboBoxAdherents.getSelectedItem();
                int adhnum = Integer.parseInt(selectedAdherent.split(":")[0].trim());
                
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie", "root", "root")) {
                    String deleteQuery = "DELETE FROM adherent WHERE adhnum = ?";
                    try (PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery)) {
                        deleteStatement.setInt(1, adhnum);
                        
                        int rowsDeleted = deleteStatement.executeUpdate();
                        if (rowsDeleted > 0) {
                            JOptionPane.showMessageDialog(null,"L'adhérent a été supprimé avec succès !");
                            comboBoxAdherents.removeAllItems(); // Actualiser le menu déroulant
                            fillComboBoxAdherents(); // Recharger les adhérents
                        } else {
                            JOptionPane.showMessageDialog(null,"Aucun adhérent n'a été supprimé.");
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,"Erreur de connexion à la base de données: " + ex.getMessage());
                }
            }
        });
        btnSupprimer.setBounds(700, 47, 100, 25);
        contentPane.add(btnSupprimer);
    }

    // Méthode pour remplir le menu déroulant avec les adhérents de la base de données
    private void fillComboBoxAdherents() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie", "root", "root")) {
            String selectQuery = "SELECT adhnum, nom, prenom, email FROM adherent";
            try (PreparedStatement selectStatement = conn.prepareStatement(selectQuery)) {
                ResultSet resultSet = selectStatement.executeQuery();
                while (resultSet.next()) {
                    int adhnum = resultSet.getInt("adhnum");
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    String email = resultSet.getString("email");
                    comboBoxAdherents.addItem(adhnum + ": " + nom + " " + prenom + ", " + email);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erreur lors du chargement des adhérents: " + ex.getMessage());
        }
    }
}