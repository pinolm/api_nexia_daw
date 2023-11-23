package cat.nexia.spring.mail;

import cat.nexia.spring.models.Reserva;

public class StringMails {
        public static String mailReserva = "<!DOCTYPE html>\r\n"
                        + "<html lang=\"es\">\r\n"
                        + "\r\n"
                        + "<head>\r\n"
                        + "  <meta charset=\"UTF-8\" />\r\n"
                        + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\r\n"
                        + "  <link rel=\"stylesheet\" href=\"style.css\" />\r\n"
                        + "  <title>Browser</title>\r\n"
                        + "  <style>\r\n"
                        + "    body {\r\n"
                        + "    	color:black;\r\n"
                        + "  		margin: 20px;\r\n"
                        + "	}\r\n"
                        + "  </style>\r\n"
                        + "</head>\r\n"
                        + "\r\n"
                        + "<body style=\"margin: 20px;\">\r\n"
                        + "  <h1>\r\n"
                        + "    <img src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAYAAABw4pVUAAAAGXRFWHRTb2Z0d2FyZQBBZ"
                        +
                        "G9iZSBJbWFnZVJlYWR5ccllPAAAD2pJREFUeNrsXQtwFFUWfT2/kCAwyBKgVtzh/xGW8JUCXAYpUXRZwiq1oosJy4KwoEmAiMBqEmRXEGEm"
                        +
                        "fBW0iKWgrCABVxClSAYBBQQGVEICQviUEBAYkJDMr9/e29MJQ5xP98zrmbj2rZrq6Z7u16/fefeee+97/YYQVVRRRRVVVFFFFVVUUUUVVVRRRR"
                        +
                        "VVVPllCFefK9ex619xo4GPVtyyFh4+3rJj7/Ey66Nj3HYU64EfXcXFF4so5eAIR3iqEba+71Bbygn3xS2Px2vPu3NLa38nwhaOb+7bboY10tpt+"
                        +
                        "fYNC5SbguXfqvJq3ZxeR2EHW81XB1J7X//v+DvWu/YZ/PZrnkF4LuJ7Np7T0Mqr1fO2zCbbABQqoWragbnDHmlwd9IcoVBWWgHVdVe68myztn6GSJsV6Hnm"
                        +
                        "/d8vKuzXbnp5hNenYBlQUaLTccTj8hBdAwORAwgv/sbf7iTCca/fPm6T7k6cDaftgN7vAlBCaQcC0CCp+V0rOQ3Xmqmaevg9AIYNH0WjoMVZw6IQnU5DtKAb"
                        +
                        "vJdXpJIanWbg4/mPjpNgErWDX300nTUY0Euun999+kn4JnQIJQEx7zu5OJUFyyU0AEX2uEELqCIV1SXq58AmQeSIYNqRZGjc4EXW9666Ujnm+Hr7DyKfKQoImgz"
                        +
                        "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\" width=\"50\" height=\"50\">\r\n"
                        + "      Nexia Pàdel\r\n"
                        + "  </h1>\r\n"
                        + "  <div style=\"border-top: 1px solid black;\"></div>\r\n"
                        + "  <p style=\"text-align:center;\">\r\n"
                        + "    GRÀCIES PER UTILITZAR NEXIA PÀDEL\r\n"
                        + "  </p>\r\n"
                        + "   <p style=\"text-align:center;\">\r\n"
                        + "    La seua reserva és la següent: \r\n"
                        + "  </p>\r\n"
                        + "  </br>\r\n"
                        + "  <div style=\"text-align: center;\">\r\n"
                        + "<p> Dia: " + "</p>"
                        + "<p> Hora inici: " + "</p>"
                        + "<p> Hora fi:" + "</p>"
                        + "<p> Pista:" + "</p>"

                        + "  </div>\r\n"
                        + "   \r\n"
                        + "</body>\r\n"
                        + "\r\n"
                        + "</html>";

