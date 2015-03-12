package zzserver;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import service.ServerThread;
import model.UserSocketMap;

public class ZZServer {
	public static final int SERVER_PORT = 30000;
	// 使用这个对象来保存每个客户名字和对应输出流之间的关系
	public static UserSocketMap<String, PrintStream> clients = new UserSocketMap<>();
	public void init() {
		//此try出现故障自动关闭
		try (
		// 建立serverscoket
		ServerSocket ss = new ServerSocket(SERVER_PORT)) {
			while (true) {
				Socket socket = ss.accept();
				new ServerThread(socket).start();
			}
		} catch (IOException e) {
			System.out.println("服务端启动失败，检查端口"+SERVER_PORT+"是否被占用!");
		}
	}
	
	public static void main(String[] args)
	{
		ZZServer server = new ZZServer();
		server.init();
	}
}
