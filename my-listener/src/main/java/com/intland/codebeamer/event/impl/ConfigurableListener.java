/*
 * Copyright by Intland Software
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Intland Software. ("Confidential Information"). You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Intland.
 */

package com.intland.codebeamer.event.impl;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Throwables;
import com.intland.codebeamer.event.BaseEvent;
import com.intland.codebeamer.event.TrackerItemListener;
import com.intland.codebeamer.event.util.VetoException;
import com.intland.codebeamer.manager.TrackerItemManager;
import com.intland.codebeamer.manager.TrackerItemManager.ActionOptions;
import com.intland.codebeamer.manager.util.ActionData;
import com.intland.codebeamer.persistence.dto.TrackerItemDto;
import com.intland.codebeamer.persistence.dto.UserDto;
import com.intland.codebeamer.utils.SafeStopWatch;
import com.intland.codebeamer.utils.SafeStopWatch.StopWatchException;

@Component("configurableListener")
public class ConfigurableListener implements TrackerItemListener {

	private static final Logger logger = LogManager.getLogger(ConfigurableListener.class);

	private static final Integer CONFIG_ITEM_ID = 2302;

	private static final Integer ACTIVATED_TRACKERS_FIELD_ID = 0;

	@Autowired
	private TrackerItemManager trackerItemManager;

	@Override
	public void trackerItemUpdated(BaseEvent<TrackerItemDto, TrackerItemDto, ActionData> event) throws VetoException {
		if (event.isPreEvent()) {
			return;
		}

		try {
			// Measure the time of your code
			SafeStopWatch.measure(() -> {
				
				final Set<Integer> activatedTrackerIds = getActivatedTrackerIdList();
				if (CollectionUtils.isNotEmpty(activatedTrackerIds)) {
					updateTrackerItem(event.getUser(), event.getSource().clone(), activatedTrackerIds);
				}
				
			}, taskTimeMillis -> logger.info("Updating tracker item took: {}ms", Long.valueOf(taskTimeMillis)));
		} catch (StopWatchException e) {
			Throwables.throwIfInstanceOf(e.getOriginalException(), VetoException.class);
			throw new IllegalStateException(e.getMessage(), e.getOriginalException());
		}

	}

	private void updateTrackerItem(UserDto user, TrackerItemDto updatedItem, Set<Integer> activatedTrackerIdList) throws VetoException {
		Integer subjectTrackerId = updatedItem.getTracker().getId();

		// Log what your are doing
		logger.info("Configurable Listener is active for the following Tracker IDs: {}", activatedTrackerIdList);
		logger.info("Configurable Listener activated by the following Tracker Item: {}, which is in the following Tracker: {}",
				updatedItem.getId(), subjectTrackerId);

		if (!activatedTrackerIdList.contains(subjectTrackerId)) {
			logger.info("Configurable Listener - Tracker ID is not configured for this Listener.");
			return;
		}

		try {
			logger.info("Configurable Listener entered the active condition for item {}", updatedItem.getId());			
			updatedItem.setDescription("Configurable Listener works");
			trackerItemManager.update(user, updatedItem, getActionDataForUpdate());
		} catch (Exception e) {
			// Log all exception even if it is just rethrow it
			logger.error("Tracker item cannot be updated", e);
			throw new VetoException("Tracker item cannot be updated", e);
		}
	}

	private ActionData<EnumSet<ActionOptions>> getActionDataForUpdate() {
		return new ActionData<>(null, EnumSet.of(
				ActionOptions.DONT_SEND_EMAIL));
	}

	private Set<Integer> getActivatedTrackerIdList() {
		final String activatedTrackerIds = getActivatedTrackersFieldValue();
		if (StringUtils.isBlank(activatedTrackerIds)) {
			logger.info("Configurable Listener - Activated Tracker IDs field is empty, please enter the Tracker IDs you want to use this listener for!");
			return Collections.emptySet();
		}

		return Stream.of(activatedTrackerIds.split(","))
				.map(String::trim)
				.map(Integer::parseInt)
				.collect(Collectors.toSet());
	}

	private String getActivatedTrackersFieldValue() {
		final TrackerItemDto configItem = trackerItemManager.findById(CONFIG_ITEM_ID);
		return configItem.getCustomField(ACTIVATED_TRACKERS_FIELD_ID);
	}
	
}