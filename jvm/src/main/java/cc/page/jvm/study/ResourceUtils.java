package cc.page.jvm.study;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

//@Slf4j
public class ResourceUtils {
    private static ResourceUtils instance;

    static {
        instance = new ResourceUtils();
    }

    private ResourceUtils() {
    }

    public static ResourceUtils create() {
        return instance;
    }

    public String getFile(String resourceFile) {
        // https://stackoverflow.com/questions/20389255/reading-a-resource-file-from-within-jar

        val fn = FilenameUtils.concat(System.getProperty("user.dir"), StringUtils.removeStart(resourceFile, "/"));
//        log.debug(fn);

        try (InputStream in = getClass().getResourceAsStream(resourceFile)) {
            if (null == in) {
//                log.warn("getResourceAsStream is null");
                ClassLoader cl = this.getClass().getClassLoader();
                URL url = cl.getResource(resourceFile);
                if (url == null) {
//                    log.warn("getResource is null");
                    return null;
                }
                File f = new File(url.getFile());
                return f.getAbsolutePath();
            }

//            log.debug("copy input stream");
            FileUtils.copyInputStreamToFile(in, new File(fn));
            return fn;
        } catch (IOException e) {
//            log.error(e.getMessage(), e);
            return null;
        }
    }
}
