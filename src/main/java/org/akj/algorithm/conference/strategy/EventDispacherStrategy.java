package org.akj.algorithm.conference.strategy;

import java.util.List;
import java.util.Optional;

import org.akj.algorithm.conference.entity.Bucket;
import org.akj.algorithm.conference.entity.Event;

public interface EventDispacherStrategy {
	
	public Optional <Bucket> getTargetBucket(Event event, List<Bucket> buckets);
	
}
