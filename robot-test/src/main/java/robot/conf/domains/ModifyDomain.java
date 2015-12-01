package robot.conf.domains;
import robot.GenUtil;

public class ModifyDomain 
{
    public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.POST, "/conf/domains/b8e42397b1a249b7a968c7c30f67bb38");
    	System.out.println(cmd);
    }
}
