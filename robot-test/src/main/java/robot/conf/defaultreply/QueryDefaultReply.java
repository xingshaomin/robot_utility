package robot.conf.defaultreply;

import robot.GenUtil;

public class QueryDefaultReply {

	private static final String uid = "3a97143c90884a4f9b02cc6462b9e59e";
	private static final String id = "d4eff5966b8b47efba79b0b02e5a905b";

	public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.GET, "/conf/defaultreply/" + uid + "/" + id);
    	System.out.println(cmd);
    }
}
