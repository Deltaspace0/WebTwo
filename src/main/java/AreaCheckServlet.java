import history.RequestHistory;
import history.RequestRow;

import java.io.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import javax.servlet.*;
import javax.servlet.http.*;

public class AreaCheckServlet extends HttpServlet {
    private static Integer convertX(String inputX) {
        try {
            int x = (int)Double.parseDouble(inputX);
            return x;
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private static Double convertY(String inputY) {
        try {
            double y = Double.parseDouble(inputY);
            return y;
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private static Double convertR(String inputR) {
        try {
            double r = Double.parseDouble(inputR);
            return r;
        } catch (NumberFormatException exception) {
            return null;
        }
    }
//    private static Integer convertX(String inputX) {
//        try {
//            int x = Integer.parseInt(inputX);
//            if (x < -4 || x > 4) {
//                return null;
//            }
//            return x;
//        } catch (NumberFormatException exception) {
//            return null;
//        }
//    }
//
//    private static Double convertY(String inputY) {
//        try {
//            double y = Double.parseDouble(inputY);
//            if (y < -5 || y > 5) {
//                return null;
//            }
//            return y;
//        } catch (NumberFormatException exception) {
//            return null;
//        }
//    }
//
//    private static Double convertR(String inputR) {
//        try {
//            double r = Double.parseDouble(inputR);
//            if (r < 2 || r > 5) {
//                return null;
//            }
//            return r;
//        } catch (NumberFormatException exception) {
//            return null;
//        }
//    }

    private static boolean checkTriangle(int x, double y, double r) {
        return x >= 0 && y >= 0 && y <= r-2*x;
    }

    private static boolean checkRectangle(int x, double y, double r) {
        return x >= 0 && y <= 0 && x <= r && y >= -r/2;
    }

    private static boolean checkCircle(int x, double y, double r) {
        return x <= 0 && y >= 0 && x*x+y*y <= r*r/4;
    }

    private static boolean checkHit(int x, double y, double r) {
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
        String[] parts = p.split("&");
        if (parts.length != 3) {
            sendError(writer, "POST payload не содержит ровно три куска данных", p);
            return;
        }
        Integer x = null;
        Double y = null;
        Double r = null;
        for (String part : parts) {
            String[] tokens = part.split("=");
            if (tokens.length != 2) {
                sendError(writer, "Кусок данных не в формате \"name=value\"", p);
                return;
            }
            String name = tokens[0];
            String value = tokens[1];
            if (name.equals("x-value")) {
                x = convertX(value);
            }
            if (name.equals("y-value")) {
                y = convertY(value);
            }
            if (name.equals("r-value")) {
                r = convertR(value);
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
        boolean hit = checkHit(x, y, r);
        ServletContext servletContext = request.getServletContext();
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
            writer.println("<html>\n" +
                    "\n" +
                    "<head>\n" +
                    "    <meta charset=\"utf-8\">\n" +
                    "    <title>Лабораторная работа по веб-программированию №2</title>\n" +
                    "    <link rel=\"icon\" href=\"smile.ico\">\n" +
                    "    <link href=\"https://fonts.googleapis.com/css2?family=Cormorant&display=swap\" rel=\"stylesheet\">\n" +
                    "    <style>\n" +
                    "        body {\n" +
                    "            font-size: 16px;\n" +
                    "            font-family: \"Cormorant\", serif;\n" +
                    "            background-color: #be9fed;\n" +
                    "        }\n" +
                    "\n" +
                    "        #main-grid {\n" +
                    "            width: 100%;\n" +
                    "            margin: auto;\n" +
                    "            border-spacing: 1em;\n" +
                    "        }\n" +
                    "        #urlback {\n" +
                    "            font-size: 64px;\n" +
                    "            color: #ffffff;\n" +
                    "        }\n" +
                    "\n" +
                    "        #table-plate {\n" +
                    "            width: 70%;\n" +
                    "            border-radius: 8px;\n" +
                    "        }\n" +
                    "\n" +
                    "        #header-plate {\n" +
                    "            padding: 1em 30px;\n" +
                    "            font-size: 32px;\n" +
                    "            color: #a3f7ff;\n" +
                    "            background-color: #6100c9;\n" +
                    "            border-radius: 8px;\n" +
                    "        }\n" +
                    "\n" +
                    "        .content-plate {\n" +
                    "            padding: 0 0 20px;\n" +
                    "            vertical-align: top;\n" +
                    "            background-color: #ffffff;\n" +
                    "            border-radius: 8px;\n" +
                    "        }\n" +
                    "\n" +
                    "        .plate-top {\n" +
                    "            display: inline-block;\n" +
                    "            width: 100%;\n" +
                    "            margin: 0;\n" +
                    "            padding: 0em 0;\n" +
                    "            margin-bottom: 20px;\n" +
                    "            text-align: center;\n" +
                    "            color: #ffffff;\n" +
                    "            background-color: #e33030;\n" +
                    "            border-radius: 8px;\n" +
                    "        }\n" +
                    "\n" +
                    "        .plate-top-title {\n" +
                    "            font-size: 20px;\n" +
                    "            font-weight: normal;\n" +
                    "        }\n" +
                    "\n" +
                    "        .image-container {\n" +
                    "            text-align: center;\n" +
                    "        }\n" +
                    "\n" +
                    "        #input-grid {\n" +
                    "            width: 95%;\n" +
                    "            margin: auto;\n" +
                    "            text-align: center;\n" +
                    "        }\n" +
                    "\n" +
                    "        .input-grid-label {\n" +
                    "            width: 12%;\n" +
                    "            font-weight: bold;\n" +
                    "        }\n" +
                    "\n" +
                    "        .y-text {\n" +
                    "            width: 75%;\n" +
                    "            height: 20px;\n" +
                    "            margin: 2% 0;\n" +
                    "        }\n" +
                    "\n" +
                    "        .text-error {\n" +
                    "            border: 1px solid #ff0000;\n" +
                    "        }\n" +
                    "\n" +
                    "        .box-error {\n" +
                    "            color: #ff0000;\n" +
                    "        }\n" +
                    "\n" +
                    "        .text-error::placeholder {\n" +
                    "            color: #ff0000;\n" +
                    "        }\n" +
                    "\n" +
                    "        .form-subtext {\n" +
                    "            padding-bottom: 2%;\n" +
                    "            font-size: 16px;\n" +
                    "        }\n" +
                    "\n" +
                    "        .center-labeled {\n" +
                    "            display: inline-block;\n" +
                    "            position: relative;\n" +
                    "            margin: 0 1% 5%;\n" +
                    "            font-size: 16px;\n" +
                    "        }\n" +
                    "\n" +
                    "        .center-labeled label {\n" +
                    "            position: absolute;\n" +
                    "            top: 18px;\n" +
                    "            left: 6px;\n" +
                    "        }\n" +
                    "\n" +
                    "        .center-labeled {\n" +
                    "            left: 0;\n" +
                    "        }\n" +
                    "\n" +
                    "        .center-labeled .add-labeled {\n" +
                    "            left: 7px;\n" +
                    "        }\n" +
                    "\n" +
                    "        .x-button {\n" +
                    "            color: #ffffff;\n" +
                    "            background-color: #ff5700;\n" +
                    "            border: none;\n" +
                    "            border-radius: 8px;\n" +
                    "        }\n" +
                    "\n" +
                    "        .x-button:hover {\n" +
                    "            background-color: #ff003e;\n" +
                    "        }\n" +
                    "\n" +
                    "        .buttons {\n" +
                    "            margin-top: 3%;\n" +
                    "        }\n" +
                    "\n" +
                    "        .buttons::before {\n" +
                    "            content: \"\";\n" +
                    "            display: block;\n" +
                    "            height: 2px;\n" +
                    "            width: 300px;\n" +
                    "            margin: 0 auto 4%;\n" +
                    "            background-color: #ed6161;\n" +
                    "        }\n" +
                    "\n" +
                    "        .button {\n" +
                    "            height: 27px;\n" +
                    "            width: 90px;\n" +
                    "            color: #ffffff;\n" +
                    "            background-color: #ed6161;\n" +
                    "            border: none;\n" +
                    "            border-radius: 8px;\n" +
                    "        }\n" +
                    "\n" +
                    "        .button:hover {\n" +
                    "            background-color: #514284;\n" +
                    "        }\n" +
                    "\n" +
                    "        #table-plate {\n" +
                    "            position: relative;\n" +
                    "        }\n" +
                    "\n" +
                    "        .scroll-container {\n" +
                    "            position: absolute;\n" +
                    "            bottom: 0;\n" +
                    "            right: 0;\n" +
                    "            top: 54px;\n" +
                    "            width: 100%;\n" +
                    "            overflow-y: scroll;\n" +
                    "        }\n" +
                    "\n" +
                    "        #result-table {\n" +
                    "            width: 100%;\n" +
                    "            text-align: center;\n" +
                    "            border-collapse: collapse;\n" +
                    "            border-radius: 8px;\n" +
                    "        }\n" +
                    "\n" +
                    "        #result-table tr {\n" +
                    "            height: 2em;\n" +
                    "        }\n" +
                    "\n" +
                    "        #result-table tr:nth-child(2n-1) {\n" +
                    "            background-color: #ed6161;\n" +
                    "        }\n" +
                    "\n" +
                    "        #result-table tr:nth-child(2n):hover {\n" +
                    "            background-color: #e9e9e9;\n" +
                    "        }\n" +
                    "\n" +
                    "        #result-table tr:nth-child(2n-1):hover {\n" +
                    "            background-color: #d8aad3;\n" +
                    "        }\n" +
                    "\n" +
                    "        #result-table tr.table-header:hover {\n" +
                    "            background-color: #514284;\n" +
                    "        }\n" +
                    "\n" +
                    "        .coords-col {\n" +
                    "            width: 12%;\n" +
                    "        }\n" +
                    "\n" +
                    "        .time-col {\n" +
                    "            width: 25%;\n" +
                    "        }\n" +
                    "\n" +
                    "        .left-aligned {\n" +
                    "            float: left;\n" +
                    "            margin: 3.14%;\n" +
                    "        }\n" +
                    "\n" +
                    "        .right-aligned {\n" +
                    "            float: right;\n" +
                    "        }\n" +
                    "\n" +
                    "        .y-text:focus {\n" +
                    "            border-color: #ff3ddf;\n" +
                    "            outline-style: none;\n" +
                    "        }\n" +
                    "    </style>\n" +
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
        } finally {
            writer.close();
        }
    }
}
