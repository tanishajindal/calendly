package com.calendarApp.helper;

import com.calendarApp.model.ImmutableLoginResponse;
import com.calendarApp.model.ImmutableValidationResult;
import com.calendarApp.model.User;
import com.calendarApp.model.ValidationResult;
import com.calendarApp.state.DatabaseMock;
import com.google.common.collect.ImmutableList;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.UUID;

import static com.calendarApp.helper.Constants.EXPIRATION_TIME_IN_MINUTES;
import static com.calendarApp.helper.ValidationHelper.isValidUUID;
import static com.calendarApp.helper.ValidationHelper.isValidUser;

public class LoginHelper {

    public static ValidationResult processUserToken(User user) {
        DatabaseMock databaseMock = DatabaseMock.getInstance();
        // clear any active session
        if(databaseMock.getUserTokens().containsKey(user.getId())) {
            clearUserSession(user.getId());
        }

        UUID tokenId = UUID.randomUUID();
        databaseMock.getUserTokens().put(user.getId(), tokenId);
        databaseMock.getActiveTokenSessions().put(tokenId, LocalDateTime.now());
        return ImmutableValidationResult.builder().entity(ImmutableLoginResponse.builder().token(tokenId)
                .userId(user.getId()).build()).build();
    }

    public static void clearUserSession(UUID userId) {
        DatabaseMock databaseMock = DatabaseMock.getInstance();
        UUID activeSessionId = databaseMock.getUserTokens().get(userId);
        databaseMock.getUserTokens().remove(userId);
        databaseMock.getActiveTokenSessions().remove(activeSessionId);
    }

    public static ValidationResult validateAndSlideSession(UUID token, UUID userId) {
        DatabaseMock databaseMock = DatabaseMock.getInstance();
        // check validity of token and userId
        // check if token exists corresponding to given user
        if(!isValidUUID(token) || !validateUser(userId) ||
                !databaseMock.getUserTokens().containsKey(userId) ||
                !databaseMock.getUserTokens().get(userId).equals(token) ||
                !databaseMock.getActiveTokenSessions().containsKey(token)){
            return ImmutableValidationResult.builder()
                    .errors(Collections.singletonList(Constants.AUTHENTICATION_FAILED))
                    .entity(userId).build();
        }

        // check if session is not expired
        if(ChronoUnit.MINUTES.between(databaseMock.getActiveTokenSessions().get(token), LocalDateTime.now()) >
                EXPIRATION_TIME_IN_MINUTES){
            // clear expired session and return validation result
            clearUserSession(userId);
            return ImmutableValidationResult.builder()
                    .errors(Collections.singletonList(Constants.SESSION_EXPIRED))
                    .entity(ImmutableLoginResponse.builder().token(token).userId(userId).build()).build();
        }

        // else slide token ttl window
        databaseMock.getActiveTokenSessions().put(token, LocalDateTime.now());
        return ImmutableValidationResult.builder()
                .errors(ImmutableList.of())
                .entity(ImmutableLoginResponse.builder().token(token).userId(userId).build())
                .build();
    }

    public static boolean validateUser(UUID userId) {
        //check if user id is valid and corresponding user exists
        return isValidUUID(userId) && isValidUser(userId, DatabaseMock.getInstance().getUsers());
    }
}
