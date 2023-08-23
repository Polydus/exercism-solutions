package com.polydus.exercism;

import com.polydus.exercism.say.Say;

public class Main {
    public static void main(String[] args) {


        var say = new Say();

        for(var i = 0; i < 10001; i++){
            //System.out.println(say.say(i * 100));
        }
        System.out.println(say.say(987_654_321_123L));

    }
}