package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class DatabaseService {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(DatabaseService.class);

    public void exec() {
        DBProperties properties = DBProperties.getProperties();
        try (Connection connection = DriverManager.getConnection(
                properties.getUrl(),
                properties.getUser(),
                properties.getPassword()
        )) {
            getStudents(connection);
            addStudent(connection);
            updateStudent(connection);
            transaction(connection);
        } catch (SQLException e) {
            LOGGER.info(e.getMessage(), e);
        }
    }

    private void getStudents(@NotNull Connection connection) throws
            SQLException {
        String query = "SELECT * FROM student";
        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    LOGGER.info(
                            "id: {}, name: {}, surname: {}, group_id: {}",
                            result.getInt("id"),
                            result.getString("name"),
                            result.getString("surname"),
                            result.getString("group_id")
                    );
                }
            }
        }
    }

    private void addStudent(@NotNull Connection connection) throws
            SQLException {
        String query = "INSERT INTO student (name, surname, group_id) VALUES
        ( ?, ?, ?)";
        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {
            statement.setString(1, "Александр");
            statement.setString(2, "Петров");
            statement.setLong(3, 1);
            statement.executeUpdate();
        }
    }

    private void updateStudent(@NotNull Connection connection) throws
            SQLException {
        String query = "UPDATE student SET name = ? WHERE id = ?";
        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {
            statement.setString(1, "Александр Третий");
            statement.setLong(2, 5);
            statement.execute();
        }
    }

    private void transaction(@NotNull Connection connection) throws
            SQLException {
        String query = "UPDATE student SET name = ? WHERE id = ?";
        connection.setAutoCommit(false);
        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {
            statement.setString(1, "Сергей непонятный");
            statement.setLong(2, 6);
            statement.execute();

            String example = null;
            example.toString();
            connection.commit();
        } catch (RuntimeException e) {
            LOGGER.info("Ошибка {}", e.getMessage());
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }

}
}
