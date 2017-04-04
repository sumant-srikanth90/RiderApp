package in.droom.riderapp.util;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.internal.http.RealResponseBody;
import okio.GzipSource;
import okio.Okio;

public class UnzippingInterceptor implements Interceptor {
    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        System.out.println("chain.request ==== " + chain.request());
        okhttp3.Response response = chain.proceed(chain.request());
        return unzip(response);
    }


    // copied from okhttp3.internal.http.HttpEngine (because is private)
    private okhttp3.Response unzip(final okhttp3.Response response) throws IOException {

        if (response.body() == null) {
            return response;
        }

        GzipSource responseBody = new GzipSource(response.body().source());
        System.out.println("source == " + responseBody);
        Headers strippedHeaders = response.headers().newBuilder()
                .removeAll("Content-Encoding")
                .removeAll("Content-Length")
                .build();
        return response.newBuilder()
                .headers(strippedHeaders)
                .body(new RealResponseBody(strippedHeaders, Okio.buffer(responseBody)))
                .build();
    }


}
