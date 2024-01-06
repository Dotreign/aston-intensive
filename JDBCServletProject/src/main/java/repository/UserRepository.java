package repository;

import entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.UUID;

public class UserRepository {

    private static UserRepository instance;

    private UserRepository() {

    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public Optional<User> get(String username) {
        try (var connection = JdbcConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE name = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setUsername(username);
                user.setPassword(resultSet.getString("password"));
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<UUID> getUserId(String username) {
        try (var connection = JdbcConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM users WHERE name = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(UUID.fromString(resultSet.getString("id")));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void update(User user) {
        try (var connection = JdbcConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE users SET password = ? WHERE name = ?");
            statement.setString(1, user.getPassword());
            statement.setString(2, user.getUsername());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String username) {
        try (var connection = JdbcConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE name = ?");
            statement.setString(1, username);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create(User user) {
        try (var connection = JdbcConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users (name, password) VALUES (?, ?)");
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
