package com.nakeiven.chat;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 聊天客户端
 */
public class Client {

    public static  void send(BufferedReader reader) {
        send(CommonConstant.DEFAULT_SERVER_IP, CommonConstant.DEFAULT_PORT, reader);
    }

    private static void send(String serverIp, int port, BufferedReader reader) {
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            socket = new Socket(serverIp, port);
            CommonUtil.println("客户端已启动！");
            // 输出流，用于发送消息
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("hello, 我是小明！小花，你在不？");
            // 输入流，用于接受消息
            new Thread(new CommonUtil.PrintThread(socket.getInputStream(), CommonConstant.MSG_TYPE_SERVER)).start();

            String msg;
            while ((msg = reader.readLine()) != null && !msg.equals("bye")) {
                out.println(msg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtil.println(ex.getLocalizedMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                out.close();
            }
            CommonUtil.closeSocket(socket);
        }
    }

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader in = null;
                in = new BufferedReader(new InputStreamReader(System.in));
                Client.send(in);
            }
        }).start();
    }
}
