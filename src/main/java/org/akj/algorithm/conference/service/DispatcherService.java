package org.akj.algorithm.conference.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

import org.akj.algorithm.conference.entity.Bucket;
import org.akj.algorithm.conference.entity.BucketCapacityEnum;
import org.akj.algorithm.conference.entity.Event;
import org.akj.algorithm.conference.factory.BucketFactory;
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
			Optional<Bucket> targetBucket = strategy.getTargetBucket(event, buckets);

			if (targetBucket.isEmpty()) {
				// all buckets is 'full'(relatively full, and still has capacity for smaller
				// event), while create new bucket
				String trackerID = UUID.randomUUID().toString();
				Bucket morning = BucketFactory.getBucket(BucketCapacityEnum.MORNING, trackerID);
				Bucket afternoon = BucketFactory.getBucket(BucketCapacityEnum.AFTERNOON, trackerID);

				buckets.add(morning);
				buckets.add(afternoon);
				
				// to get an available bucket again after new bucket created
				targetBucket = strategy.getTargetBucket(event, buckets);
			}
			
			assign(event, targetBucket.get());
		});

		return buckets;
	}

	/**
	 * assign the event to target bucket
	 * 
	 * @param event
	 * @param targetBucket
	 */
	private void assign(Event event, Bucket targetBucket) {
		if (targetBucket.getEvents().size() == 0) {
			Calendar cal = null;
			if (targetBucket.getMaxCapacity().value() == BucketCapacityEnum.MORNING.value()) {
				cal = new Calendar.Builder().setTimeZone(TimeZone.getDefault()).set(Calendar.HOUR_OF_DAY, 9).build();
			} else {
				cal = new Calendar.Builder().setTimeZone(TimeZone.getDefault()).set(Calendar.HOUR_OF_DAY, 13).build();
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
	}

}
