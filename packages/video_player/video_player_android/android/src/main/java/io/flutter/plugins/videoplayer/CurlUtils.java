// package io.flutter.plugins.videoplayer;
//
// import okhttp3.Request;
// import okhttp3.RequestBody;
// import okhttp3.Headers;
//
// import java.io.IOException;
//
// public class CurlUtils {
//
//     public static String toCurlCommand(Request request) throws IOException {
//         StringBuilder curlCommand = new StringBuilder("curl");
//
//         // Add method
//         curlCommand.append(" -X ").append(request.method());
//
//         // Add headers
//         Headers headers = request.headers();
//         for (String name : headers.names()) {
//             curlCommand.append(" -H ").append("\"").append(name).append(": ").append(headers.get(name)).append("\"");
//         }
//
//         // Add body (if applicable)
//         RequestBody body = request.body();
//         if (body != null) {
//             if (body.contentType() != null) {
//                 curlCommand.append(" -H \"Content-Type: ").append(body.contentType()).append("\"");
//             }
//
//             // Extract body content
//             if (body.contentLength() > 0) {
//                 okio.Buffer buffer = new okio.Buffer();
//                 body.writeTo(buffer);
//                 curlCommand.append(" --data ").append("\"").append(buffer.readUtf8()).append("\"");
//             }
//         }
//
//         // Add URL
//         curlCommand.append(" \"").append(request.url().toString()).append("\"");
//
//         return curlCommand.toString();
//     }
// }