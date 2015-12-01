package robot.kb;

import robot.GenUtil;

public class DeleteKB {
//	private static final String id = "ff0e9bb9ccf244d589455dcc428585dd";
	private static final String[] ids = {"33888bbd663c46c79c8930dc1f84551c",
			"97dad6836bf84aa4baed75d40c6e1d4b",
			"63e9155180534587bf256c04b6d5defb",
			"8522e764f6014e1883471f17a33cb942",
			"0a4d93827d9d4cd7b9198ebe17cdf5ca",
			"34afc08ae3894f649065806fdd51e8ae",
			"43a62f3c201346fc8c871df9dc124088"
			};
	public static void main( String[] args){
		for (String id : ids) {
			String cmd = GenUtil.getCurlCmd(GenUtil.DEL, "/kb/custom/" + id);
			System.out.println(cmd);
		}
//		String cmd = GenUtil.getCurlCmd(GenUtil.DEL, "/kb/custom/" + id);
//			System.out.println(cmd);
    }
}
