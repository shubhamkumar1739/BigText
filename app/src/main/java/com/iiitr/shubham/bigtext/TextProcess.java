package com.iiitr.shubham.bigtext;

import java.util.ArrayList;

public class TextProcess {
    public static ArrayList<String> process(String text, int maxChars) {
        ArrayList<String> result = new ArrayList<>();
        text = text.trim();
        int n = text.length();
        String part = "";
        for(int i = 0; i < n; i++) {
            char c = text.charAt(i);
            part += c;
            if(part.length() == maxChars) {
                result.add(part);
                part = "";
            }
        }
        if(!part.equals("")) {
            result.add(part);
        }

        for(int i = 1; i < result.size(); i++) {
            String cur = result.get(i);
            String prev = result.get(i - 1);
            char first = cur.charAt(0);
            char last = prev.charAt(prev.length() - 1);
            if(!Character.isWhitespace(first) && !Character.isWhitespace(last)) {
                cur = "-" + cur;
                prev += "-";
                result.set(i, cur);
                result.set(i - 1, prev);
            }
        }

        return result;
    }
}
