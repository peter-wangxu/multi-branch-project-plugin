package com.github.mjdetullio.jenkins.plugins.multibranch;

import hudson.Util;
/**
 * Created by wangp11 on 10/8/2016.
 */
public class Utils {
    public static String ReplaceUrlChar(String origin) {
        //Current only handle /

        if (null != origin || origin.isEmpty())
            return Util.rawEncode(origin);
        return Util.rawEncode(origin.replace("-", "--").replace("/", "-"));
    }
}