        public static String cosEmailReserva(Reserva rsv) {
                return "<!DOCTYPE html>\r\n"
                                + "<html lang=\"es\">\r\n"
                                + "\r\n"
                                + "<head>\r\n"
                                + "  <meta charset=\"UTF-8\" />\r\n"
                                + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\r\n"
                                + "  <link rel=\"stylesheet\" href=\"style.css\" />\r\n"
                                + "  <title>Browser</title>\r\n"
                                + "  <style>\r\n"
                                + "    body {\r\n"
                                + "    	color:black;\r\n"
                                + "  		margin: 20px;\r\n"
                                + "	}\r\n"
                                + "  </style>\r\n"
                                + "</head>\r\n"
                                + "\r\n"
                                + "<body style=\"margin: 20px;\">\r\n"
                                + "  <h1>\r\n"
                                + "    <img src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAYAAABw4pVUAAAAGXRFWHRTb2Z0d2FyZQBBZ"
                                +
                                "G9iZSBJbWFnZVJlYWR5ccllPAAAD2pJREFUeNrsXQtwFFUWfT2/kCAwyBKgVtzh/xGW8JUCXAYpUXRZwiq1oosJy4KwoEmAiMBqEmRXEGEm"
                                +
                                "fBW0iKWgrCABVxClSAYBBQQGVEICQviUEBAYkJDMr9/e29MJQ5xP98zrmbj2rZrq6Z7u16/fefeee+97/YYQVVRRRRVVVFFFFVVUUUUVVVRRRR"
                                +
                                "VVVPllCFefK9ex619xo4GPVtyyFh4+3rJj7/Ey66Nj3HYU64EfXcXFF4so5eAIR3iqEba+71Bbygn3xS2Px2vPu3NLa38nwhaOb+7bboY10tpt+"
                                +
                                "fYNC5SbguXfqvJq3ZxeR2EHW81XB1J7X//v+DvWu/YZ/PZrnkF4LuJ7Np7T0Mqr1fO2zCbbABQqoWragbnDHmlwd9IcoVBWWgHVdVe68myztn6GSJsV6Hnm"
                                +
                                "/d8vKuzXbnp5hNenYBlQUaLTccTj8hBdAwORAwgv/sbf7iTCca/fPm6T7k6cDaftgN7vAlBCaQcC0CCp+V0rOQ3Xmqmaevg9AIYNH0WjoMVZw6IQnU5DtKAb"
                                +
                                "vJdXpJIanWbg4/mPjpNgErWDX300nTUY0Euun999+kn4JnQIJQEx7zu5OJUFyyU0AEX2uEELqCIV1SXq58AmQeSIYNqRZGjc4EXW9666Ujnm+Hr7DyKfKQoImgz"
                                +
                                "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\" width=\"50\" height=\"50\">\r\n"
                                + "      Nexia Pàdel\r\n"
                                + "  </h1>\r\n"
                                + "  <div style=\"border-top: 1px solid black;\"></div>\r\n"
                                + "  <p style=\"text-align:center;\">\r\n"
                                + "    GRÀCIES PER UTILITZAR NEXIA PÀDEL\r\n"
                                + "  </p>\r\n"
                                + "   <p style=\"text-align:center;\">\r\n"
                                + "    La seua reserva és la següent: \r\n"
                                + "  </p>\r\n"
                                + "  </br>\r\n"
                                + "  <div style=\"text-align: center;\">\r\n"
                                + "<p> Dia: " + rsv.getDia() + "</p>"
                                + "<p> Hora inici: " + rsv.getHorari().getIniHora() + "</p>"
                                + "<p> Hora fi:" + rsv.getHorari().getFiHora() + "</p>"
                                + "<p> Pista:" + rsv.getPista().getNumPista() + "</p>"

                                + "  </div>\r\n"
                                + "   \r\n"
                                + "</body>\r\n"
                                + "\r\n"
                                + "</html>";
        }

