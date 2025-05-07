/*
package com.chiro.config;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DatabaseConfig {
    private static final String DEFAULT_DB_URL = "jdbc:sqlite:chiropractic.db";
    private static DatabaseConfig instance;
    private String dbUrl;
    
    private DatabaseConfig() {
        this(DEFAULT_DB_URL);
    }

    private DatabaseConfig(String dbUrl) {
        this.dbUrl = dbUrl;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load SQLite JDBC driver", e);
        }
    }
    
    public static DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    public static DatabaseConfig getInstance(String dbUrl) {
        if (instance == null) {
            instance = new DatabaseConfig(dbUrl);
        } else {
            instance.dbUrl = dbUrl;
        }
        return instance;
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl);
    }
}
 */