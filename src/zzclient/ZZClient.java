package zzclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import service.ClientThread;
import model.ZZProtocol;

public class ZZClient {
	private static final int SERVER_PORT = 30000;
	private Socket socket;
	private PrintStream ps;
	private BufferedReader brserver;
	private BufferedReader keyIn;
	
	public void init()
	{
		try {
			//初始化代表键盘的输入流
			keyIn = new BufferedReader(new InputStreamReader(System.in));
			//连接到服务器端
			socket = new Socket("127.0.0.1",SERVER_PORT);
			//获取该Socket对应的输入输出流
			ps = new PrintStream(socket.getOutputStream());
			
			brserver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String tip = "";
			//采用循环不断的弹出对话框要求输入用户名
			while (true) {
				String userName = JOptionPane.showInputDialog(tip + "输入用户名");
				//在用户输入的用户名前后增加协议字符串后发送
				ps.println(ZZProtocol.USER_LOGIN + userName + ZZProtocol.USER_LOGIN);
				//读取服务器的响应
				String result = brserver.readLine();
				//如果用户名重复，则开始下一个循环
				if (result.equals(ZZProtocol.USERNAME_REP)) {
					tip = "用户名重复，请重新输入";
					continue;
				}
				//如果服务器返回登陆成功，则结束循环
				if (result.equals(ZZProtocol.lOGIN_SUCCESS)) {
					break;
				}
			}
		} catch (UnknownHostException e) {
			System.out.println("找不到远程服务器，请确定服务器已经启动!");
			closeRs();
			System.exit(1);
		}catch (IOException e) {
			System.out.println("网络异常，请重新登陆");
			closeRs();
			System.exit(1);
		}
		//以该Socket对应的输入流启动ClientThread线程
		new ClientThread(brserver).start();
	}
	
	//定义一个读取键盘输出，并向网络发送的方法
	private void readAndSend()
	{
		try {
			//不断地读取键盘输入
			String line = null;
			while ((line = keyIn.readLine())!=null) {
				//如果发送的信息有冒号，且以/开头，则认为想要发送私聊消息
				if (line.indexOf(":")>0 && line.startsWith("/")) {
					line = line.substring(1);
					ps.println(ZZProtocol.MSG_ONE + line+ ZZProtocol.MSG_ONE);
				}
				else {
					ps.println(ZZProtocol.MSG_ALL + line + ZZProtocol.MSG_ALL);
				}
			}
		}
		//捕获异常，关闭网络资源，并退出该程序
		catch (IOException e) {
			System.out.println("网络通信异常，请重新登陆");
			closeRs();
			System.exit(1);
		}
	}
	
	//关闭socket,输入流，输出流的方法
	
	private void closeRs()
	{
		try {
			if (keyIn!=null) {
				ps.close();
			}
			if (brserver!=null) {
				ps.close();
			}
			if (ps!=null) {
				ps.close();
			}
			if (socket!=null) {
				keyIn.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		ZZClient client = new ZZClient();
		client.init();
		client.readAndSend();
	}
}
