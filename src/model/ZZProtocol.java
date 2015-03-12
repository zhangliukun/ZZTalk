package model;

public interface ZZProtocol {

	//协议长度
	int PROTOCOL_LEN = 2;
	//接口内定义的变量都是public static final类型的
	String MSG_ONE = "#1"; //发送给单个人的
	String MSG_ALL = "#2"; //群发的
	String USER_LOGIN = "#3"; //用户登陆的
	String MSG_SPLIT_SIGN = ":"; //用户名与消息的分隔符
	
	String USERNAME_REP ="-1"; //用户名重名
	String lOGIN_SUCCESS = "11"; //登陆成功
}
