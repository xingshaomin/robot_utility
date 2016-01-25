package robot.conf.domains;
import robot.GenUtil;

public class QueryDomain 
{
    public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.GET, "/conf/domains/7e08fbba62e347b086a68215e99f9c3f");
    	System.out.println(cmd);
    }
}
