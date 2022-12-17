package com.msc.mydropcloud;

/**
 *
 * @author Michael
 */
public class GetInstance {

    private static boolean IS_SERVER = false;

    public static void setIsServer(boolean b) {
        IS_SERVER = b;
    }

    public static boolean getIsServer() {
        return IS_SERVER;
    }

}
