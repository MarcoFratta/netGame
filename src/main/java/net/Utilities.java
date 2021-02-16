package net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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

    public static <T> void permute(final List<List<T>> lists, final Consumer<List<T>> consumer) {
        final int[] index_pos = new int[lists.size()];

        final int last_index = lists.size() - 1;
        final List<T> permuted = new ArrayList<T>(lists.size());

        for (int i = 0; i < lists.size(); ++i) {
            permuted.add(null);
        }

        while (index_pos[last_index] < lists.get(last_index).size()) {
            for (int i = 0; i < lists.size(); ++i) {
                permuted.set(i, lists.get(i).get(index_pos[i]));
            }
            consumer.accept(permuted);

            for (int i = 0; i < lists.size(); ++i) {
                ++index_pos[i];
                if (index_pos[i] < lists.get(i).size()) {
                    /* stop at first element without overflow */
                    break;
                } else if (i < last_index) {
                    index_pos[i] = 0;
                }
            }
        }
    }
}
