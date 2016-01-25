package robot.conf.defaultreply;

import robot.GenUtil;

public class DeleteDefaultReply {
	private static final String id = "d4eff5966b8b47efba79b0b02e5a905b";

	public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.DEL, "/conf/defaultreply/" + id);
    	System.out.println(cmd);
    }
}
