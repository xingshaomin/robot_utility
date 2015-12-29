package robot.conf.domains;
import robot.GenUtil;

public class ModifyDomain 
{
    public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.POST, "/conf/domains/9979352196914ae0b9f0cfb6e6aebc42");
    	System.out.println(cmd);
    }
}
