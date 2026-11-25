package activeRecord;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test unitaire verifiant le bon fonctionnement du patron Singleton / Factory
 * implemente dans DBConnection (Question 4 du TP).
 */
public class TestDBConnection {

    /**
     * Verifie qu'il n'existe qu'un seul objet connexion vers la base
     * meme si la connexion est demandee a plusieurs reprises (Patron Singleton),
     * et verifie que le type de la connexion est bien java.sql.Connection.
     */
    @Test
    public void testSingletonConnection() throws SQLException {
        // Demande de connexion a plusieurs reprises
        Connection c1 = DBConnection.getConnection();
        Connection c2 = DBConnection.getConnection();

        // Verification que les objets ne sont pas nuls
        assertNotNull(c1, "La première connexion ne doit pas être nulle");
        assertNotNull(c2, "La deuxième connexion ne doit pas être nulle");

        // Verification que le type de la connexion est bien java.sql.Connection
        assertTrue(c1 instanceof Connection, "L'objet c1 doit être du type java.sql.Connection");
        assertTrue(c2 instanceof Connection, "L'objet c2 doit être du type java.sql.Connection");

        // Verification qu'il n'existe qu'une seule instance de connexion (meme reference memoire)
        assertSame(c1, c2, "Deux appels a getConnection() doivent retourner exactement le meme objet connexion");
    }

    /**
     * Verifie qu'il est possible de changer la base fournie par la classe DBConnection
     * grace a setNomDB(String nomDB), et qu'une nouvelle connexion est bien creee a l'appel suivant
     * de getConnection().
     */
    @Test
    public void testChangementDeBase() throws SQLException {
        // Configuration sur la base initiale
        DBConnection.setNomDB("testpersonne");
        Connection c1 = DBConnection.getConnection();
        assertNotNull(c1, "La connexion a 'testpersonne' ne doit pas être nulle");

        // Changement de la base demandee vers une seconde base (par ex. 'test')
        DBConnection.setNomDB("test");
        Connection c2 = DBConnection.getConnection();
        assertNotNull(c2, "La connexion a 'test' ne doit pas être nulle");

        // Verification qu'une NOUVELLE connexion a ete creee suite a setNomDB
        assertNotSame(c1, c2, "Apres setNomDB, une nouvelle connexion distincte doit etre retournee");

        // Verification que les appels suivants retournent bien cette nouvelle connexion unique
        Connection c3 = DBConnection.getConnection();
        assertSame(c2, c3, "Les appels suivants doivent retourner la meme instance sur la nouvelle base");

        // Restauration de la base par defaut
        DBConnection.setNomDB("testpersonne");
    }
}
