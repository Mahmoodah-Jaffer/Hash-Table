/**
Store datetime, voltage and power in an object called PowerStation
@author Mahmoodah Jaffer - JFFMAH001
@since 25 February 2019
*/

/**
PowerStation class takes in given datetime, power and voltage and stores it so that it can be used as an object
*/

public class PowerStation{

	private String datetime;
	private String power;
	private String voltage;

	/**
	 *Method PowerStation class takes in given datetime, power and voltage
	 *@param dt String
	 *@param p String
	 *@param v String
	*/
	public PowerStation(String dt, String p, String v){

		this.datetime = dt;
		this.power = p;
		this.voltage = v; 
	}

	/**
	Method getDateTime returns datetime
	@return String datetime
	*/
	public String getDateTime(){
		return datetime;
	}

	/**
	Method getPower returns power
	@return String power
	*/
	public String getPower(){

		return power;
	}

	/**
	Method getVoltage returns voltage
	@return String voltage
	*/
	public String getVoltage(){

		return voltage;
	}

	/**
	Method toString converts PowerStation Class to string
	@return String 
	*/
	public String toString(){

		return "Date/time: " + datetime + " Power: " + power + " Voltage: " + voltage;
	}

}