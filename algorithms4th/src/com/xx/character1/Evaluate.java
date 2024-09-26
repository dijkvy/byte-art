package com.xx.character1;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Stack;

public class Evaluate {
    public static void main(String[] args) {
        Stack<String> ops = new Stack<String>(); // 操作符号
        Stack<Double> vals = new Stack<Double>(); // 操作数

        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            if (s.equals("(")) {
                ;
            } else if (s.equals("+")) {
                ops.push(s);
            } else if (s.equals("-")) {
                ops.push(s);
            } else if (s.equals("*")) {
                ops.push(s);
            } else if (s.equals("/")) {
                ops.push(s);
            } else if (s.equals("sqrt")) {
                ops.push(s);
            } else if (s.equals(")")) { // 获取的是右括号
                String op = ops.pop();  // 获取操作符号
                double v = vals.pop();  // 获取操作数
                if (op.equals("+")) {   // 如果操作符号是 +
                    v = vals.pop() + v; // 再弹一个栈并且相加
                } else if (op.equals("-")) { //  如果当前的操作符号是 -
                    v = vals.pop() - v; // 再弹出一个操作数并且相减
                } else if (op.equals("*")) {
                    v = vals.pop() * v;
                } else if (op.equals("/")) {
                    v = vals.pop() / v;
                } else {
                    v = Math.sqrt(v);
                }
                vals.push(v);
            } else {
                vals.push(Double.parseDouble(s));
            }
        }
        StdOut.println(vals.pop());
    }
}
