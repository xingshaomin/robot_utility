package robot.kb;

import robot.GenUtil;

public class CreateKB {
	private static final String uid = "5779d0e95784470283ab43c8f2d13f05";
	public static void main( String[] args){
		String cmd = GenUtil.getCurlCmd(GenUtil.POST, "/kb/custom/" + uid);
		System.out.println(cmd);
    }
}
