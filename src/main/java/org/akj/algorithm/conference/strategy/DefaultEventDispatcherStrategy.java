package org.akj.algorithm.conference.strategy;

import java.util.List;
import java.util.Optional;

import org.akj.algorithm.conference.entity.Bucket;
import org.akj.algorithm.conference.entity.Event;

public class DefaultEventDispatcherStrategy implements EventDispacherStrategy {

	@Override
	public Bucket getTargetBucket(Event event, List<Bucket> buckets) {

		// 1. check the bucket with low use ratio
		Optional<Bucket> min = buckets.stream().min((x, y) -> x.compareTo(y));
		if (event.getDuration() <= min.get().getRemainingCapacity())
			return min.get();

		// 2. to get a bucket using normal approach
		Optional<Bucket> targetBucket = buckets.stream().filter(bucket -> {
			return (bucket.getRemainingCapacity() >= event.getDuration());
		}).findAny();

		return targetBucket.get();
	}

}
