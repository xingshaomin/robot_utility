package robot.conf.domains;
import robot.GenUtil;

public class QueryDomain 
{
    public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.GET, "/conf/domains/71e2ebc1a75a4ee48befc1948aee5402");
    	System.out.println(cmd);
    }
}
