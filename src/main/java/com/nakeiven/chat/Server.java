package com.nakeiven.chat;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 聊天服务端
 */
public class Server {



    // 单例的ServerSocket
    private static ServerSocket serverSocket;

    public static void start(BufferedReader in) {
        start(in, CommonConstant.DEFAULT_PORT);
    }

    private synchronized static void start(BufferedReader reader, int port) {
        if (serverSocket != null) {
            return;
        }

        PrintWriter out = null;
        try {
            serverSocket = new ServerSocket(port);
            CommonUtil.println("服务端已启动！！！");
            Socket socket = serverSocket.accept();
            CommonUtil.println("客户端已接入！");
            // 创建新线程，负责打印接收到的消息
            new Thread(new CommonUtil.PrintThread(socket.getInputStream(), CommonConstant.MSG_TYPE_CLIENT)).start();
            // 输出流
            out = new PrintWriter(socket.getOutputStream(), true);
            String msg;
            while ((msg = reader.readLine()) != null && !msg.equals("bye")) {
                out.println(msg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (serverSocket != null) {
                CommonUtil.println("服务端已关闭");
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    serverSocket = null;
                }
            }
        }
    }


    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                Server.start(reader);
            }
        }).start();
    }
}
