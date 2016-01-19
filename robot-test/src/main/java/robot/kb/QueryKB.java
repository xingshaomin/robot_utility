package robot.kb;

import robot.GenUtil;

public class QueryKB {
	private static final String uid = "5779d0e95784470283ab43c8f2d13f05";
	private static final String id = "c4bb18aec8ed4249a9a4d2e06402c567";
	public static void main( String[] args){
		String curlCommandLine = GenUtil.getCurlCmd(GenUtil.GET, "/kb/custom/" + uid +  "/" + id);
		System.out.println(curlCommandLine);
    }
}
