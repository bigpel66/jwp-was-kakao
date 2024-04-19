package webserver.enums;

public enum MediaType {
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded"),
    TEXT_PLAIN("text/plain;charset=utf-8"),
    TEXT_HTML("text/html;charset=utf-8"),
    TEXT_CSS("text/css");
    private final String value;

    MediaType(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
