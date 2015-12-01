package robot.conf.defaultreply;

import robot.GenUtil;

public class QueryDefaultReply {

	private static final String uid = "38490282ae77429dbe86a9bd2ab75d36";
	private static final String id = "10e8e7ba47eb4003963b7406183ac71d";

	public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.GET, "/conf/defaultreply/" + uid + "/" + id);
    	System.out.println(cmd);
    }
}
