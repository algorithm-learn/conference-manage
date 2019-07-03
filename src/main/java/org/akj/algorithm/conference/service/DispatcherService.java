package org.akj.algorithm.conference.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.akj.algorithm.conference.entity.Bucket;
import org.akj.algorithm.conference.entity.BucketCapacityEnum;
import org.akj.algorithm.conference.entity.Event;
import org.akj.algorithm.conference.strategy.DefaultEventDispatcherStrategy;

public class DispatcherService {
	private DefaultEventDispatcherStrategy strategy;

	public DispatcherService() {
		this(null);
	}

	public DispatcherService(DefaultEventDispatcherStrategy strategy) {
		this.strategy = strategy;

		// apply default dispatcher strategy
		if (this.strategy == null)
			this.strategy = new DefaultEventDispatcherStrategy();
	}

	public List<Bucket> doDispatch(List<Event> events, List<Bucket> buckets) {

		events.forEach(event -> {
			Bucket targetBucket = strategy.getTargetBucket(event, buckets);

			if (targetBucket.getEvents().size() == 0) {
				Calendar cal = null;
				if (targetBucket.getMaxCapacity().value() == BucketCapacityEnum.MORNING.value()) {

					cal = new Calendar.Builder().setTimeZone(TimeZone.getDefault()).set(Calendar.HOUR_OF_DAY, 9)
							.build();
				} else {
					cal = new Calendar.Builder().setTimeZone(TimeZone.getDefault()).set(Calendar.HOUR_OF_DAY, 13)
							.build();
				}
				event.setStart(cal.getTime());
			} else {
				Date beforeEndTime = new Date(
						targetBucket.getEvents().get(targetBucket.getEvents().size() - 1).getEnd().getTime());
				event.setStart(beforeEndTime);
			}

			Date endTime = new Date(event.getStart().getTime());
			endTime.setTime(event.getStart().getTime() + event.getDuration() * 60 * 1000);
			event.setEnd(endTime);

			targetBucket.add(event);
		});

		return buckets;
	}

}
