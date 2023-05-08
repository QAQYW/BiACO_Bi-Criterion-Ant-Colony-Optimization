package resource;

/**
 * position of a task or base station
 * @author wyqaq
 *
 */
public class Node {
	
	private double x, y;// coordinate of a node
	
	private double dataTime;
	private double calcTime;
	private double resTime;
	private double transTime;// transTime = dataTime + resTime, i.e., the sum of transmission time
	private double ddl;
	private double slotTime;// slotTime = deadline - transTime - calcTime
	
	public double calcTimeLeft;
	
	public static double ddlPara;
	public static double dataPara;
	public static double resPara = 0.01;
	
	public Node() {
		calcTimeLeft = calcTime;
	}
	
	public Node(double x, double y, double calcTime) {
		this.setX(x);
		this.setY(y);
		this.calcTime = calcTime;
		ddl = calcTime * ddlPara;
		dataTime = calcTime * dataPara;
		resTime = calcTime * resPara;
		transTime = dataTime + resTime;
		slotTime = ddl - transTime - calcTime;
		
		calcTimeLeft = calcTime;
	}
	
	public void resetCalcTimeLeft() {
		calcTimeLeft = calcTime;
	}

	public double getDataTime() {
		return dataTime;
	}

	public void setDataTime(double dataTime) {
		this.dataTime = dataTime;
	}

	public double getResTime() {
		return resTime;
	}

	public void setResTime(double resTime) {
		this.resTime = resTime;
	}

	public double getSlotTime() {
		return slotTime;
	}

	public void setSlotTime(double slotTime) {
		this.slotTime = slotTime;
	}

	public double getDdl() {
		return ddl;
	}

	public void setDdl(double ddl) {
		this.ddl = ddl;
	}

	public double getTransTime() {
		return transTime;
	}

	public void setTransTime(double transTime) {
		this.transTime = transTime;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public double getCalcTime() {
		return calcTime;
	}
}
