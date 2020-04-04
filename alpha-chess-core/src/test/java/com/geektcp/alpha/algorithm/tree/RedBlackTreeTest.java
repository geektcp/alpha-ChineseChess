package com.geektcp.alpha.algorithm.tree;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

/**
 * @author tanghaiyang on 2019/9/16.
 */
@Slf4j
public class RedBlackTreeTest {

    @Test
    public void redBlackTree() {
        ArrayList<String> words = new ArrayList<>();
        words.add("1");
        words.add("1");
        words.add("1");
        words.add("2");
        words.add("3");
        words.add("4");
        words.add("5");
        words.add("6");
        words.add("77");
        words.add("4");
        RedBlackTree<String, Integer> redBlackTree = new RedBlackTree<>();
        for (String word : words) {
            if (redBlackTree.contains(word)) {
                redBlackTree.set(word, redBlackTree.get(word) + 1);
            } else {
                redBlackTree.add(word, 1);
            }
            redBlackTree.print();
            log.info("===========");
        }


        log.info("共有不同单词数：" + redBlackTree.getSize());
        log.info("出现4的次数: " + redBlackTree.get("4"));
        log.info("出现1的次数: " + redBlackTree.get("1"));
        Assert.assertTrue(true);
    }

    @Test
    public void redBlackTreeFromFile() {
        ArrayList<String> words = new ArrayList<>();
        String path = RedBlackTreeTest.class.getResource("/").getPath();
        if (readFile(path + "/" + "test.txt", words)) {
            log.info("共有单词数：" + words.size());
            RedBlackTree<String, Integer> redBlackTree = new RedBlackTree<>();
            for (String word : words) {
                if (redBlackTree.contains(word)) {
                    redBlackTree.set(word, redBlackTree.get(word) + 1);
                } else {
                    redBlackTree.add(word, 1);
                }
            }
            redBlackTree.print();
            log.info("共有不同单词数：" + redBlackTree.getSize());
            log.info("出现pride的次数: " + redBlackTree.get("pride"));
            log.info("出现prejudice的次数: " + redBlackTree.get("prejudice"));
        }
        Assert.assertTrue(true);
    }


    /**
     * 读取一个文本文件所有单词，存入List
     *
     * @param filename 文件的绝对路径
     * @param words    结果集合
     * @return 是否读取成功
     */
    private static boolean readFile(String filename, ArrayList<String> words) {
        if (filename == null || words == null) {
            log.info("文件名或words不能为空");
            return false;
        }
        File file = new File(filename);
        if (!file.exists()) {
            return false;
        }
        try (FileInputStream fis = new FileInputStream(file); Scanner scanner = new Scanner(new BufferedInputStream(fis), "UTF-8")) {
            scanner.useLocale(Locale.ENGLISH);
            if (scanner.hasNextLine()) {
                String contents = scanner.useDelimiter("\\A").next();
                int start = firstCharacterIndex(contents, 0);
                for (int i = start + 1; i <= contents.length(); i++) {
                    if (i == contents.length() || !Character.isLetter(contents.charAt(i))) {
                        String word = contents.substring(start, i).toLowerCase();
                        words.add(word);
                        start = firstCharacterIndex(contents, i);
                        i += start;
                    }
                }
            }
        } catch (IOException ioe) {
            log.info("不能打开" + filename);
            return false;
        }

        return true;
    }

    /**
     * 寻找字符串s中，从start的位置开始的第一个字母字符的位置
     *
     * @param s     目标字符串
     * @param start 寻找的起始位置
     * @return 从起始位置开始的第一个字母的位置
     */
    private static int firstCharacterIndex(String s, int start) {
        for (int i = start; i < s.length(); i++) {
            if (Character.isLetter(s.charAt(i))) {
                return i;
            }
        }
        return s.length();
    }
}
