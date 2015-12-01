package robot.conf.defaultreply;

import robot.GenUtil;

public class DeleteDefaultReply {
	private static final String id = "76fbf876a63d4c4aa900b3807bd247db";

	public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.DEL, "/conf/defaultreply/" + id);
    	System.out.println(cmd);
    }
}
