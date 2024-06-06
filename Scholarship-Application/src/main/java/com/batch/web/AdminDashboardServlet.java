package com.batch.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the user is logged in as admin (you may need to implement admin authentication)
        boolean isAdmin = true; // Example: Check if the user is an admin

        if (isAdmin) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.println("["); // Start of JSON array

            try {
                // Load MySQL JDBC driver class
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/scholarship", "root", "saketh");
                PreparedStatement ps = con.prepareStatement("SELECT id, name, email, application_status AS status FROM scholarship_applications WHERE application_status = 'Pending'");
                ResultSet rs = ps.executeQuery();
                boolean first = true; // Flag to check if it's the first JSON object

                while (rs.next()) {
                    if (!first) {
                        out.println(","); // Add comma between JSON objects
                    }
                    out.println("{"); // Start of JSON object
                    out.println("\"id\": " + rs.getInt("id") + ",");
                    out.println("\"name\": \"" + rs.getString("name") + "\",");
                    out.println("\"email\": \"" + rs.getString("email") + "\",");
                    out.println("\"status\": \"" + rs.getString("status") + "\"");
                    out.println("}"); // End of JSON object
                    first = false;
                }
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            out.println("]"); // End of JSON array
        } else {
            // If not an admin, return unauthorized access message
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
