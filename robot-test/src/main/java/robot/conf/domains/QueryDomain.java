package robot.conf.domains;
import robot.GenUtil;

public class QueryDomain 
{
    public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.GET, "/conf/domains/5dc445a2ed1742ab9cacfcb54cf18cc5");
    	System.out.println(cmd);
    }
}
