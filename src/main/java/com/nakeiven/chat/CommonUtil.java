package com.nakeiven.chat;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 工具类
 */
public class CommonUtil {

    /**
     * 格式化打印
     * @param msg
     */
    public static void println(String msg) {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy=MM-dd HH:mm:ss ");
        System.out.println(Thread.currentThread().getName() + "---->" + format.format(date) + msg);
    }

    /**
     * 关闭客户端socket
     */
    public static void closeSocket(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                socket = null;
            }
        }
    }

    static class PrintThread implements Runnable {
        InputStream input;
        String msgType;
        public PrintThread(InputStream in, String msgType) {
            this.input = in;
            this.msgType = msgType;
        }

        public void run() {
            byte[] bytes = new byte[1024];
            int len;
            try {
                //当收到消息时把消息打印到控制台
                while ((len = input.read(bytes)) != -1) {
                    CommonUtil.println("收到来自" + msgType +
                            "的消息：" + new String(bytes, 0, len));
                }
            } catch (Exception e) {
                //当服务端或（输入quit时）会关闭socket从而抛出java.net.SocketException: Socket closed异常
                //当服务端输入quit时隐藏此异常不做处理让程序继续运行（回到等待客户端接入的状态）
                if (!(e instanceof SocketException)) {
                    e.printStackTrace();
                }
            }

        }
    }
}
