
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by candy on 2020/10/29.
 */
public class Main {

    public static void main(String[] args) {
        httpClient_get("http://127.0.0.1:8088/api/hello");
    }

    private static void httpClient_get(String url) {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        // 创建http GET请求
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                //请求体内容
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");

                System.out.println("返回内容：" + content);
            }
        } catch (IOException ex) {

        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //相当于关闭浏览器
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static void okHttp_get(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            response.body().string();
        } catch (IOException ex) {
        }

    }


}
