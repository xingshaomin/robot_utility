package robot.conf.defaultreply;

import robot.GenUtil;

public class CreateDefaultReply {
	private static final String uid = "3a97143c90884a4f9b02cc6462b9e59e";

	public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.POST, "/conf/defaultreply/" + uid);
    	System.out.println(cmd + " -d '{\"content\":\"{\\\"ext\\\":{\\\"msgtype\\\":{\\\"choice\\\":{\\\"list\\\":[\\\"环信IM介绍\\\",\\\"环信移动客服\\\"],\\\"title\\\":\\\"请选择您要咨询的问题类型：\\\"}}}}\"}'");
    }
}
