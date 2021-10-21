<%@ page import="history.RequestHistory" %>
<%@ page import="history.RequestRow" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="false" %>
<html>

<head>
    <meta charset="utf-8">
    <title>Лабораторная работа по веб-программированию №2</title>
    <link rel="icon" href="smile.ico">
    <link href="https://fonts.googleapis.com/css2?family=Cormorant&display=swap" rel="stylesheet">
    <style>
        body {
            font-size: 16px;
            font-family: "Cormorant", serif;
            background-color: #be9fed;
        }

        #main-grid {
            width: 100%;
            margin: auto;
            border-spacing: 1em;
        }

        #table-plate {
            width: 70%;
            border-radius: 8px;
        }

        #header-plate {
            padding: 1em 30px;
            font-size: 32px;
            color: #a3f7ff;
            background-color: #6100c9;
            border-radius: 8px;
        }

        .content-plate {
            padding: 0 0 20px;
            vertical-align: top;
            background-color: #ffffff;
            border-radius: 8px;
        }

        .plate-top {
            display: inline-block;
            width: 100%;
            margin: 0;
            padding: 0em 0;
            margin-bottom: 20px;
            text-align: center;
            color: #ffffff;
            background-color: #e33030;
            border-radius: 8px;
        }

        .plate-top-title {
            font-size: 20px;
            font-weight: normal;
        }

        .image-container {
            text-align: center;
        }

        #input-grid {
            width: 95%;
            margin: auto;
            text-align: center;
        }

        .input-grid-label {
            width: 12%;
            font-weight: bold;
        }

        .y-text {
            width: 75%;
            height: 20px;
            margin: 2% 0;
        }

        .text-error {
            border: 1px solid #ff0000;
        }

        .box-error {
            color: #ff0000;
        }

        .text-error::placeholder {
            color: #ff0000;
        }

        .form-subtext {
            padding-bottom: 2%;
            font-size: 16px;
        }

        .center-labeled {
            display: inline-block;
            position: relative;
            margin: 0 1% 5%;
            font-size: 16px;
        }

        .center-labeled label {
            position: absolute;
            top: 18px;
            left: 6px;
        }

        .center-labeled {
            left: 0;
        }

        .center-labeled .add-labeled {
            left: 7px;
        }

        .x-button {
            color: #ffffff;
            background-color: #ff5700;
            border: none;
            border-radius: 8px;
        }

        .x-button:hover {
            background-color: #ff003e;
        }

        .buttons {
            margin-top: 3%;
        }

        .buttons::before {
            content: "";
            display: block;
            height: 2px;
            width: 300px;
            margin: 0 auto 4%;
            background-color: #ed6161;
        }

        .button {
            height: 27px;
            width: 90px;
            color: #ffffff;
            background-color: #ed6161;
            border: none;
            border-radius: 8px;
        }

        .button:hover {
            background-color: #514284;
        }

        #table-plate {
            position: relative;
        }

        .scroll-container {
            position: absolute;
            bottom: 0;
            right: 0;
            top: 54px;
            width: 100%;
            overflow-y: scroll;
        }

        #result-table {
            width: 100%;
            text-align: center;
            border-collapse: collapse;
            border-radius: 8px;
        }

        #result-table tr {
            height: 2em;
        }

        #result-table tr:nth-child(2n-1) {
            background-color: #ed6161;
        }

        #result-table tr:nth-child(2n):hover {
            background-color: #e9e9e9;
        }

        #result-table tr:nth-child(2n-1):hover {
            background-color: #d8aad3;
        }

        #result-table tr.table-header:hover {
            background-color: #514284;
        }

        .coords-col {
            width: 12%;
        }

        .time-col {
            width: 25%;
        }

        .left-aligned {
            float: left;
            margin: 3.14%;
        }

        .right-aligned {
            float: right;
        }

        .y-text:focus {
            border-color: #ff3ddf;
            outline-style: none;
        }
    </style>
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
                <div id="tochka"><img src="//upload.wikimedia.org/wikipedia/commons/thumb/b/b4/Red_pog.png/9px-Red_pog.png"></div>
            </div>
        </td>
    </tr>
</table>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="main.js"></script>
</body>

</html>