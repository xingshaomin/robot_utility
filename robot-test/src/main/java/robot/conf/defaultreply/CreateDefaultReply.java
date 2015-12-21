package robot.conf.defaultreply;

import robot.GenUtil;

public class CreateDefaultReply {
	private static final String uid = "9a46ed8ab4964d918cdac1e432434071";

	public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.POST, "/conf/defaultreply/" + uid);
    	System.out.println(cmd + " -d '{\"ext\":{\"msgtype\":{\"choice\":{\"list\":[\"环信IM介绍\",\"环信移动客服\"],\"title\":\"请选择您要咨询的问题类型：\"}}}}'");
    }
}
