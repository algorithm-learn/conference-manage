package org.akj.algorithm.conference.factory;

import org.akj.algorithm.conference.entity.Bucket;
import org.akj.algorithm.conference.entity.BucketCapacityEnum;

public class BucketFactory {
	
	public static Bucket getBucket(BucketCapacityEnum bucketType, String trackerID) {
		
		return new Bucket(bucketType, trackerID);
		
	}

}
