import activeRecord.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test unitaire verifiant le bon fonctionnement du patron Singleton / Factory
 * implemente dans DBConnection (Question 4 du TP).
 */
public class TestDBConnection {

    @BeforeEach
    public void setUp() throws SQLException {
        // Reinitialise la base par defaut avant chaque test
        DBConnection.setNomDB("testpersonne");
    }

    @Test
    public void testSingletonConnection() throws SQLException {
        // Demande de connexion a plusieurs reprises
        Connection c1 = DBConnection.getConnection();
        Connection c2 = DBConnection.getConnection();

        // Verification qu'il n'existe qu'une seule instance de connexion (meme reference memoire)
        assertSame(c1, c2);
    }

    @Test
    public void testChangementDeBase() throws SQLException {
        Connection c1 = DBConnection.getConnection();

        // Changement de la base demandee vers une seconde base
        DBConnection.setNomDB("test");
        Connection c2 = DBConnection.getConnection();

        // NOUVELLE connexion creee suite a setNomDB
        assertNotSame(c1, c2);

        // Verification que les appels suivants retournent bien cette nouvelle connexion unique
        Connection c3 = DBConnection.getConnection();
        assertSame(c2, c3);
    }

    @Test
    public void testChangementDeNom() throws SQLException {
        Connection c1 = DBConnection.getConnection();
        // Verification que la base initiale est bien testpersonne
        assertEquals("testpersonne", c1.getCatalog());

        // Changement du nom de la base
        DBConnection.setNomDB("test");
        Connection c2 = DBConnection.getConnection();

        // Verification que la nouvelle connexion pointe bien vers la nouvelle base
        assertEquals("test", c2.getCatalog());
    }
}
