package com.calendarApp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableValidationResult.class)
@JsonDeserialize(as = ImmutableValidationResult.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface ValidationResult {
    List<String> getErrors();
    Object getEntity();
}
