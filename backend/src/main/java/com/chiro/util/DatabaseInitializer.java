package com.chiro.util;

import com.chiro.config.DatabaseConfig;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseInitializer {
    private final DatabaseConfig dbConfig;

    public DatabaseInitializer(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public void initializeDatabase() {
        try {
            String schema = readResourceFile("schema.sql");
            try (Connection conn = dbConfig.getConnection();
                 Statement stmt = conn.createStatement()) {
                
                // Split the SQL into individual statements and execute each one
                for (String sql : schema.split(";")) {
                    sql = sql.trim();
                    if (!sql.isEmpty()) {
                        stmt.execute(sql);
                    }
                }
                System.out.println("Database initialized successfully");
            }
        } catch (Exception e) {
            System.out.println("Error initializing database: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private String readResourceFile(String resourcePath) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read resource file: " + resourcePath, e);
        }
    }
} 