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

    private Connection c1;
    private Connection c2;

    @BeforeEach
    public void setUp() throws SQLException {
        // Reinitialise la base par defaut et effectue les connexions avant chaque test
        DBConnection.setNomDB("testpersonne");
        c1 = DBConnection.getConnection();
        c2 = DBConnection.getConnection();
    }

    @Test
    public void testSingletonConnection() {
        // Verification qu'il n'existe qu'une seule instance de connexion (meme reference memoire)
        assertSame(c1, c2);
    }

    @Test
    public void testChangementDeBase() throws SQLException {
        // Changement de la base demandee vers une seconde base
        DBConnection.setNomDB("test");
        Connection c3 = DBConnection.getConnection();

        // NOUVELLE connexion creee suite a setNomDB
        assertNotSame(c1, c3);

        // Verification que les appels suivants retournent bien cette nouvelle connexion unique
        Connection c4 = DBConnection.getConnection();
        assertSame(c3, c4);
    }

    @Test
    public void testChangementDeNom() throws SQLException {
        // Verification que la base initiale est bien testpersonne
        assertEquals("testpersonne", c1.getCatalog());

        // Changement du nom de la base
        DBConnection.setNomDB("test");
        Connection c3 = DBConnection.getConnection();

        // Verification que la nouvelle connexion pointe bien vers la nouvelle base
        assertEquals("test", c3.getCatalog());
    }
}
