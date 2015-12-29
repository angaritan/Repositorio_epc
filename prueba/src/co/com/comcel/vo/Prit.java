package co.com.comcel.vo;

public class Prit {
	
	private UsageCharge usageCharge;
	private RecurringCharge recurringCharge;
	private OneTimeCharge oneTimeCharge;
	private int row;
	
	public Prit(){
		this.usageCharge = new UsageCharge();
		this.recurringCharge = new RecurringCharge();
	}
	
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public UsageCharge getUsageCharge() {
		return usageCharge;
	}
	public void setUsageCharge(UsageCharge usageCharge) {
		this.usageCharge = usageCharge;
	}
	public RecurringCharge getRecurringCharge() {
		return recurringCharge;
	}
	public void setRecurringCharge(RecurringCharge recurringCharge) {
		this.recurringCharge = recurringCharge;
	}

	/**
	 * @return the oneTimeCharge
	 */
	public OneTimeCharge getOneTimeCharge() {
		return oneTimeCharge;
	}

	/**
	 * @param oneTimeCharge the oneTimeCharge to set
	 */
	public void setOneTimeCharge(OneTimeCharge oneTimeCharge) {
		this.oneTimeCharge = oneTimeCharge;
	}
	
}
