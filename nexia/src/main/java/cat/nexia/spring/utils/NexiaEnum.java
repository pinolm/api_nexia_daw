package cat.nexia.spring.utils;

public enum NexiaEnum {
    SQLEXCEPTION_MESSAGE_END(172, "final del missatge error"),
    SQLEXCEPTION(0,"PSQLException" ),
    DATA_TIME_FORMAT(1, "yyy-MM-dd"),

    ID_ERROR(2, "No existe la Reserva con id: "),

    RESERVA_DELETE_INFO (3, "S'ha elimininat correctament la reserva"),

    SEND_MAIL(4, "Missatge enviat a ")




    ;

    private final int value;

    private final String phrase;

    private NexiaEnum(int value, String phrase) {
        this.value = value;
        this.phrase = phrase;
    }

    public int value() {
        return this.value;
    }

    public String getPhrase() {
        return this.phrase;
    }
}
