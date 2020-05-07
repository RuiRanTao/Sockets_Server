package src;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class server1 {

            /**
14      * Socket服务端
15      */
            public static void main(String[] args) throws IOException{

                        ServerSocket serverSocket=new ServerSocket(8086);
                        System.out.println("服务端已启动，等待客户端连接..");

                    while(true) {
                        Socket socket=serverSocket.accept();//侦听并接受到此套接字的连接,返回一个Socket对象
                        LoginThread newThread =new LoginThread(socket);
                        newThread.start();

                    }
            }
        }

