package com.example.demo;

import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootTest
public class UserTest {
    @Autowired
    private OkHttpClient okHttpClient;

    @Test
    public void testSplit() {
        int num = 12345;
        ArrayList<Integer> arrNum = new ArrayList<>();
        while(num > 0) {
            arrNum.add(num %10);
            num /= 10;
        }
        System.out.println(arrNum);
    }

    @Test
    public void testSplit1() {
        int num = 12345;
        String strNum = Integer.toString(num);
        int[] arrNum = new int[strNum.length()];
        for (int i = 0; i < strNum.length(); i++) {
            arrNum[i] = strNum.charAt(i) - '0';
        }
        System.out.print(Arrays.toString(arrNum));
    }

    @Test
    public void testOkHttp() {
        // RequestBody 생성
        RequestBody requestBody = RequestBody.create(
                MediaType.get("application/json; charset=utf-8"),
                "테스트"
        );

        // Post 요청 객체 생성
        Request request = new Request.Builder()
                .url("https://test.com/")
                .post(requestBody)
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        // 요청 전송
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println("fcm 전송 성공 responseCode = " + response.code() + "errorMessage = " + response.message());
            } else {
                System.out.println("fcm 전송 실패 responseCode = " + response.code() + "errorMessage = " + response.message());
            }
        } catch (IOException e) {
            // IOException 처리
        }
    }

}
