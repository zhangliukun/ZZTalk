package service;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientThread extends Thread{

	//该客户端线程处理输入流
	BufferedReader br = null;
	public ClientThread(BufferedReader br)
	{
		this.br = br;
	}
	
	public void run()
	{
		try {
			String line = null;
			//不断的从输入流中读取数据，并将这些数据打印输出
			while ((line = br.readLine())!=null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			try {
				if(br!=null)
				{
					br.close();
				}
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}
}
