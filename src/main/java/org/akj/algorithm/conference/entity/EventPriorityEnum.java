package org.akj.algorithm.conference.entity;

public enum EventPriorityEnum {
	HIGH(1), NORMAL(0), LOW(-1);

	Integer priority;

	EventPriorityEnum(Integer priority) {
		this.priority = priority;
	}

	public Integer value() {
		return this.priority;
	}
}
