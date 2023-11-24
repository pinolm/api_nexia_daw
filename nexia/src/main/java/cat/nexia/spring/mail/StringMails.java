package cat.nexia.spring.mail;

import cat.nexia.spring.models.Reserva;

public class StringMails {

        public static final String HEADER = "<!DOCTYPE html>" +
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
                        "</head>";

        public static final String FOOTER = "    <div class=\"fons-degradat\">" +
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


        public static String cosEmailReservaConfirmada(Reserva rsv) {
                return HEADER +
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
                                FOOTER;
        };

        public static String cosEmailReservaCancelada(Reserva rsv) {
                return HEADER +
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
                                "        <p><strong>S'ha cancel·lat la teva reserva amb les següents dades:</strong></p>"
                                +
                                "        <p>Dia: " + rsv.getDia() + "</p>" +
                                "        <p>Hora Inici: " + rsv.getHorari().getIniHora() + "</p>" +
                                "        <p>Hora Fi: " + rsv.getHorari().getFiHora() + "</p>" +
                                "        <p class=\"ultim\">Pista: " + rsv.getPista().getNumPista() + "</p>" +
                                "    </div>"
                                + FOOTER;
        }

}
