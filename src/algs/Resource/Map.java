package algs.Resource;

public class Map implements Cloneable {
	//地图数据来源文件
	public static String  filepath;//="node150uniform";
	public Node[] node;
	public int csNum;//充电站数量
	public int wsNum;//无线传感器的数量
	public int totalNum;//充电站+无线传感器的总数量
	public float[][]distance;
	public int [] whichChargingStationIsCloser;


	public float[][] getDistance() {
		return distance;
	}
	public void setDistance(float[][] distance) {
		this.distance = distance;
	}

	public Map clone() 
	{  
	   Map map = null;  
	    try{  
	        map = (Map)super.clone();  
	       }catch(CloneNotSupportedException e) 
	    {  
	        e.printStackTrace();  
	    }  
	    return map;  
	}



}  