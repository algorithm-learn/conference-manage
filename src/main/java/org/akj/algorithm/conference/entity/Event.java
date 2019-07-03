package org.akj.algorithm.conference.entity;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Event implements Comparable<Event> {
	private String subject;

	private Date start;

	private Date end;

	private Integer duration = 0;

	private EventPriorityEnum priority = EventPriorityEnum.NORMAL;

	@Override
	public int compareTo(Event o) {
		// time ascending order
		return start.compareTo(o.getStart());
	}

}
