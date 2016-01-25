package robot.conf.domains;
import robot.GenUtil;

public class ModifyDomain 
{
    public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.POST, "/conf/domains/7e08fbba62e347b086a68215e99f9c3f");
    	System.out.println(cmd + " -d '[{\"id\":\"chatRobot\",\"status\":0}]'");
    }
}
