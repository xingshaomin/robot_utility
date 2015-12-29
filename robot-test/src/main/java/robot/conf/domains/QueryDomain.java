package robot.conf.domains;
import robot.GenUtil;

public class QueryDomain 
{
    public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.GET, "/conf/domains/9979352196914ae0b9f0cfb6e6aebc42");
    	System.out.println(cmd);
    }
}
