package robot.conf.repeatreply;

import robot.GenUtil;

public class QueryRepeatReplyCollection {

	private static final String uid = "71e2ebc1a75a4ee48befc1948aee5402";

	public static void main( String[] args){
		String cmd = GenUtil.getCurlCmd(GenUtil.GET, "/conf/repeatreply/" + uid);
		System.out.println(cmd);
    }
}
