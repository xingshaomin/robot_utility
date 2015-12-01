package robot.conf.repeatreply;

import robot.GenUtil;

public class DeleteRepeatReply {
	private static final String id = "9eb852b3ad7246cbb57c8cb7667d8a6e";

	public static void main( String[] args){
		String cmd = GenUtil.getCurlCmd(GenUtil.DEL, "/conf/repeatreply/" + id);
		System.out.println(cmd);
    }
}
