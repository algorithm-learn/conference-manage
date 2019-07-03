package org.akj.algorithm.conference.strategy;

import java.util.List;
import java.util.Optional;

import org.akj.algorithm.conference.entity.Bucket;
import org.akj.algorithm.conference.entity.Event;

public class DefaultEventDispatcherStrategy implements EventDispacherStrategy {

	@Override
	public Optional<Bucket> getTargetBucket(Event event, List<Bucket> buckets) {

		Optional<Bucket> targetBucket = null;

		// 1. check the bucket with lowest use ratio
		targetBucket = buckets.parallelStream().filter(bucket -> {
			return (bucket.getRemainingCapacity() >= event.getDuration());}).min((x, y) -> x.compareTo(y));

		return targetBucket;
	}

}
