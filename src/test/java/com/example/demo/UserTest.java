package com.example.demo;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class UserTest {

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
}
