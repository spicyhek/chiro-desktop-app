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

    public DatabaseInitializer() {
        this.dbConfig = DatabaseConfig.getInstance();
    }

    public void initializeDatabase() {
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Read the schema.sql file
            String schemaSql = readResourceFile("/schema.sql");
            
            // Execute the schema
            stmt.execute(schemaSql);
            
            System.out.println("Database initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private String readResourceFile(String resourcePath) {
        try (InputStream is = getClass().getResourceAsStream(resourcePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to read resource file: " + resourcePath, e);
        }
    }
} 