package com.batch.web;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    // JDBC URL, email, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/scholarship";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "saketh";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve email and password from request parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Validate input (e.g., check if email and password are not empty)
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            response.sendRedirect("login.html"); // Redirect to login page if input is invalid
            return;
        }

        // Authenticate user against the database
        boolean isAuthenticated = authenticateUser(email, password);

        if (isAuthenticated) {
            // If authentication succeeds, create a session and redirect to index
            HttpSession session = request.getSession();
            session.setAttribute("email", email);
            response.sendRedirect("home");
        } else {
            // If authentication fails, redirect back to login page
            response.sendRedirect("login.html?error=true");
        }
    }

    private boolean authenticateUser(String email, String password) {
        // Query the database to check if the email and password match
    	try {
            // Load MySQL JDBC driver class
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next(); // Return true if a matching user is found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle database errors appropriately
            return false;
        }
    }
}
