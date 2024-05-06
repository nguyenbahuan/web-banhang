package com.spring.electronicshop.contants;

public enum OderStatus {
	PENDING(0), APPROVED(1), REJECTED(2), DELIVERING(3), COMPLETED(4), CANCEL(5), REFUND(6);

	private final int code;

	OderStatus(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
