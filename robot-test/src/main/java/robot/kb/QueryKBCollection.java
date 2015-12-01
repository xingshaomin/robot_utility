package robot.kb;

import robot.GenUtil;

public class QueryKBCollection {
	private static final String uid = "575f693644c84825b7ce871c65956d91";
	public static void main( String[] args){
		String curlCommandLine = GenUtil.getCurlCmd(GenUtil.POST, "/kb/customs/" + uid);
		System.out.println(curlCommandLine + " -d '{\"start\":0,\"limit\":20}'");
    }
}
