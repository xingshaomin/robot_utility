package robot.kb;

import robot.GenUtil;

public class QueryKBCollection {
	private static final String uid = "851a1e94c28a44b39f8860def309c1e4";
	public static void main( String[] args){
		String curlCommandLine = GenUtil.getCurlCmd(GenUtil.POST, "/kb/customs/" + uid);
		System.out.println(curlCommandLine + " -d '{\"start\":0,\"limit\":20}'");
    }
}
