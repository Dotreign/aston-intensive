package repository;

import context.UserAuthContext;
import entity.Link;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LinkRepository {

    private static LinkRepository instance;
    UserRepository userRepository;
    UserAuthContext userAuthContext;

    private LinkRepository() {
        this.userRepository = UserRepository.getInstance();
        this.userAuthContext = UserAuthContext.getInstance();
    }

    public static LinkRepository getInstance() {
        if (instance == null) {
            instance = new LinkRepository();
        }
        return instance;
    }

    public void create(Link link) {
        try (var connection = JdbcConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO links (full_url, short_url) VALUES (?, ?)");
            statement.setString(1, link.getFullUrl());
            statement.setString(2, link.getShortUrl());
            statement.executeUpdate();
            PreparedStatement statement2 = connection.prepareStatement("INSERT INTO users_links (user_id, url_id) VALUES (?, ?)");
            UUID userId = userRepository.getUserId(userAuthContext.getAuthenticatedUsername()).get();
            UUID linkId = getLinkId(link.getFullUrl()).get();
            statement2.setObject(1, userId);
            statement2.setObject(2, linkId);
            statement2.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Optional<UUID> getLinkId(String fullUrl) {
        try (var connection = JdbcConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM links WHERE full_url = ?");
            statement.setString(1, fullUrl);
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

    public Optional<Link> get(String shortUrl) {
        try (var connection = JdbcConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM links WHERE short_url = ?");
            statement.setString(1, shortUrl);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Link link = new Link();
                link.setFullUrl(resultSet.getString("full_url"));
                link.setShortUrl(resultSet.getString("short_url"));
                return Optional.of(link);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<List<Link>> getAll(String username) {
        try (var connection = JdbcConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT links.* " +
                    "FROM users " +
                    "JOIN users_links ON users.id = users_links.user_id " +
                    "JOIN links ON users_links.url_id = links.id " +
                    "WHERE users.name = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            List<Link> links = new ArrayList<>();
            while (resultSet.next()) {
                Link link = new Link();
                link.setFullUrl(resultSet.getString("full_url"));
                link.setShortUrl(resultSet.getString("short_url"));
                links.add(link);
            }
            return Optional.of(links);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void delete(String shortUrl) {
        try (var connection = JdbcConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM links WHERE short_url = ?");
            statement.setString(1, shortUrl);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Link link) {
        try (var connection = JdbcConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE links SET full_url = ? WHERE short_url = ?");
            statement.setString(1, link.getFullUrl());
            statement.setString(2, link.getShortUrl());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Optional<String> getFullUrlByShortLink(String shortLink) {
        try (var connection = JdbcConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT full_url FROM links WHERE short_url = ?");
            statement.setString(1, shortLink);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSet.getString("full_url"));
            }
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