        public static String cosEmailReservaMillorat(Reserva rsv) {
                return "<!DOCTYPE html>" +
                                "<html lang=\"ca\">" +
                                "" +
                                "<head>" +
                                "    <meta charset=\"UTF-8\">" +
                                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                                "    <title>Nexia</title>" +
                                "    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.3.0/css/all.min.css\">"
                                +
                                "    <style>" +
                                "        body {" +
                                "            background-color: #f2f2f2;" +
                                "        }" +
                                "        #body {" +
                                "            background-color: #f2f2f2;" +
                                "        }" +
                                "" +
                                "        .fons-degradat {" +
                                "            background: rgb(15, 19, 89);" +
                                "            background: linear-gradient(138deg, rgba(15, 19, 89, 1) 0%, rgba(49, 84, 109, 1) 35%, rgba(59, 103, 115, 1) 50%, rgba(80, 143, 127, 1) 78%, rgba(105, 191, 142, 1) 100%);"
                                +
                                "        }" +
                                "" +
                                "        .header {" +
                                "            padding: 30px;" +
                                "            margin: 0 auto;" +
                                "            text-align: center;" +
                                "        }" +
                                "" +
                                "        .cos-missatge {" +
                                "            max-width: 700px;" +
                                "            margin: 0 auto;" +
                                "            background-color: #fff;" +
                                "            position: relative;" +
                                "            background-image: url(https://mcusercontent.com/db03ab86cca73f5e687380379/images/5be6a399-1144-c291-62d4-078dc04deaee.jpg);"
                                +
                                "            background-repeat: no-repeat;" +
                                "            background-size: 330px;" +
                                "            background-position: center bottom;" +
                                "            padding: 50px 50px 250px 50px;" +
                                "        }" +
                                "" +
                                "        @media only screen and (min-width: 768px) {" +
                                "            .cos-missatge {" +
                                "                padding-bottom: 200px;" +
                                "                background-position: right bottom;" +
                                "            }" +
                                "        }" +
                                "" +
                                "        .cos-missatge ::before {" +
                                "            content: \"\";" +
                                "            position: absolute;" +
                                "            top: 0;" +
                                "            left: 0;" +
                                "            right: 0;" +
                                "            bottom: 0;" +
                                "        }" +
                                "" +
                                "" +
                                "        .cos-missatge h1 {" +
                                "            position: relative;" +
                                "            margin: 0;" +
                                "            padding-bottom: 10px;" +
                                "            font-family: 'Work Sans', 'Lora', Georgia, 'Times New Roman', serif;" +
                                "        }" +
                                "" +
                                "        .cos-missatge p," +
                                "        .cos-missatge strong {" +
                                "            position: relative;" +
                                "            font-size: 20px;" +
                                "            font-family: 'Lora', Georgia, 'Times New Roman', serif;" +
                                "        }" +
                                "" +
                                "" +
                                "        .footer {" +
                                "            padding: 30px;" +
                                "            margin: 0 auto;" +
                                "            color: #fff;" +
                                "            max-width: 700px;" +
                                "        }" +
                                "" +
                                "        @media only screen and (min-width: 768px) {" +
                                "" +
                                "            .footer img {" +
                                "                height: 100%;" +
                                "                width: 250px;" +
                                "                padding-bottom: 5px;" +
                                "            }" +
                                "        }" +
                                "" +
                                "        .footer p {" +
                                "            font-family: 'Lora', Georgia, 'Times New Roman', serif;" +
                                "            margin-bottom: 8px;" +
                                "        }" +
                                "" +
                                "        .footer h3 {" +
                                "            font-family: 'Lora', Georgia, 'Times New Roman', serif;" +
                                "        }" +
                                "        " +
                                "        .dades-contacte .col-primari{" +
                                "            color: #69BF8E" +
                                "        }" +
                                "        " +
                                "        .ultim{" +
                                "            margin:0;" +
                                "        }" +
                                "" +
                                "        .copyright {" +
                                "            background-color: #fff;" +
                                "            font-family: 'Lora', Georgia, 'Times New Roman', serif;" +
                                "            text-align: center;" +
                                "            color: #121212;" +
                                "                margin: 0; " +
                                "            padding: 5px;" +
                                "        }" +
                                "" +
                                "    </style>" +
                                "</head>" +
                                "" +
                                "<body>" +
                                "<div id=\"body\">" +
                                "    <div class=\"fons-degradat\">" +
                                "        <div class=\"header\">" +
                                "            <img src=\"https://mcusercontent.com/db03ab86cca73f5e687380379/images/67a816ef-101e-9b4d-125d-6d76888bc7b7.png\" alt=\"logo-nexia\" width=\"150\">"
                                +
                                "        </div>" +
                                "    </div>" +
                                "    <div class=\"cos-missatge\">" +
                                "        <h1>GRÀCIES PER UTILITZAR NEXIA</h1>" +
                                "        <p><strong>La teva reserva es la següent:</strong></p>" +
                                "        <p>Dia: " + rsv.getDia() + "</p>" +
                                "        <p>Hora Inici: " + rsv.getHorari().getIniHora() + "</p>" +
                                "        <p>Hora Fi: " + rsv.getHorari().getFiHora() + "</p>" +
                                "        <p class=\"ultim\">Pista: " + rsv.getPista().getNumPista() + "</p>" +
                                "    </div>" +
                                "    <div class=\"fons-degradat\">" +
                                "        <div class=\"footer\">" +
                                "            <img class=\"logo\" src=\"https://mcusercontent.com/db03ab86cca73f5e687380379/images/1a33ea10-7913-bd42-81a6-ad534974ca25.png\" alt=\"logo\" width=\"200\">"
                                +
                                "            <div class=\"dades-contacte\">" +
                                "                <h3 class=\"col-primari\">Troba'ns a...</h3>" +
                                "                <p><i class=\"fa-solid fa-location-dot col-primari\"></i>" +
                                "                    C/Falsa 123, Baixos <br> 08012 - Barcelona</p>" +
                                "                <p class=\"ultim\"><i class=\"fa-solid fa-phone col-primari\"></i> 931234567</p>"
                                +
                                "            </div>" +
                                "        </div>" +
                                "       </div>" +
                                "           <div class=\"copyright\">" +
                                "            <p class=\\\"ultim\\\">&copy; 2023 - <b>Nexia SL</b></p>" +
                                "           </div>" +
                                "    </div>" +
                                "</body>" +
                                "" +
                                "</html>";
        }
}
