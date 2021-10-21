<%@ page import="com.deltaspace.webtwo.history.RequestHistory" %>
<%@ page import="com.deltaspace.webtwo.history.RequestRow" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="false" %>
<html>

<head>
    <meta charset="utf-8">
    <title>Лабораторная работа по веб-программированию №2</title>
    <link rel="icon" href="smile.ico">
    <link href="https://fonts.googleapis.com/css2?family=Cormorant&display=swap" rel="stylesheet">
    <link href="main.css" rel="stylesheet">
</head>

<body>
<table id="main-grid">
    <tr>
        <td id="header-plate" colspan="2">
            <span class="left-aligned">Вариант - 68168</span>
            <span class="right-aligned">ФИО: Гадеев Руслан Рустамович</br>Группа: P3214</span>
        </td>
    </tr>

    <tr>
        <td class="content-plate" id="table-plate" rowspan="2">
            <div class="plate-top">
                <h2 class="plate-top-title">Таблица</h2>
            </div>
            <div class="scroll-container">
                <table id="result-table">
                    <tr class="table-header">
                        <th class="time-col">Текущее время</th>
                        <th class="time-col">Время выполнения</th>
                        <th class="coords-col">X</th>
                        <th class="coords-col">Y</th>
                        <th class="coords-col">R</th>
                        <th class="hit-col">Попадание</th>
                    </tr>
                    <%
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
                        if (uniqueid != null) {
                            ServletContext servletContext = request.getServletContext();
                            Object requestHistoryOrNull = servletContext.getAttribute(uniqueid);
                            if (requestHistoryOrNull != null) {
                                RequestHistory requestHistory = (RequestHistory)requestHistoryOrNull;
                                for (RequestRow row : requestHistory.rows) {
                                    out.println(row.toString());
                                }
                            }
                        }
                    %>
                </table>
            </div>
        </td>

        <td class="content-plate" id="values-plate">
            <div class="plate-top">
                <h2 class="plate-top-title">Данные</h2>
            </div>
            <form id="input-form" action="" method="POST">
                <table id="input-grid">
                    <tr>
                        <td class="input-grid-label">
                            <label id="xlabel">X:</label>
                        </td>
                        <td class="input-grid-value">
                            <input type="hidden" name="x-value" id="x-value" value="">
                            <input class="x-button" id="x-button1" type="button" value="-4">
                            <input class="x-button" id="x-button2" type="button" value="-3">
                            <input class="x-button" id="x-button3" type="button" value="-2">
                            <input class="x-button" id="x-button4" type="button" value="-1">
                            <input class="x-button" id="x-button5" type="button" value="0">
                            <input class="x-button" id="x-button6" type="button" value="1">
                            <input class="x-button" id="x-button7" type="button" value="2">
                            <input class="x-button" id="x-button8" type="button" value="3">
                            <input class="x-button" id="x-button9" type="button" value="4">
                        </td>
                    </tr>
                    <tr>
                        <td class="input-grid-label">
                            <label for="y-textinput">Y:</label>
                        </td>
                        <td class="input-grid-value">
                            <input class="y-text" id="y-textinput" type="text" name="y-value" maxlength="10" autocomplete="on" placeholder="Нужно число в пределах от -5 до 5...">
                        </td>
                    </tr>
                    <tr>
                        <td class="input-grid-label">
                            <label for="r-textinput">R:</label>
                        </td>
                        <td class="input-grid-value">
                            <input class="y-text" id="r-textinput" type="text" name="r-value" maxlength="10" autocomplete="on" placeholder="Нужно число в пределах от 2 до 5...">
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <div class="buttons">
                                <input class="button" type="submit" id="sub" value="Отправить">
                                <input class="button" id="resetter" type="reset" value="Сбросить">
                                <input class="button" type="button" value="Всё удалить">
                            </div>
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
    <tr>
        <td class="content-plate" id="graph-plate">
            <div class="plate-top">
                <h2 class="plate-top-title">График</h2>
            </div>
            <div class="image-container">
                <img id="grofik" src="image.png">
                <div id="tochka"><img id="img-tochka" src="red.png"></div>
            </div>
        </td>
    </tr>
</table>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="main.js"></script>
</body>

</html>