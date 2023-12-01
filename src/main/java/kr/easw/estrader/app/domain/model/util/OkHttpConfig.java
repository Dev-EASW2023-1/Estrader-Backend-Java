package kr.easw.estrader.app.domain.model.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

// @Configuration 어노테이션과 @Bean 어노테이션을 함께 사용하여 싱글톤임을 보장
@Slf4j
@Configuration
public class OkHttpConfig {
    private static final int NUMBER_OF_RETRIES = 3;
    private static final long RETRY_DELAY = 300;

    @Bean("okHttpClient")
    OkHttpClient okHttpClient() {
        return new OkHttpClient()
                .newBuilder()
                // 서버 연결을 최대 10초 수행
                .connectTimeout(10, TimeUnit.SECONDS)
                // 서버 요청을 최대 10초 수행
                .writeTimeout(10, TimeUnit.SECONDS)
                // 서버 응답을 최대 10초 기다림
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    boolean responseOK = false;
                    Response response = null;
                    int tryCount = 0;

                    // try the request
                    while (!responseOK && tryCount < NUMBER_OF_RETRIES) {
                        try {
                            // If chain.proceed(request) is being called more than once previous response bodies must be closed.
                            response = chain.proceed(request);
                            responseOK = response.isSuccessful();
                        } catch (IOException e) {
                            e.printStackTrace();
                            log.error("intercept, Request is not successful - " + tryCount);
                        } finally {
                            tryCount++;
                            if (response != null) {
                                // Close the response body after using it
                                response.close();
                            }
                        }
                    }
                    // If we have timeout or no connection, it returns null then it generates NullPointerException.
                    // To fix nullPointerException need to replace "return response;" at the end with "return response != null ? response : chain.proceed(request)";
                    return response != null ? response : chain.proceed(request);
                })
                .build();
    }
}