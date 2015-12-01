package robot.conf.domains;
import robot.GenUtil;

public class CreateDomain 
{
    public static void main( String[] args){
    	GenUtil.getCurlCmd(GenUtil.GET, "/conf/domains/ba69f141a0914b83ae785dcd6986dec6");
    }
}
