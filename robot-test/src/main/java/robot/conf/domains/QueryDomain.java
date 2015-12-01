package robot.conf.domains;
import robot.GenUtil;

public class QueryDomain 
{
    public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.GET, "/conf/domains/b20e7d76a72a472f91e7fc4c10c4b57c");
    	System.out.println(cmd);
    }
}
