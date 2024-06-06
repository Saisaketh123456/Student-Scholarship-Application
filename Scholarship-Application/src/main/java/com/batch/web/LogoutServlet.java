package com.batch.web;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Get the current session, if it exists

        if (session != null) {
            // Invalidate the session
            session.invalidate();
        }

        // Redirect to the login page
        response.sendRedirect("login.html");
    }
}

