package utils;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import webserver.controller.StaticServingController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class FileIoUtils {
    private static final String EXT_DELIMITER = ".";
    private static final String HANDLEBARS_EXT_NAME = ".html";
    private static final String TEMPLATE_PATH = "/templates";
    private static final String STATIC_PATH = "/static";

    public static byte[] loadFileFromClasspath(String filePath) throws IOException, URISyntaxException {
        URL resource = FileIoUtils.class.getResource(filePath);
        Path path = Paths.get(Objects.requireNonNull(resource).toURI());
        return Files.readAllBytes(path);
    }

    public static boolean isFileFromTemplates(String filePath) {
        return StaticServingController.class.getResource(TEMPLATE_PATH + filePath) != null;
    }

    public static boolean isFileFromStatic(String filePath) {
        return StaticServingController.class.getResource(STATIC_PATH + filePath) != null;
    }

    public static byte[] loadFileFromTemplates(String filePath, Object attribute) throws IOException {
        ClassPathTemplateLoader loader = new ClassPathTemplateLoader(TEMPLATE_PATH, HANDLEBARS_EXT_NAME);
        Handlebars handlebars = new Handlebars(loader);
        String extname = filePath.substring(filePath.lastIndexOf(EXT_DELIMITER));
        if (!HANDLEBARS_EXT_NAME.equals(extname)) {
            throw new FileNotFoundException("존재하지 않는 템플릿 파일입니다.");
        }
        String filename = filePath.substring(0, filePath.lastIndexOf(EXT_DELIMITER));
        Template compile = handlebars.compile(filename);
        return compile.apply(attribute).getBytes();
    }

    public static byte[] loadFileFromStatic(String filePath) throws IOException, URISyntaxException {
        return loadFileFromClasspath(STATIC_PATH + filePath);
    }
}
