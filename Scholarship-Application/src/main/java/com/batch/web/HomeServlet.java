package com.batch.web;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Get the current session, if it exists

        if (session != null && session.getAttribute("email") != null) {
            // User is logged in, show the home page
            RequestDispatcher dispatcher = request.getRequestDispatcher("home.html");
            dispatcher.forward(request, response);
        } else {
            // User is not logged in, redirect to the login page
            response.sendRedirect("login.html");
        }
    }
}

