package robot.conf.defaultreply;

import robot.GenUtil;

public class QueryDefaultReplyCollection {

	private static final String uid = "851a1e94c28a44b39f8860def309c1e4";

	public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.GET, "/conf/defaultreply/" + uid);
    	System.out.println(cmd);
    }
}
