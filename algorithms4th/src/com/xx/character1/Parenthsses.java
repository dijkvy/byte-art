package com.xx.character1;

import com.xx.mycontainer.Stack;

public class Parenthsses {
    public static void main(String[] args) {
        System.out.println(isOk("{ ]"));
        System.out.println(isOk("{ }"));
        System.out.println(isOk("{ {"));
        System.out.println(isOk("{ { } )"));
        System.out.println(isOk(""));
    }

    public static boolean isOk(String data) {
        String[] sp = data.split(" ");
        Stack<String> ss = new Stack<>();
        String tmp;
        for (String s : sp) {
            if (s.equals(")")) {
                tmp = ss.pop();
                if (tmp == null) {
                    return false;
                }
                if (!tmp.equals("(")) {
                    return false;
                }
            } else if (s.equals("]")) {
                tmp = ss.pop();
                if (tmp == null) {
                    return false;
                }
                if (!tmp.equals("[")) {
                    return false;
                }
            } else if (s.equals("}")) {
                tmp = ss.pop();
                if (tmp == null) {
                    return false;
                }
                if (!tmp.equals("{")) {
                    return false;
                }
            } else {
                ss.push(s);
            }
        }
        return ss.isEmpty();
    }

    private class Item {
        private String data /* "{[)}"*/;
        private boolean isOk;
    }
}
