package com.chiro;

import com.chiro.config.DatabaseConfig;
import com.chiro.util.DatabaseInitializer;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize the database
            DatabaseConfig dbConfig = DatabaseConfig.getInstance();
            DatabaseInitializer initializer = new DatabaseInitializer(dbConfig);
            initializer.initializeDatabase();
            System.out.println("Database initialization completed successfully.");
        } catch (Exception e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
} 