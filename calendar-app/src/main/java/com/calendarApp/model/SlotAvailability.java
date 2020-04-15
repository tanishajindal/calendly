package com.calendarApp.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Set;

@Value.Immutable
@JsonSerialize(as = ImmutableSlotAvailability.class)
@JsonDeserialize(as = ImmutableSlotAvailability.class)

public interface SlotAvailability {
    LocalDate getDate();
    Set<Time> getSlots();
}
