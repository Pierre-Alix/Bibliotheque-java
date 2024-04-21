import java.sql.*;

public class App {
    public static void main(String[] args) {
        try {
            // Étape 1: Charger la classe du driver
            Class.forName("com.mysql.jdbc.Driver");
            // Étape 2: Créer l'objet de connexion
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "root");
            // Étape 3: Créer la base de données
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS librairie");
            stmt.executeUpdate("USE librairie");
            
            // Créer la table adhérent
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS adherent (adhnum INT PRIMARY KEY AUTO_INCREMENT, nom VARCHAR(255), prenom VARCHAR(255), email VARCHAR(255))");
            
            // Créer la table auteur
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS auteur (autnum INT PRIMARY KEY AUTO_INCREMENT, nom VARCHAR(255), prenom VARCHAR(255), date_naissance DATE, description TEXT)");
            
            // Créer la table livre
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS livre (isbn INT PRIMARY KEY AUTO_INCREMENT, titre VARCHAR(255), prix DECIMAL(10, 2), autnum INT, FOREIGN KEY (autnum) REFERENCES auteur(autnum))");
            
            // Créer la table emprunt
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS emprunt (id_emprunt INT PRIMARY KEY AUTO_INCREMENT, date_emprunt DATE, date_retour DATE, adhnum INT, isbn INT, FOREIGN KEY (adhnum) REFERENCES adherent(adhnum), FOREIGN KEY (isbn) REFERENCES livre(isbn))");
            
            System.out.println("Tables créées avec succès dans la base de données librairie.");

            // Étape 5: Fermer l'objet de connexion
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

