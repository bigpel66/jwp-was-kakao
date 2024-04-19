package webserver.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class Cookie {
    public static final String JAVA_SESSION_KEY = "JSESSIONID";
    public static final String PATH_KEY = "Path";
    public static final String PATH_VALUE = "/";
    public static final String LOGINED_KEY = "logined";
    public static final String LOGINED_VALUE = "true";
    private static final String COOKIE_ENTRY_DELIMITER = "; ";
    private static final String COOKIE_KEY_DELIMITER = "=";
    private static final int KEY_INDEX = 0;
    private static final int VALUE_INDEX = 1;
    private static final int KEY_VALUE_LENGTH = 2;

    private final Map<String, String> cookie;

    public Cookie() {
        this.cookie = new HashMap<>();
    }

    public Cookie(String cookie) {
        this.cookie = new HashMap<>();
        String[] entries = cookie.split(COOKIE_ENTRY_DELIMITER);
        addEntries(entries);
    }

    private void addEntries(String[] entries) {
        Arrays.stream(entries).forEach(e -> {
            if (e.isBlank()) {
                return;
            }
            String[] keyVal = e.split(COOKIE_KEY_DELIMITER);
            if (keyVal.length != KEY_VALUE_LENGTH) {
                throw new IllegalStateException("Cookie의 형식이 올바르지 않습니다.");
            }
            this.cookie.putIfAbsent(keyVal[KEY_INDEX], keyVal[VALUE_INDEX]);
        });
    }

    public Map<String, String> all() {
        return cookie;
    }

    public boolean isEmpty() {
        return this.cookie.isEmpty();
    }

    public void put(String key, String value) {
        this.cookie.putIfAbsent(key, value);
    }
}
