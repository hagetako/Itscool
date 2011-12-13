package info.hagedev.itscool;

import java.util.Calendar;

public class myCalendar extends Calendar{
	/**
	 *
	 */
	static final long serialVersionUID = 5588623791164656533L;

	private boolean limited;
	private boolean nonstop;

	myCalendar(){
		setLimited(false);
		setNonstop(false);
	}

	myCalendar(int hour, int minute, boolean limited, boolean nonstop){
		setCalendar(hour, minute, limited, nonstop);
	}

	public boolean IsLimited(){
		return limited;
	}
	public boolean IsNonstop(){
		return nonstop;
	}
	public void setLimited(boolean b){
		limited = b;
	}
	public void setNonstop(boolean b){
		nonstop = b;
	}
	public void setCalendar(int hour, int minute, boolean lim, boolean stop){
		this.set(Calendar.HOUR_OF_DAY, hour);
		this.set(Calendar.MINUTE, minute);
		this.setLimited(lim);
		this.setNonstop(stop);
	}

	//implements some abstract methods of Calendar class, but no use.
	@Override
	public void add(int field, int amount){}
	@Override
	public void roll(int field, boolean up){}
	@Override
	protected void computeFields() {}

	@Override
	protected void computeTime() {}

	@Override
	public int getGreatestMinimum(int i) {	return 0;	}

	@Override
	public int getLeastMaximum(int i) {	return 0;	}

	@Override
	public int getMaximum(int i) {	return 0;	}

	@Override
	public int getMinimum(int i) {	return 0;	}
}