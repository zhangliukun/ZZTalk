package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Iterator;

import zzserver.ZZServer;
import model.ZZProtocol;

public class ServerThread extends Thread{

	private Socket socket;
	BufferedReader br = null;
	PrintStream ps = null;
	
	//构造器，接受socket
	public ServerThread(Socket socket)
	{
		this.socket = socket;
	}
	public void run(){
		try {
			//获取该Socket的输入流
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//获取输出流
			ps = new PrintStream(socket.getOutputStream());
			String line = null;
			while((line = br.readLine())!=null)
			{
				//用户登陆操作
				if (line.startsWith(ZZProtocol.USER_LOGIN)&&line.endsWith(ZZProtocol.USER_LOGIN)) {
					//得到真实消息
					String userName = getRealMsg(line);
					//如果用户名重复
					if (ZZServer.clients.containsKey(userName)) {
						System.out.println("用户名重复!");
						ps.println(ZZProtocol.USERNAME_REP);
					}
					else {
						System.out.println("登陆成功");
						ps.println(ZZProtocol.lOGIN_SUCCESS);
						ZZServer.clients.put(userName, ps);
					}
				}
				else if (line.startsWith(ZZProtocol.MSG_ONE)&&line.endsWith(ZZProtocol.MSG_ONE)) {
					//得到真实信息
					String userAndMsg = getRealMsg(line);
					//以分隔符分割消息获得用户名和消息
					String user = userAndMsg.split(ZZProtocol.MSG_SPLIT_SIGN)[0];
					String msg = userAndMsg.split(ZZProtocol.MSG_SPLIT_SIGN)[1];
					//获取私聊用户对应的输出流，并发送私聊消息
					ZZServer.clients.get(user).println(ZZServer.clients.getKeyByValue(ps) + "悄悄对你说:" + msg);;
				}
				//群发信息要向每一个socket发送
				else {
					String msg = getRealMsg(line);
					//发送给每个clients的输出流
					for (PrintStream clientPs : ZZServer.clients.valueSet()) {
						clientPs.println(ZZServer.clients.getKeyByValue(ps) + "说: " + msg);
					}
				}
				
			}
		}
		//捕获到异常以后，表明该socket对应的客户端已经出了问题，所以将对应的输出流从map中删掉
		catch (IOException e) {
			ZZServer.clients.removeByValue(ps);
			System.out.println(ZZServer.clients.size());
			//关闭网络，IO资源
			try {
				if (br!=null) {
					br.close();
				}
				if (ps!=null) {
					ps.close();
				}
				if (socket!=null) {
					socket.close();
				}
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}
	//消息处理函数
	public String getRealMsg(String line)
	{
		//去掉协议字符
		return line.substring(ZZProtocol.PROTOCOL_LEN, line.length()-ZZProtocol.PROTOCOL_LEN);
	}
}
