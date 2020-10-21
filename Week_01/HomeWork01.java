/**
 * Created by yuping on 2020/10/20.
 * //理解java字节码
 */
public class HomeWork01 {

    public static void main(String[] args) {

        int a = 1, b = 2;
        int c = a + b;

        long d = 1;
        float f = 3.4f;

        if (a <= 1) {
            System.out.println("hello world");
        }

        for (int i = 0; i < 3; i++) {
            a++;
        }
        System.out.println(a);

    }
}
