package com.batch.web;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    // JDBC URL, email, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/scholarship";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "saketh";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve user input from request parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        System.out.println(email);
        // Validate input (e.g., check if email is not empty)
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            response.sendRedirect("register.html"); // Redirect to registration page if input is invalid
            return;
        }

        // Store user information in the database

        try {
            // Load MySQL JDBC driver class
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "INSERT INTO users (email, password) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle database errors appropriately
            System.out.println("error"); // Redirect to an error page if database operation fails
            return;
        }

        // Redirect to a confirmation page after successful registration
        response.sendRedirect("login.html");
    }
}
