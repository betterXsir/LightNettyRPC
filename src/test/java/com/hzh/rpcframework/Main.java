package com.hzh.rpcframework;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();
        int letterCount = 0;
        int digitCount = 0;
        int length = line.length();
        int[] num = new int[length];
        char pre = ' ';
        boolean preIsDigit = false;
        ArrayList<Character> out = new ArrayList<>();
        for(int i=0; i<length; i++) {
            char c = line.charAt(i);
            if (!Character.isLetterOrDigit(c)) {
                System.out.print("!error");
                return;
            }
            if(Character.isLetter(c)){
                if(!Character.isLowerCase(c)){
                    System.out.print("!error");
                    return;
                }
                if (i == 0) {
                    pre = c;
                    letterCount++;
                }
                else if (c == pre) {
                    letterCount++;
                    if(letterCount > 2){
                        System.out.print("!error");
                        return;
                    }
                } else {
                    letterCount = 1;
                }
                if(preIsDigit){
                    int a = 0;
                    int powNum = digitCount-1;
                    for(int k=digitCount-1; k>=0; k--){
                        a += num[k] * Math.pow(10,powNum--);
                    }
                    for(int j=0; j<a; j++){
                        out.add(c);
                    }
                    num = new int[length];
                    digitCount = 0;
                }
                else{
                    out.add(c);
                }
                pre = c;
                preIsDigit = false;
            }
            if(Character.isDigit(c)){
                num[digitCount++] = Integer.valueOf(c)-48;
                preIsDigit = true;
            }
        }
        Object[] objects = out.toArray();
        char[] chars = new char[objects.length];
        for(int i = 0 ; i < objects.length ; i++)
            chars[i] = (char)objects[i];
        System.out.print(chars);
    }
}
