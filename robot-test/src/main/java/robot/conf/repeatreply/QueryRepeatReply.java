package robot.conf.repeatreply;

import robot.GenUtil;

public class QueryRepeatReply {

	private static final String uid = "38490282ae77429dbe86a9bd2ab75d36";
	private static final String id = "262d932ac91d47c3888598e6d6e983e7";

	public static void main( String[] args){
		String cmd = GenUtil.getCurlCmd(GenUtil.GET, "/conf/repeatreply/" + uid + "/" +id);
		System.out.println(cmd);
    }
}
