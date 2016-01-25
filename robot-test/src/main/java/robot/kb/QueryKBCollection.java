package robot.kb;

import robot.GenUtil;

public class QueryKBCollection {
	private static final String uid = "5779d0e95784470283ab43c8f2d13f05";
	public static void main( String[] args){
		String curlCommandLine = GenUtil.getCurlCmd(GenUtil.POST, "/kb/customs/" + uid);
		System.out.println(curlCommandLine + " -d '{\"start\":0,\"limit\":20}'");
    }
}
