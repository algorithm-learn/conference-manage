package org.akj.algorithm.conference.entity;

public enum BucketCapacityEnum {
	MORNING(180), AFTERNOON(240);

	private Integer capacity;

	BucketCapacityEnum(Integer capacity) {
		this.capacity = capacity;
	}

	public Integer value() {
		return this.capacity;
	}
}
