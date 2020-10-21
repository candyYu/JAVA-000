
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by candy on 2020/10/20.
 */
public class HomeWork02HelloClassLoader extends ClassLoader {

    public static void main(String[] args) {
        try {
            Class<?> hello = new HomeWork02HelloClassLoader().findClass("Hello");
            Object object = hello.newInstance();
            Method method = hello.getMethod("hello");
            method.invoke(object);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        FileInputStream fileInputStream = null;
        String filePath = "/Users/yuping/Downloads/2020-10-19 (2)/讲师秦金卫-资料分享/Hello/Hello.xlass";
        try {
            fileInputStream = new FileInputStream(filePath);
            int in = -1;
            byte[] bytes = new byte[fileInputStream.available()];
            byte[] newBytes = new byte[bytes.length];
            fileInputStream.read(bytes);
            for (int i = 0; i < bytes.length; i++) {
                newBytes[i] = (byte)(255 - bytes[i]);
            }
            return  defineClass(name,newBytes,0,newBytes.length);

        }catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }finally {
            try {
                if (null != fileInputStream) {
                    fileInputStream.close();
                }
            }catch (IOException ex) {

            }
        }
    }
}
