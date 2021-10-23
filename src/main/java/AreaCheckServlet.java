import com.deltaspace.webtwo.history.RequestHistory;
import com.deltaspace.webtwo.history.RequestRow;

import java.io.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import javax.servlet.*;
import javax.servlet.http.*;

public class AreaCheckServlet extends HttpServlet {
    private static Double convertNumber(String inputNumber) {
        try {
            return Double.parseDouble(inputNumber);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private static boolean checkTriangle(double x, double y, double r) {
        return x >= 0 && y >= 0 && y <= r-2*x;
    }

    private static boolean checkRectangle(double x, double y, double r) {
        return x >= 0 && y <= 0 && x <= r && y >= -r/2;
    }

    private static boolean checkCircle(double x, double y, double r) {
        return x <= 0 && y >= 0 && x*x+y*y <= r*r/4;
    }

    private static boolean checkHit(double x, double y, double r) {
        return checkTriangle(x, y, r) || checkRectangle(x, y, r) || checkCircle(x, y, r);
    }

    private static void sendError(PrintWriter writer, String errorMessage, String payload) {
        try {
            writer.println("<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<meta charset=\"utf-8\">" +
                    "<title>Ошибка</title>" +
                    "</head>" +
                    "<body>" +
                    "<h2>" + errorMessage + "</h2>" +
                    "<p>" + payload + "</p>" +
                    "</body>" +
                    "</html>");
        } finally {
            writer.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long startTime = System.nanoTime();
        PrintWriter writer = response.getWriter();
        String p = request.getReader().lines().collect(Collectors.joining());
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
            sendError(writer,"а где куки?!", p);
            return;
        }
        ServletContext servletContext = request.getServletContext();
        if (p.equals("totalreset")) {
            servletContext.setAttribute(uniqueid, new RequestHistory());
            return;
        }
        String[] parts = p.split("&");
        if (parts.length != 3) {
            sendError(writer, "POST payload не содержит ровно три куска данных", p);
            return;
        }
        Double x = null;
        Double y = null;
        Double r = null;
        boolean graph = false;
        for (String part : parts) {
            String[] tokens = part.split("=");
            if (tokens.length != 2) {
                sendError(writer, "Кусок данных не в формате \"name=value\"", p);
                return;
            }
            String name = tokens[0];
            String value = tokens[1];
            switch (name) {
                case "x-value": {
                    x = convertNumber(value);
                    break;
                }
                case "y-value": {
                    y = convertNumber(value);
                    break;
                }
                case "r-value": {
                    r = convertNumber(value);
                    break;
                }
                case "rg-value": {
                    r = convertNumber(value);
                    graph = true;
                    break;
                }
                default: {
                    sendError(writer, "Ошибка! Не надо "+name, p);
                    return;
                }
            }
        }
        if (x == null) {
            sendError(writer, "x неправильный!", p);
            return;
        }
        if (y == null) {
            sendError(writer, "y неправильный!", p);
            return;
        }
        if (r == null) {
            sendError(writer, "r неправильный!", p);
            return;
        }
        boolean hit = checkHit(x, y, r);
        Object requestHistoryOrNull = servletContext.getAttribute(uniqueid);
        RequestHistory requestHistory;
        if (requestHistoryOrNull == null) {
            requestHistory = new RequestHistory();
        } else {
            requestHistory = (RequestHistory)requestHistoryOrNull;
        }
        long endTime = System.nanoTime();
        RequestRow nextRow = new RequestRow(LocalDateTime.now(), (double)(endTime-startTime)/1000000, x, y, r, hit);
        requestHistory.addRow(nextRow);
        servletContext.setAttribute(uniqueid, requestHistory);
        try {
            if (graph) {
                writer.println(nextRow);
            } else {
                writer.println("<html>\n" +
                        "\n" +
                        "<head>\n" +
                        "    <meta charset=\"utf-8\">\n" +
                        "    <title>Лабораторная работа по веб-программированию №2</title>\n" +
                        "    <link rel=\"icon\" href=\"smile.ico\">\n" +
                        "    <link href=\"https://fonts.googleapis.com/css2?family=Cormorant&display=swap\" rel=\"stylesheet\">\n" +
                        "    <link href=\"main.css\" rel=\"stylesheet\">\n" +
                        "</head>\n" +
                        "\n" +
                        "<body>\n" +
                        "<table id=\"main-grid\">\n" +
                        "    <tr>\n" +
                        "        <td class=\"content-plate\" id=\"table-plate\" rowspan=\"2\">\n" +
                        "                <table id=\"result-table\">\n" +
                        "                    <tr class=\"table-header\">\n" +
                        "                        <th class=\"time-col\">Текущее время</th>\n" +
                        "                        <th class=\"time-col\">Время выполнения</th>\n" +
                        "                        <th class=\"coords-col\">X</th>\n" +
                        "                        <th class=\"coords-col\">Y</th>\n" +
                        "                        <th class=\"coords-col\">R</th>\n" +
                        "                        <th class=\"hit-col\">Попадание</th>\n" +
                        "                    </tr>\n" + nextRow + "\n" +
                        "                </table>\n" +
                        "        </td>\n" +
                        "    </tr>\n" +
                        "</table>\n" +
                        "<a id=\"urlback\" href=\"/lab2/main\">Потыкать ещё!</a>" +
                        "</body>\n" +
                        "\n" +
                        "</html>");
            }
        } finally {
            writer.close();
        }
    }
}
