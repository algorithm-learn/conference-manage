package org.akj.algorithm.conference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;

import org.akj.algorithm.conference.entity.Bucket;
import org.akj.algorithm.conference.entity.BucketCapacityEnum;
import org.akj.algorithm.conference.entity.Event;
import org.akj.algorithm.conference.service.DispatcherService;

import lombok.Setter;

/**
 * Conference track management application
 *
 */
@Setter
public class Application {
	private DispatcherService dispatcher;

	private List<Bucket> buckets;

	public Application(DispatcherService dispatcher) {
		this.dispatcher = dispatcher;

	}

	public void run(List<Event> events) {
		this.init(events);
		buckets = dispatcher.doDispatch(events, buckets);

		// TODO not good to put it here
		handleAdditonalEvents(buckets);
		this.print(buckets);
	}

	/**
	 * add network event and lunch event
	 * 
	 * @param buckets
	 */
	private void handleAdditonalEvents(List<Bucket> buckets) {
		buckets.stream().filter(bucket -> {
			return bucket.getMaxCapacity().value() == BucketCapacityEnum.AFTERNOON.value();
		}).forEach(bucket -> {
			// to add network event
			Calendar cal = new Calendar.Builder().setTimeZone(TimeZone.getDefault()).set(Calendar.HOUR_OF_DAY, 17)
					.build();
			Event event = Event.builder().subject("Networking Event").duration(0).start(cal.getTime()).build();
			bucket.add(event);

			// to add lunch event
			Calendar temp = new Calendar.Builder().setTimeZone(TimeZone.getDefault()).set(Calendar.HOUR_OF_DAY, 12)
					.build();
			Event lunchEvent = Event.builder().subject("Lunch").duration(0).start(temp.getTime()).build();
			bucket.add(lunchEvent);
		});
	}

	/**
	 * init event buckets against the events passed in
	 * 
	 * @param events
	 */
	private void init(List<Event> events) {
		Integer sum = events.stream().mapToInt(Event::getDuration).sum();
		int cnt = 0;
		cnt = (int) Math
				.ceil((double) sum / (BucketCapacityEnum.MORNING.value() + BucketCapacityEnum.AFTERNOON.value()));

		if (cnt == 0) {
			throw new IllegalStateException("illegal system state - no event bucket will be created...");
		}

		buckets = new ArrayList<Bucket>(cnt);

		for (int i = 0; i < cnt; i++) {
			String trackerID = UUID.randomUUID().toString();
			Bucket morning = new Bucket(BucketCapacityEnum.MORNING, trackerID);
			Bucket afternoon = new Bucket(BucketCapacityEnum.AFTERNOON, trackerID);

			buckets.add(morning);
			buckets.add(afternoon);
		}
	}

	private void print(List<Bucket> buckets) {
		Integer cnt = 1;
		Map<String, List<Bucket>> collect = buckets.stream().collect(Collectors.groupingBy(Bucket::getTrackerNum));
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mma");

		for (String key : collect.keySet()) {
			System.out.println("Track " + cnt++);
			collect.get(key).stream().sorted().forEach(bucket -> {
				List<Event> events = bucket.getEvents();
				Collections.sort(events);
				events.stream().forEach(event -> {
					StringBuffer buffer = new StringBuffer();
					buffer.append(formatter.format(event.getStart())).append(" ");
					if (event.getSubject().endsWith("lightning")) {
						buffer.append(event.getSubject().subSequence(0, event.getSubject().lastIndexOf("lightning")));
					}

					if (null != event.getDuration() && 0 != event.getDuration()) {
						buffer.append(" ").append(event.getDuration()).append("min");
					}
					System.out.println(buffer.toString());
				});
			});

			System.out.println("");
		}
	}
}
