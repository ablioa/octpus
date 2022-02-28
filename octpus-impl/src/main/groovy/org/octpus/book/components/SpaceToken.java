//package org.octpus.book.components;
//
//import lombok.Data;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Data
//public class SpaceToken {
//    char start;
//    int startAt;
//    int endAt;
//
//    public SpaceToken(char token,int startAt){
//        this.startAt = startAt;
//        this.start = token;
//    }
//
//    public static Set <Character> startTokes = new HashSet<>();
//    public static Set <Character> endTokens = new HashSet<>();
//
//    static {
//        startTokes.add('[');
//        endTokens.add(']');
//    }
//
//    public static boolean start(char token){
//        return startTokes.contains(token);
//    }
//
//    public static boolean end(char token){
//        return endTokens.contains(token);
//    }
//}
