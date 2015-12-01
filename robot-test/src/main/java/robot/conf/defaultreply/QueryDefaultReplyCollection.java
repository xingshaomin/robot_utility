package robot.conf.defaultreply;

import robot.GenUtil;

public class QueryDefaultReplyCollection {

	private static final String uid = "575f693644c84825b7ce871c65956d91";

	public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.GET, "/conf/defaultreply/" + uid);
    	System.out.println(cmd);
    }
}
