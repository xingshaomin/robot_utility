package robot.kb;

import robot.GenUtil;

public class CreateKB {
	private static final String uid = "190b78a8f01540199273ca2fe840f028";
	public static void main( String[] args){
		String cmd = GenUtil.getCurlCmd(GenUtil.POST, "/kb/custom/" + uid);
    }
}
