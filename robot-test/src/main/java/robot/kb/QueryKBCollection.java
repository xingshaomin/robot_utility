package robot.kb;

import robot.GenUtil;

public class QueryKBCollection {
	private static final String uid = "8207a922984c4ce887a6abcecd123100";
	public static void main( String[] args){
		String curlCommandLine = GenUtil.getCurlCmd(GenUtil.POST, "/kb/customs/" + uid);
		System.out.println(curlCommandLine + " -d '{\"start\":0,\"limit\":20}'");
    }
}
