package robot.conf.repeatreply;

import robot.GenUtil;

public class DeleteRepeatReply {
	private static final String id = "bfde9d56530148d1b34f6d51ac582e67";

	public static void main( String[] args){
		String cmd = GenUtil.getCurlCmd(GenUtil.DEL, "/conf/repeatreply/" + id);
		System.out.println(cmd);
    }
}
