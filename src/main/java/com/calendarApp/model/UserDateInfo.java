package com.calendarApp.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.sql.Time;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;


/**
 * For now there is only one property later on we can extend this model further..
 */
@Value.Immutable
@JsonSerialize(as = ImmutableUserDateInfo.class)
@JsonDeserialize(as = ImmutableUserDateInfo.class)
public interface UserDateInfo {
    @Nullable
    HashMap<Time, UUID> getBookings();
    @Nullable
    Set<Time> getAvailability();
}
