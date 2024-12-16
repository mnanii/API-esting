package retrofit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

import java.io.IOException;

public class CurlLoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // Build cURL command
        StringBuilder curlCmd = new StringBuilder("curl -X ")
                .append(request.method())
                .append(" '")
                .append(request.url())
                .append("'");

        // Add headers
        if (request.headers() != null) {
            for (String name : request.headers().names()) {
                curlCmd.append(" -H '").append(name).append(": ").append(request.header(name)).append("'");
            }
        }

        // Add body if POST/PUT
        if (request.body() != null) {
            Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            curlCmd.append(" --data '").append(buffer.readUtf8()).append("'");
        }

        System.out.println("cURL: " + curlCmd.toString());

        return chain.proceed(request);
    }
}
