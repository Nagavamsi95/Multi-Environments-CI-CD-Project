package com.nagavamsi.app;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.time.LocalDateTime;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String host = InetAddress.getLocalHost().getHostName();
        String server = getServletContext().getServerInfo();

        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html><html><head><title>Java Tomcat WebApp</title>");
        out.println("<style>body{font-family:Segoe UI,sans-serif;background:#10263b;color:#fff;display:flex;");
        out.println("justify-content:center;align-items:center;height:100vh;margin:0}");
        out.println(".box{background:#17334d;padding:40px;border-radius:12px;text-align:center}");
        out.println("h1{color:#f2a63b}</style></head><body><div class='box'>");
        out.println("<h1>Deployed Successfully!</h1>");
        out.println("<p>Deployed to Apache Tomcat with GitHub Actions</p>");
        out.println("<p>Server: " + server + "</p>");
        out.println("<p>Host: " + host + "</p>");
        out.println("<p>Server time: " + LocalDateTime.now() + "</p>");
        out.println("</div></body></html>");
    }
}
