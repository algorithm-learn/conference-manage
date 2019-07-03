package org.akj.algorithm.conference.strategy;

import java.util.List;

import org.akj.algorithm.conference.entity.Bucket;
import org.akj.algorithm.conference.entity.Event;

public interface EventDispacherStrategy {
	
	public Bucket getTargetBucket(Event event, List<Bucket> buckets);
	
}
