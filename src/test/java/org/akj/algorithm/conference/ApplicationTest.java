package org.akj.algorithm.conference;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

import org.akj.algorithm.conference.constant.Constant;
import org.akj.algorithm.conference.entity.Event;
import org.akj.algorithm.conference.entity.EventPriorityEnum;
import org.akj.algorithm.conference.service.DispatcherService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class ApplicationTest {
	private final static String fileName = "input.txt";

	private List<Event> events;

	@SuppressWarnings("static-access")
	@BeforeEach
	public void setup() throws IOException, URISyntaxException {
		URI uri = this.getClass().getClassLoader().getSystemResource(fileName).toURI();
		List<String> inputs = Files.readAllLines(Paths.get(uri));

		events = inputs.stream().map(item -> {
			String[] temp = item.split(Constant.SPACE);
			if (item.endsWith(Constant.LIGHTNING_STR)) {
				return Event.builder().subject(item).priority(EventPriorityEnum.LOW).duration(Constant.LIGHTNING_EVENT_DURATION).build();
			} else {
				String str = temp[temp.length - 1];
				if (str.matches("\\d*min")) {
					return Event.builder().subject(item.substring(0, item.lastIndexOf(str)).trim())
							.duration(Integer.valueOf(str.substring(0, str.lastIndexOf("min")))).build();
				} else {
					throw new InvalidParameterException("invalid input...");
				}
			}
		}).collect(Collectors.toList());
	}

	@Test
	public void test() {
		Assertions.assertNotNull(events);
	}
	
	@Test
	public void testRun() {
		new Application(new DispatcherService()).run(events);
	}
}
