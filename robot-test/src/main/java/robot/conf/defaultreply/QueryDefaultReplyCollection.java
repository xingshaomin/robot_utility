package robot.conf.defaultreply;

import robot.GenUtil;

public class QueryDefaultReplyCollection {

	private static final String uid = "26d2835e2b134c55bebab31acfaa5405";

	public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.GET, "/conf/defaultreply/" + uid);
    	System.out.println(cmd);
    }
}
