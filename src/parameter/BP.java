package parameter;

/**
 * All the parameters used in experiments
 * @author wyqaq
 *
 */
public class BP {
	/*
	 * All parameters are divided into five parts:
	 * 
	 * 1 - Param. to generate instance
	 * 2 - Param. to set the environment
	 * 3 - All compared algorithms
	 * 4 - Components of bi-ACO
	 * 5 - Repeat
	 */
	
	
	/*
	 * Part 1:
	 * Param. to generate instance
	 */
	
	// maps
	public static String[] mapName = {"node48uniform", "node60uniform", "node150uniform", "node532uniform"};
	
	// the pair of speed and full energy
	public static String[] speedAndEnergy = {"22_850", "30_1000", "40_1250", "50_1750"};
	
	// data transmission param. (result transmission param. is fixed to 0.01)
	public static String[] dataTransmission = {"0.25", "0.5", "0.75"};
	
	// deadline param.
	public static String[] ddl = {"5", "10", "15"};
	
	
	/*
	 * Part 2:
	 * Param. to set the environment
	 */
	
	// price magnification between reserved UAV and on-demand UAV
	public static String[] priceMag = {"2", "4", "6"};
	
	// reserved factor
	public static String[] reserved = {"20", "25"};
//	public static String[] reserved = {"10", "15", "20"};
	
	
	/*
	 * Part 3:
	 * All compared algorithms
	 */
	
	public static String[] mainMethods = {"biACO", "ACO-DSP", "NSGA-II", "SA", "GLS", "VND"};
	
	
	/*
	 * Part 4:
	 * Components of bi-ACO
	 */
	
	public static String[] breakMethod = {"break", "none"};
	public static String[] divisionMethod = {"division", "none"};
	
	
	/*
	 * Part 5:
	 * Repeat
	 */
	
	public static int repeatTimes = 5;
}
