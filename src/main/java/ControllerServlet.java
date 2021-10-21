import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ControllerServlet extends HttpServlet {
    private static final RandomString cookieGenerator = new RandomString();

    private void cookieChecker(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String uniqueid = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("uniqueid")) {
                    uniqueid = cookie.getValue();
                    break;
                }
            }
        }
        if (uniqueid == null) {
            uniqueid = cookieGenerator.nextString();
        }
        response.addCookie(new Cookie("uniqueid", uniqueid));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        cookieChecker(request, response);
        ServletContext servletContext = request.getServletContext();
        servletContext.getRequestDispatcher("/WEB-INF/main.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        cookieChecker(request, response);
        request.getServletContext().getRequestDispatcher("/model").forward(request, response);
    }
}
