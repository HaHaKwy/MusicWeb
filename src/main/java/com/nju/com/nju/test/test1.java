package com.nju.com.nju.test;

public class test1 {
    public static void main(String[] args) {
        String s = "\"I spoke a different language? But-I didn't realize - how can I speak a language without knowing I can speak it?\"";
        String s0 = s.replaceAll("[^(A-Za-z \\-)]"," ");
        System.out.println(s0);
        String[] list = s0.split("\\s+");
        for (String str:list
             ) {
            System.out.println(str);
        }
    }
}
