package com.batch.web;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.simple.JSONObject;

import java.sql.*;

@WebServlet("/apply")
public class ApplyServlet extends HttpServlet {
	
    // Database connection parameters
    String url = "jdbc:mysql://localhost:3306/scholarship";
    String username = "root";
    String password = "saketh";
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean hasApplied = false;
		
		HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM scholarship_applications WHERE email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    hasApplied = true;
                    System.out.println(hasApplied);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.html");
            return;
        }
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("applied", hasApplied);

        // Send the JSON response
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.println(jsonResponse.toString());
}
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form parameters
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String usn = request.getParameter("usn");
        String university = request.getParameter("university");
        String college = request.getParameter("college");
        String degree = request.getParameter("degree");
        String course = request.getParameter("course");
        int semester = Integer.parseInt(request.getParameter("semester"));
        double cgpa = Double.parseDouble(request.getParameter("cgpa"));
        
   	 // Retrieve email from HttpSession
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        
        // Insert data into database
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = con.prepareStatement("INSERT INTO scholarship_applications "
                    + "(name, phone, usn, university, college, degree, course, semester, cgpa, email) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, usn);
            ps.setString(4, university);
            ps.setString(5, college);
            ps.setString(6, degree);
            ps.setString(7, course);
            ps.setInt(8, semester);
            ps.setDouble(9, cgpa);
            ps.setString(10, email);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                response.sendRedirect("home");
            } else {
                response.sendRedirect("error.html");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.html");
        }
    }
}
