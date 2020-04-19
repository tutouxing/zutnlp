package zut.cs.sys.service.impl;

import zut.cs.sys.domain.Key_words;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class tfidf_from_python {
    public static List<Key_words> main(String[] args) {
        List<String> a=new ArrayList<>();
        List<String> result = new ArrayList<>();
        List<Key_words> key_words = new ArrayList<Key_words>();
        try {
            a.add("python");
            a.add("src/main/resources/static/TF-IDF.py");
            a.add("src/main/resources/static/stop_words.txt");
            for (int i=0; i< args.length;i++){
                a.add(args[i]);
                System.out.println("输出传入的args:"+args[i]);
            }
            String[] corpus=a.toArray(new String[a.size()]);
            Process proc = Runtime.getRuntime().exec(corpus); // 执行py
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), "GBK"));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                result.add(line);
            }
            in.close();
            proc.waitFor();
        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
        for(int x = 0 ; x < result.size(); x = x + 2){
            Key_words ss = new Key_words(result.get(x) , Double.parseDouble(result.get(x + 1)));
            key_words.add(ss);
        }
        System.out.println(key_words);
        return key_words;
    }
}
