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

public class Auteur extends JFrame {

    private JPanel contentPane;
    private JTextField textFieldNomAjout;
    private JTextField textFieldPrenomAjout;
    private JTextField textFieldDateNaissanceAjout;
    private JTextField textFieldDescriptionAjout;
    private JTextField textFieldNomModif;
    private JTextField textFieldPrenomModif;
    private JTextField textFieldDateNaissanceModif;
    private JTextField textFieldDescriptionModif;
    private JComboBox<String> comboBoxAuteurs;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Auteur frame = new Auteur();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Auteur() {
        setTitle("Gestion des Auteurs");
        setBounds(325, 200, 900, 400);
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // Partie Ajout d'auteurs
        JLabel lblAjouterAuteur = new JLabel("Ajouter un Auteur");
        lblAjouterAuteur.setBounds(30, 20, 150, 20);
        contentPane.add(lblAjouterAuteur);

        JLabel lblNomAjout = new JLabel("Nom:");
        lblNomAjout.setBounds(30, 50, 50, 20);
        contentPane.add(lblNomAjout);

        textFieldNomAjout = new JTextField();
        textFieldNomAjout.setBounds(90, 50, 220, 20);
        contentPane.add(textFieldNomAjout);
        textFieldNomAjout.setColumns(10);

        JLabel lblPrenomAjout = new JLabel("Prénom:");
        lblPrenomAjout.setBounds(30, 80, 70, 20);
        contentPane.add(lblPrenomAjout);

        textFieldPrenomAjout = new JTextField();
        textFieldPrenomAjout.setBounds(90, 80, 220, 20);
        contentPane.add(textFieldPrenomAjout);
        textFieldPrenomAjout.setColumns(10);

        JLabel lblDateNaissanceAjout = new JLabel("Date de Naissance:");
        lblDateNaissanceAjout.setBounds(30, 110, 120, 20);
        contentPane.add(lblDateNaissanceAjout);

        textFieldDateNaissanceAjout = new JTextField();
        textFieldDateNaissanceAjout.setBounds(160, 110, 150, 20);
        contentPane.add(textFieldDateNaissanceAjout);
        textFieldDateNaissanceAjout.setColumns(10);

        JLabel lblDescriptionAjout = new JLabel("Description:");
        lblDescriptionAjout.setBounds(30, 140, 80, 20);
        contentPane.add(lblDescriptionAjout);

        textFieldDescriptionAjout = new JTextField();
        textFieldDescriptionAjout.setBounds(110, 140, 200, 20);
        contentPane.add(textFieldDescriptionAjout);
        textFieldDescriptionAjout.setColumns(10);

// Bouton pour ajouter un auteur
JButton btnAjouter = new JButton("Ajouter");
btnAjouter.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        // Récupération des informations saisies
        String nom = textFieldNomAjout.getText();
        String prenom = textFieldPrenomAjout.getText();
        String dateNaissance = textFieldDateNaissanceAjout.getText();
        String description = textFieldDescriptionAjout.getText();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie", "root", "root")) {
            // Vérifier si le nombre d'auteurs est inférieur à 4
            String countQuery = "SELECT COUNT(*) AS total FROM auteur";
            try (PreparedStatement countStatement = conn.prepareStatement(countQuery)) {
                ResultSet resultSet = countStatement.executeQuery();
                resultSet.next();
                int totalAuteurs = resultSet.getInt("total");

                if (totalAuteurs >= 4) {
                    JOptionPane.showMessageDialog(null, "Ajout impossible, nombre max. d'auteurs atteint.");
                } else {
                    // Vérifier si l'auteur existe déjà
                    String checkQuery = "SELECT * FROM auteur WHERE nom = ? AND prenom = ?";
                    try (PreparedStatement checkStatement = conn.prepareStatement(checkQuery)) {
                        checkStatement.setString(1, nom);
                        checkStatement.setString(2, prenom);
                        resultSet = checkStatement.executeQuery();

                        if (resultSet.next()) {
                            // Afficher un message d'erreur si l'auteur existe déjà
                            JOptionPane.showMessageDialog(null, "Cet auteur existe déjà");
                        } else {
                            // Insérer l'auteur dans la base de données
                            String insertQuery = "INSERT INTO auteur (nom, prenom, date_naissance, description) VALUES (?, ?, ?, ?)";
                            try (PreparedStatement insertStatement = conn.prepareStatement(insertQuery)) {
                                insertStatement.setString(1, nom);
                                insertStatement.setString(2, prenom);
                                insertStatement.setString(3, dateNaissance);
                                insertStatement.setString(4, description);

                                int rowsInserted = insertStatement.executeUpdate();
                                if (rowsInserted > 0) {
                                    JOptionPane.showMessageDialog(null, "L'auteur a été ajouté avec succès !");
                                    // Réinitialiser les champs de texte après l'ajout
                                    textFieldNomAjout.setText("");
                                    textFieldPrenomAjout.setText("");
                                    textFieldDateNaissanceAjout.setText("");
                                    textFieldDescriptionAjout.setText("");
                                    // Actualiser le menu déroulant
                                    comboBoxAuteurs.removeAllItems(); // Supprimer les éléments existants
                                    fillComboBoxAuteurs(); // Recharger les auteurs
                                } else {
                                    JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de l'auteur.");
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erreur de connexion à la base de données: " + ex.getMessage());
        }
    }
});
btnAjouter.setBounds(90, 170, 150, 25);
contentPane.add(btnAjouter);


        // Partie Modification d'auteurs
        JLabel lblModifierAuteur = new JLabel("Modifier ou Supprimer un Auteur");
        lblModifierAuteur.setBounds(350, 20, 270, 20);
        contentPane.add(lblModifierAuteur);

        comboBoxAuteurs = new JComboBox<String>();
        comboBoxAuteurs.setBounds(350, 50, 330, 20);
        fillComboBoxAuteurs(); // Remplir le menu déroulant avec les auteurs de la base de données
        contentPane.add(comboBoxAuteurs);

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

        JLabel lblDateNaissanceModif = new JLabel("Date de Naissance:");
        lblDateNaissanceModif.setBounds(350, 140, 120, 20);
        contentPane.add(lblDateNaissanceModif);

        textFieldDateNaissanceModif = new JTextField();
        textFieldDateNaissanceModif.setBounds(470, 140, 210, 20);
        contentPane.add(textFieldDateNaissanceModif);
        textFieldDateNaissanceModif.setColumns(10);

        JLabel lblDescriptionModif = new JLabel("Description:");
        lblDescriptionModif.setBounds(350, 170, 80, 20);
        contentPane.add(lblDescriptionModif);

        textFieldDescriptionModif = new JTextField();
        textFieldDescriptionModif.setBounds(430, 170, 250, 20);
        contentPane.add(textFieldDescriptionModif);
        textFieldDescriptionModif.setColumns(10);

// Bouton pour modifier un auteur
JButton btnModifier = new JButton("Modifier");
btnModifier.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        // Récupération des informations saisies
        String nom = textFieldNomModif.getText();
        String prenom = textFieldPrenomModif.getText();
        String dateNaissance = textFieldDateNaissanceModif.getText();
        String description = textFieldDescriptionModif.getText();
        String selectedAuteur = (String) comboBoxAuteurs.getSelectedItem();
        int autnum = Integer.parseInt(selectedAuteur.split(":")[0].trim());

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie", "root", "root")) {
            // Vérifier si l'auteur existe déjà
            String checkQuery = "SELECT * FROM auteur WHERE nom = ? AND prenom = ?";
            try (PreparedStatement checkStatement = conn.prepareStatement(checkQuery)) {
                checkStatement.setString(1, nom);
                checkStatement.setString(2, prenom);
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next() && resultSet.getInt("autnum") != autnum) {
                    // Afficher un message d'erreur si l'auteur existe déjà (en excluant l'auteur en cours de modification)
                    JOptionPane.showMessageDialog(null, "Cet auteur existe déjà");
                } else {
                    // Mettre à jour l'auteur dans la base de données
                    String updateQuery = "UPDATE auteur SET nom = ?, prenom = ?, date_naissance = ?, description = ? WHERE autnum = ?";
                    try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {
                        updateStatement.setString(1, nom);
                        updateStatement.setString(2, prenom);
                        updateStatement.setString(3, dateNaissance);
                        updateStatement.setString(4, description);
                        updateStatement.setInt(5, autnum);

                        int rowsUpdated = updateStatement.executeUpdate();
                        if (rowsUpdated > 0) {
                            JOptionPane.showMessageDialog(null, "L'auteur a été modifié avec succès !");
                            // Réinitialiser les champs de texte après la modification
                            textFieldNomModif.setText("");
                            textFieldPrenomModif.setText("");
                            textFieldDateNaissanceModif.setText("");
                            textFieldDescriptionModif.setText("");
                            comboBoxAuteurs.removeAllItems(); // Actualiser le menu déroulant
                            fillComboBoxAuteurs(); // Recharger les auteurs
                        } else {
                            JOptionPane.showMessageDialog(null, "Aucun auteur n'a été modifié.");
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erreur de connexion à la base de données: " + ex.getMessage());
        }
    }
});
btnModifier.setBounds(350, 200, 100, 25);
contentPane.add(btnModifier);

        // Bouton pour supprimer un auteur
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedAuteur = (String) comboBoxAuteurs.getSelectedItem();
                int autnum = Integer.parseInt(selectedAuteur.split(":")[0].trim());
                
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie", "root", "root")) {
                    String deleteQuery = "DELETE FROM auteur WHERE autnum = ?";
                    try (PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery)) {
                        deleteStatement.setInt(1, autnum);
                        
                        int rowsDeleted = deleteStatement.executeUpdate();
                        if (rowsDeleted > 0) {
                            JOptionPane.showMessageDialog(null,"L'auteur a été supprimé avec succès !");
                            comboBoxAuteurs.removeAllItems(); // Actualiser le menu déroulant
                            fillComboBoxAuteurs(); // Recharger les auteurs
                        } else {
                            JOptionPane.showMessageDialog(null,"Aucun auteur n'a été supprimé.");
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

    // Méthode pour remplir le menu déroulant avec les auteurs de la base de données
    private void fillComboBoxAuteurs() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie", "root", "root")) {
            String selectQuery = "SELECT autnum, nom, prenom, date_naissance, description FROM auteur";
            try (PreparedStatement selectStatement = conn.prepareStatement(selectQuery)) {
                ResultSet resultSet = selectStatement.executeQuery();
                while (resultSet.next()) {
                    int autnum = resultSet.getInt("autnum");
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    String date = resultSet.getString("date_naissance");
                    String desc = resultSet.getString("description");
                    comboBoxAuteurs.addItem(autnum + ": " + nom + " " + prenom+ ", "+date+ ", "+desc);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erreur lors du chargement des auteurs: " + ex.getMessage());
        }
    }
}
