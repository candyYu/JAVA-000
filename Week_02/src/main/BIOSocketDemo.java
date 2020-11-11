import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by candy on 2020/10/30.
 */
public class BIOSocketDemo {

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(8801);
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                service(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void service(Socket socket) throws IOException, InterruptedException {
        Thread.sleep(20);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        printWriter.println("HTTP/1.1 200 OK");
        printWriter.println("Content-Type:text/html;charset=utf-8");
        String body = "hello,nio";
        printWriter.println("Content-Length:" + body.getBytes().length);
        printWriter.println();
        printWriter.write(body);
        printWriter.close();
        socket.close();
    }
}
