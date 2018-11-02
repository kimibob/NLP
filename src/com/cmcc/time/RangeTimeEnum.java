package com.cmcc.time;

/**
 *
 * Description: 范围时间的默认时间点<br>
 * 
 * Modified log:<br>
 * ------------------------------------------------------<br>
 * Ver. Date Author Description<br>
 * ------------------------------------------------------<br>
 * 1.0 2018年6月21日 上午11:35:29 zhouqiang created.<br>
 */

public enum RangeTimeEnum {
	day_break(3), early_morning(8), // 早
	morning(10), // 上午
	noon(12), // 中午、午间
	afternoon(15), // 下午、午后
	night(18), // 晚上、傍晚
	lateNight(20), // 晚、晚间
	midNight(23); // 深夜

	private int hourTime = 0;

	/**
	 * @param hourTime
	 */
	private RangeTimeEnum(int hourTime) {
		this.setHourTime(hourTime);
	}

	/**
	 * @return the hourTime
	 */
	public int getHourTime() {
		return hourTime;
	}

	/**
	 * @param hourTime
	 *            the hourTime to set
	 */
	public void setHourTime(int hourTime) {
		this.hourTime = hourTime;
	}
}
