package webserver.controller;

import webserver.common.Path;
import webserver.enums.Method;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class RequestMapper {
    private static final Map<Entry, Controller> map = new HashMap<>();
    private static final ControllerAdvice globalControllerAdvice = new GlobalControllerAdvice();

    static {
        register(Method.GET, Path.of(Entry.STATIC_ENTRY), new StaticServingController());
    }

    public static Controller staticController() {
        return map.get(new Entry(Method.GET, Path.of(Entry.STATIC_ENTRY)));
    }

    public static void register(Method method, Path path, Controller controller) {
        Entry entry = new Entry(method, path);
        if (map.containsKey(entry)) {
            throw new UnsupportedOperationException("이미 존재하는 URI의 Controller입니다.");
        }
        map.putIfAbsent(entry, controller);
    }

    public static void doService(HttpRequest req, HttpResponse res) throws IOException, URISyntaxException {
        Controller controller = controller(req);
        globalControllerAdvice.accept(req, res, controller);
    }

    private static Controller controller(HttpRequest req) {
        Entry entry = new Entry(req.method(), req.path());
        if (map.containsKey(entry)) {
            return map.get(entry);
        }
        return map.get(new Entry(Method.GET, Path.of(Entry.STATIC_ENTRY)));
    }

    public static final class Entry {
        public static final String DEFAULT_ENTRY = "/index.html";
        public static final String STATIC_ENTRY = "thisisdefaultroutepath";
        public static final String[] STATIC_SORTED_ENTRY = {"/css", "/js", "/fonts", "images"};
        public static final String LOGIN_ENTRY = "/user/login.html";
        public static final String LOGIN_FAILED_ENTRY = "/user/login_failed.html";
        public static final String USER_LIST_ENTRY = "/user/list.html";
        private final Method method;
        private final Path path;

        Entry(Method method, Path path) {
            this.method = method;
            this.path = path;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry entry = (Entry) o;
            return method == entry.method && Objects.equals(path, entry.path);
        }

        @Override
        public int hashCode() {
            return Objects.hash(method, path);
        }
    }
}
