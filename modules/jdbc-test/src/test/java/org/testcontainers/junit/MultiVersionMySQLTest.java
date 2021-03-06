package org.testcontainers.junit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.testcontainers.containers.MySQLContainer;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.rnorth.visibleassertions.VisibleAssertions.assertEquals;

@RunWith(Parameterized.class)
public class MultiVersionMySQLTest extends AbstractContainerDatabaseTest {

    @Parameterized.Parameters(name = "{0}")
    public static String[] params() {
        return new String[]{"5.5.62", "5.6.42", "5.7.26", "8.0.16"};
    }

    private final String version;

    public MultiVersionMySQLTest(String version) {
        this.version = version;
    }

    @Test
    public void versionCheckTest() throws SQLException {
        try (final MySQLContainer container = new MySQLContainer<>("mysql:" + version)) {
            container.start();
            final ResultSet resultSet = performQuery(container, "SELECT VERSION()");
            final String resultSetString = resultSet.getString(1);

            assertEquals("The database version can be set using a container rule parameter", version, resultSetString);
        }
    }
}
