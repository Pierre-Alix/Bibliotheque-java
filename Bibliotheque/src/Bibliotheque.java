import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Bibliotheque {

    private JFrame frame;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Bibliotheque window = new Bibliotheque();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Bibliotheque() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Gestion de la Bibliothèque"); // Ajout du titre de la fenêtre
        frame.setBounds(325, 200, 900, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        JButton btnGestionLivres = new JButton("Gestion des Livres");
        btnGestionLivres.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Action à effectuer lors du clic sur "Gestion des livres"
                Livre livreWindow = new Livre();
                livreWindow.setVisible(true);
            }
        });
        btnGestionLivres.setBounds(300, 14, 220, 29);
        frame.getContentPane().add(btnGestionLivres);
        
        JButton btnGestionAdherents = new JButton("Gestion des Adhérents");
        btnGestionAdherents.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Action à effectuer lors du clic sur "Gestion des adhérents"
                Adherent adherentWindow = new Adherent();
                adherentWindow.setVisible(true);
            }
        });
        btnGestionAdherents.setBounds(300, 65, 220, 29);
        frame.getContentPane().add(btnGestionAdherents);
        
        JButton btnGestionAuteurs = new JButton("Gestion des Auteurs");
        btnGestionAuteurs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Action à effectuer lors du clic sur "Gestion des auteurs"
                Auteur auteurWindow = new Auteur();
                auteurWindow.setVisible(true);
            }
        });
        btnGestionAuteurs.setBounds(300, 116, 220, 29);
        frame.getContentPane().add(btnGestionAuteurs);
        
        JButton btnRetraitLivre = new JButton("Retrait d'un Livre");
        btnRetraitLivre.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Action à effectuer lors du clic sur "Retrait d'un livre"
                Retrait retraitWindow = new Retrait();
                retraitWindow.setVisible(true);
            }
        });
        btnRetraitLivre.setBounds(300, 167, 220, 29);
        frame.getContentPane().add(btnRetraitLivre);

        JButton btnRetourLivre = new JButton("Retour d'un Livre");
        btnRetourLivre.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Action à effectuer lors du clic sur "Retour d'un livre"
                Retour retourWindow = new Retour();
                retourWindow.setVisible(true);
            }
        });
        btnRetourLivre.setBounds(300, 218, 220, 29); // Positionnez le bouton "Retour d'un livre" à côté du bouton "Retrait d'un livre"
        frame.getContentPane().add(btnRetourLivre);
    }
}
