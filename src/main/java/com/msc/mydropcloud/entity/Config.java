package com.msc.mydropcloud.entity;

/**
 *
 * @author Michael
 */
public class Config {

    public Common common = new Common();
    public Client client = new Client();
    public Server server = new Server();

    public static class Common {

        public int port = 9998;
        public String host = "localhost";
    }

    public static class Client {

        public String folder = "C:/Users/Michael/Documents/papiers";
        public String confFolder = "C:/tmp/myHubic/client";
        public boolean isFirstScan = false;
    }

    public static class Server {

        public String confFolder = "C:/tmp/myHubic/server";
        public String folder = "C:/tmp/myHubic/server/files";
        public boolean isFirstRun = false;
    }

}
