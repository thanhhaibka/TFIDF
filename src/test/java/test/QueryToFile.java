package test;

import app.NewToken;
import config.User;
import connectDB.Cassandra;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by pc on 06/09/2016.
 */
public class QueryToFile {
    public static void main(String args[]){
        NewToken.getInstance();
        Cassandra.getInstance();
        System.out.println(updateKeyWord("2885620731906312862","kenh14.vn").toString());
        System.exit(1);
    }

    public static Map<String, Double> updateKeyWord(String guid, String domain) {
        Map<String, Double> longTermWords = new HashMap<>();
        int M = 5;
        double[] weights = {1, 0.9, 0.8, 0.7, 0.6, 0.5};
        Map<String, Double>[] temp = new Map[6];
//        User []user= new User[N];
        for (int i = 0; i < 6; i++) {
            long t= System.currentTimeMillis();
            User user = NewToken.getInstance().setUser(guid, domain, M * i, M * (i + 1));
            if (user.getMapTFIDF().isEmpty()) {
                temp[i] = new HashMap<>();
            } else {
                temp[i] = NewToken.getInstance().getTopN(user.getMapTFIDF(), 100);
            }
            System.out.println(i+": "+(System.currentTimeMillis()-t));
        }
        Set<String> words = new HashSet<>();
        for (int i = 0; i < 6; i++) {
            words.addAll(temp[i].keySet());
        }

        for (String s : words) {
            double sum = 0, sum2 = 0;
            for (int i = 0; i < 6; i++) {
                if (temp[i].containsKey(s)) {
                    sum += temp[i].get(s) * weights[i];
                    sum2 += 1 * weights[i];
                }
            }
            longTermWords.put(s, sum / 6 * sum2);
            longTermWords = NewToken.getTopN(longTermWords, 100);
        }
        return longTermWords;
    }
}
