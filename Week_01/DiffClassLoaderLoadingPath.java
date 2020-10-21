import java.util.Arrays;
import java.util.List;

public class DiffClassLoaderLoadingPath {
    public static void main(String[] args) {
        bootClassLoaderLoadingPath();
        extClassLoaderLoadingPath();
        appClassLoaderLoadingPath();
        System.out.println(DiffClassLoaderLoadingPath.class.getClassLoader());
        System.out.println(DiffClassLoaderLoadingPath.class.getClassLoader().getParent());
        System.out.println(DiffClassLoaderLoadingPath.class.getClassLoader().getParent().getParent());
    }


    /**
     * 启动类加载器加载的职责
     */
    private static void bootClassLoaderLoadingPath() {
        //获取启动类加载器加载的目录
        String bootStrapLoadingPath = System.getProperty("sun.boot.class.path");
        //把加载的目录转为集合
        List<String> bootLoadingPathList = Arrays.asList(bootStrapLoadingPath.split(";"));
        for (String bootPath : bootLoadingPathList) {
            System.out.println("[启动类加载器---加载的目录]: " + bootPath);
        }
    }

    /**
     * 扩展类加载器加载的职责
     */
    private static void extClassLoaderLoadingPath() {
        //获取扩展类加载器加载的目录
        String bootStrapLoadingPath = System.getProperty("java.ext.dirs");
        //把加载的目录转为集合
        List<String> bootLoadingPathList = Arrays.asList(bootStrapLoadingPath.split(";"));
        for (String bootPath : bootLoadingPathList) {
            System.out.println("[扩展类加载器---加载的目录]: " + bootPath);
        }
    }

    /**
     * 系统类加载器加载的职责
     */
    private static void appClassLoaderLoadingPath() {
        //获取启动类加载器加载的目录
        String bootStrapLoadingPath = System.getProperty("java.class.path");
        //把加载的目录转为集合
        List<String> bootLoadingPathList = Arrays.asList(bootStrapLoadingPath.split(";"));
        for (String bootPath : bootLoadingPathList) {
            System.out.println("[系统类加载器---加载的目录]: " + bootPath);
        }
    }
}
