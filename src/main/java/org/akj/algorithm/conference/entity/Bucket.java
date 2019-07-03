package org.akj.algorithm.conference.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bucket implements Cloneable, Comparable<Bucket> {
	private BucketCapacityEnum maxCapacity;

	private List<Event> events = null;

	// make sure two bucket in same day could be tracked
	private String trackerNum;

	private Integer remainingCapacity;

	private Integer currentCapacity = 0;

	public Bucket(BucketCapacityEnum maxCapacity, String trackerNum) {
		this.maxCapacity = maxCapacity;
		this.remainingCapacity = maxCapacity.value();
		events = new ArrayList<Event>();
		this.trackerNum = trackerNum;
	}

	public void add(Event event) {
		this.events.add(event);
		this.currentCapacity += event.getDuration();
		this.remainingCapacity = this.maxCapacity.value() - this.currentCapacity;
	}

	@Override
	public int compareTo(Bucket o) {
		return (int) (remainingCapacity / ((double) maxCapacity.value())
				- o.remainingCapacity / ((double) o.maxCapacity.value()));
	}

}
