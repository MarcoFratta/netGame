package net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

public class Utilities {

    private static final String IP_URL = "http://checkip.amazonaws.com";

    public static String getIp(){
        URL whatismyip = null;
        String ip = "null";
        try {
            whatismyip = new URL(IP_URL);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            ip = in.readLine();
        } catch (Exception e) {
            ip = "Error loading ip";
        }

        return ip;
    }

    public static String getLocalIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "error";
    }
}
