package cat.nexia.spring.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class NexiaUtils {

    public static String psqlException(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        if (errors.toString().contains(NexiaEnum.SQLEXCEPTION.getPhrase())) {
            int inici = errors.toString().indexOf(NexiaEnum.SQLEXCEPTION.getPhrase());
            StringBuilder sb = new StringBuilder();
            sb.append(errors.toString(), inici, inici + NexiaEnum.SQLEXCEPTION_MESSAGE_END.value());
            return sb.toString();
        }
        return null;
    }

    public static StringWriter getStringWriter(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw;
    }
}
