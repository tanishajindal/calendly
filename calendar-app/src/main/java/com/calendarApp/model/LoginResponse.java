package com.calendarApp.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.UUID;

@Value.Immutable
@JsonSerialize(as = ImmutableLoginResponse.class)
@JsonDeserialize(as = ImmutableLoginResponse.class)
public interface LoginResponse {
	UUID getToken();
	UUID getUserId();
}