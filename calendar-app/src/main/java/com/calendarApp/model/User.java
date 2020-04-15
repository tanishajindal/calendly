package com.calendarApp.model;

import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.annotation.Nullable;


/**
 * For now there is only one property later on we can extend this model further..
 */
@Value.Immutable
@JsonSerialize(as = ImmutableUser.class)
@JsonDeserialize(as = ImmutableUser.class)
public interface User {
    @Nullable
    UUID getId();
    String getName();
}
