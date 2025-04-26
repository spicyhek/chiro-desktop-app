package com.chiro.util;

import com.chiro.config.DatabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    
    private final DatabaseConfig dbConfig;

    public DatabaseInitializer() {
        this.dbConfig = DatabaseConfig.getInstance();
    }

    public DatabaseInitializer(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public void initializeDatabase() {
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Read the schema.sql file
            String schemaSql = readResourceFile("schema.sql");
            
            // Split the SQL into individual statements and execute each one
            for (String sql : schemaSql.split(";")) {
                sql = sql.trim();
                if (!sql.isEmpty()) {
                    stmt.execute(sql);
                }
            }
            
            logger.info("Database initialized successfully");
        } catch (Exception e) {
            logger.error("Error initializing database: {}", e.getMessage());
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private String readResourceFile(String resourcePath) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            if (is == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to read resource file: " + resourcePath, e);
        }
    }
} 