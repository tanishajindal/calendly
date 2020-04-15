package com.calendarApp.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.sql.Time;
import java.time.LocalDate;
import java.util.UUID;

@Value.Immutable
@JsonSerialize(as = ImmutableBooking.class)
@JsonDeserialize(as = ImmutableBooking.class)
public interface Booking {
    UUID getHost();
    UUID getAttendee();
    LocalDate getDate();
    Time getTime();
}
