package robot.conf.defaultreply;

import robot.GenUtil;

public class CreateDefaultReply {
	private static final String uid = "190b78a8f01540199273ca2fe840f028";

	public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.POST, "/conf/defaultreply/" + uid);
    	System.out.println(cmd + " -d '{\"questions\":[\"测试规则\"],\"answers\":[{\"content\":\"测试回复\",\"type\":\"text\"}]}'");
    }
}
