package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.common.Path;
import webserver.common.SessionManager;
import webserver.controller.*;
import webserver.enums.Method;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.net.ServerSocket;
import java.net.Socket;

import static webserver.controller.RequestMapper.Entry.DEFAULT_ENTRY;

public class WebApplicationServer {
    private static final Logger logger = LoggerFactory.getLogger(WebApplicationServer.class);
    private static final int PORT_INDEX = 0;
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {
        int port = DEFAULT_PORT;
        if (args != null && args.length != PORT_INDEX) {
            port = Integer.parseInt(args[PORT_INDEX]);
        }
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);
            registerController();
            acceptClient(listenSocket);
        }
    }

    private static void registerController() {
        RequestMapper.register(Method.GET, Path.of("/user/create"), new GetUserJoinController());
        RequestMapper.register(Method.POST, Path.of("/user/create"), new PostUserJoinController());
        RequestMapper.register(Method.POST, Path.of("/user/login"), new PostUserLoginController());
        RequestMapper.register(Method.GET, Path.of("/user/list"), new GetUserListController());
        RequestMapper.register(Method.GET, Path.of("/user/login.html"), (HttpRequest req, HttpResponse res) -> {
            if (SessionManager.findSession(req.sessionId()).isPresent()) {
                res.setMovedTemporarily(DEFAULT_ENTRY);
                return;
            }
            RequestMapper.staticController().doService(req, res);
        });
    }

    private static void acceptClient(ServerSocket listenSocket) throws Exception {
        Socket connection;
        while ((connection = listenSocket.accept()) != null) {
            Thread thread = new Thread(new RequestHandler(connection));
            thread.start();
        }
    }
}
