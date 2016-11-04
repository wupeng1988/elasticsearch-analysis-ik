package org.singledog.simhash;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Adam.Wu on 2016/11/3.
 */
public class SimhashGenerator {

    private static final Settings setting = Settings.builder()
            .put("path.home","")
            .put("path.conf", "E:\\java\\workspace_wp\\edooon-server\\elasticsearch-analysis-ik\\target\\")
            .build();
    private static final Environment environment = new Environment(setting);

    public static final int hash_bit_length = 64;

    private static Configuration configuration;

    static {
        configuration = new Configuration(environment, setting);
        configuration.setUseSmart(true);
    }

    public static List<String> wordSplit(String word) {
        List<String> words = new ArrayList<>();
        IKSegmenter ikSegmenter = new IKSegmenter(new StringReader(word), configuration);
        Lexeme lexeme;
        try {
            while ((lexeme = ikSegmenter.next()) != null) {
                words.add(lexeme.getLexemeText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    public static int count(String text, String keyWord) {
        int i = 0;
        int j = -1;
        while ((j = text.indexOf(keyWord)) >= 0) {
            i++;
            text = text.substring(j + keyWord.length());
        }
        return i;
    }
    public static int count2(String text, String keyWord) {
        return text.split(keyWord).length - 1;
    }

    public static int count3(Pattern pattern, String text) {
        Matcher matcher = pattern.matcher(text);
        int i = 0;
        while (matcher.find())
            i++;
        return i;
    }

    public static BigInteger simHash(String word) {
        List<String> words = wordSplit(word);
        int[] v = new int[hash_bit_length];
        words.forEach(w -> {
            BigInteger t = hash(w);
            for (int i = 0; i < hash_bit_length; i++) {
                BigInteger mask = new BigInteger("1").shiftLeft(i);
                if (t.and(mask).signum() != 0) {
                    // 这里是计算整个文档的所有特征的向量和
                    // 这里实际使用中需要 +- 权重，而不是简单的 +1/-1，
                    v[i] += count(word, w);
                } else {
                    v[i] -= count(word, w);
                }
            }
        });

        BigInteger fingerprint = new BigInteger("0");
        StringBuffer simHashBuffer = new StringBuffer();
        for (int i = 0; i < hash_bit_length; i++) {
            // 4、最后对数组进行判断,大于0的记为1,小于等于0的记为0,得到一个 64bit 的数字指纹/签名.
            if (v[i] >= 0) {
                fingerprint = fingerprint.add(new BigInteger("1").shiftLeft(i));
                simHashBuffer.append("1");
            } else {
                simHashBuffer.append("0");
            }
        }
        return fingerprint;
    }

    public static void main(String[] args) {
        String text = "package com.taobao.web.servlet;\n" +
                "public class IndexOfTest {\n" +
                " /** \n" +
                "  * 本文通过代码的方式介绍如何使用Java统计某一字符串中不同子字符串出现的次数，如“Java统计不同字符串出现的次数”中“a”的出现次数，很显然是2次。\n" +
                "  * @param args\n" +
                "  */\n" +
                " public static void main(String[] args) {\n" +
                "  // TODO Auto-generated method stub\n" +
                "  String str1=\"pjhuiyhoeijtohygdifei\";\n" +
                "  String str2=\"这是一个用java语言编写的统计字符串出现次数的代码,这是一个用java语言编写的统计字符串出现次数的代码\";\n" +
                "  //String str2=\"出现出现\";\n" +
                "  String s1=\"ui\";\n" +
                "  String s2=\"统计字符串\";\n" +
                "  String s3=\"i\";\n" +
                "  String s4=\"出现\";\n" +
                "  //查找并显示子串出现的位置\n" +
                "  int i1=str1.indexOf(s1);\n" +
                "  int i2=str2.indexOf(s2);\n" +
                "  System.out.println(i1);\n" +
                "  System.out.println(i2);\n" +
                "  \n" +
                "  //显示子串出现的次数\n" +
                "  int count1=0;\n" +
                "  int count2=0;\n" +
                "  for(String tmp=str1;tmp!=null&&tmp.length()>=s3.length();){\n" +
                "   if(tmp.indexOf(s3)==0){\n" +
                "    count1++;\n" +
                "    tmp=tmp.substring(s3.length());\n" +
                "   }else{\n" +
                "    tmp=tmp.substring(1);\n" +
                "   }\n" +
                "  }\n" +
                "  for(String tmp=str2;tmp!=null&&tmp.length()>=s4.length();){\n" +
                "   if(tmp.indexOf(s4)==0){\n" +
                "    count2++;\n" +
                "    tmp=tmp.substring(s4.length());\n" +
                "   }else{\n" +
                "    tmp=tmp.substring(1);\n" +
                "   }\n" +
                "  }\n" +
                "  System.out.println(\"\"\"+s3+\"\"\"+\"  在\"+\"\"\"+str1+\"\"\"+\"中出现 \"+count1+\" 次\");\n" +
                "  System.out.println(\"\"\"+s4+\"\"\"+\"  在\"+\"\"\"+str2+\"\"\"+\"中出现 \"+count2+\" 次\");\n" +
                " }\n" +
                "}";

        long t = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++)
            count(text, "tmp");
        System.out.println(System.currentTimeMillis() - t);

        t = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++)
            count2(text, "tmp");
        System.out.println(System.currentTimeMillis() - t);

        t = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++)
            count3(Pattern.compile("tmp"), text);
        System.out.println(System.currentTimeMillis() - t);

    }

    private static BigInteger hash(String source) {
        if (source == null || source.length() == 0) {
            return new BigInteger("0");
        } else {
            char[] sourceArray = source.toCharArray();
            BigInteger x = BigInteger.valueOf(((long) sourceArray[0]) << 7);
            BigInteger m = new BigInteger("1000003");
            BigInteger mask = new BigInteger("2").pow(hash_bit_length).subtract(new BigInteger("1"));
            for (char item : sourceArray) {
                BigInteger temp = BigInteger.valueOf((long) item);
                x = x.multiply(m).xor(temp).and(mask);
            }
            x = x.xor(new BigInteger(String.valueOf(source.length())));
            if (x.equals(new BigInteger("-1"))) {
                x = new BigInteger("-2");
            }
            return x;
        }
    }

}
