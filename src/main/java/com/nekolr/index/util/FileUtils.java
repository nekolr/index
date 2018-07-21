package com.nekolr.index.util;

import java.io.*;

/**
 * 文件工具类
 *
 * @author nekolr
 */
public class FileUtils {

    /**
     * 读取文件，返回比特数组
     *
     * @param file
     * @return
     */
    public static byte[] readBytes(File file) {
        long len = file.length();
        /**
         * {@link Integer.MAX_VALUE}
         */
        if (len >= 2147483647L) {
            throw new RuntimeException("File is larger then max array size");
        } else {
            // 一次性放入缓存
            byte[] bytes = new byte[(int) len];
            FileInputStream in = null;
            try {
                in = new FileInputStream(file);
                int readLength = in.read(bytes);
                if ((long) readLength < len) {
                    throw new IOException("File length is [" + len + "] but read [" + readLength + "]!");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return bytes;
        }
    }

    /**
     * 读取文件，返回字符串
     *
     * @param file
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String readString(File file, String charset) throws UnsupportedEncodingException {
        return new String(readBytes(file), charset);
    }

    /**
     * 读取文件，返回字符串
     *
     * @param in
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String readString(InputStream in, String charset) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
        StringBuilder out = new StringBuilder();
        String line;
        try {
            while ((line = (reader.readLine())) != null) {
                out.append(line);
            }
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
