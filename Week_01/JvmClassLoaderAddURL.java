import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by candy on 2020/10/20.
 * //jdk9 之后有其他方式
 */
public class JvmClassLoaderAddURL {

    public static void main(String[] args) {
        String path = "file:d:/app/";
        URLClassLoader urlClassLoader = (URLClassLoader)JvmClassLoaderAddURL.class.getClassLoader();
        try {
            Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addURL.setAccessible(true);
            URL url = new URL(path);
            addURL.invoke(urlClassLoader, url);
            Class.forName("jvm.hello");

        }catch ( Exception ex) {
            ex.printStackTrace();
        }
    }
}
