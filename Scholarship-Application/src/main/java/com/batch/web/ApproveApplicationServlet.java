package com.batch.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;

@WebServlet("/admin/approve")
public class ApproveApplicationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int applicationId = Integer.parseInt(request.getParameter("id"));
        try {
            // Load MySQL JDBC driver class
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/scholarship", "root", "saketh");
            PreparedStatement ps = con.prepareStatement("UPDATE scholarship_applications SET application_status = 'Approved' WHERE id = ?");
            ps.setInt(1, applicationId);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                // Application status updated successfully
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                // No rows updated (application not found or already approved)
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
