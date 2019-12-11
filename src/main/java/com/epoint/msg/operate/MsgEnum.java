package com.epoint.msg.operate;

public class MsgEnum {

	public enum ActionType {
		OpenUrl("0");
		String value;

		ActionType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
}
