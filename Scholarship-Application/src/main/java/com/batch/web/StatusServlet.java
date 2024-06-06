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
import javax.servlet.http.HttpSession;

@WebServlet("/status")
public class StatusServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("email") != null) {
            String email = (String) session.getAttribute("email");
            try {
                // Load MySQL JDBC driver class
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/scholarship", "root", "saketh");
                PreparedStatement ps = con.prepareStatement("SELECT * FROM scholarship_applications WHERE email = ?");
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    // Retrieve application status
                    String applicationStatus = rs.getString("application_status");

                    // Write HTML response directly to the PrintWriter
                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Application Status</title>");
                    out.println("<style>");
                    out.println(".container {");
                    out.println("    width: 80%;");
                    out.println("    margin: 0 auto;");
                    out.println("    padding: 20px;");
                    out.println("    border: 1px solid #ccc;");
                    out.println("    border-radius: 5px;");
                    out.println("    background-color: #f9f9f9;");
                    out.println("}");
                    out.println("</style>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<div class='container'>");
                    out.println("<h1>Application Status</h1>");
                    out.println("<p>Email: " + email + "</p>");
                    out.println("<p>Status: " + applicationStatus + "</p>");
                    out.println("</div>");
                    out.println("</body>");
                    out.println("</html>");
                } else {
                    // No application found for the user
                    response.sendRedirect("no_application.html");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("error.html");
            }
        } else {
            // User not logged in
            response.sendRedirect("login.html");
        }
    }
}
