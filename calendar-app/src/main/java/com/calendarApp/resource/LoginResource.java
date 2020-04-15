package com.calendarApp.resource;

import com.calendarApp.model.User;
import com.calendarApp.model.ValidationResult;
import io.dropwizard.jersey.caching.CacheControl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.calendarApp.helper.LoginHelper.processUserToken;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("auth")
@Produces(APPLICATION_JSON)
public class LoginResource {

	static Logger LOGGER = Logger.getLogger("Login Resource");

	@GET
	@Path("/login")
	@CacheControl(noCache = true, noStore = true, mustRevalidate = true, maxAge = 0)
	public final Response doLogin(User user) {
		try {
			ValidationResult result = processUserToken(user);
			if(!result.getErrors().isEmpty()) {
				return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
			}
			LOGGER.log(Level.INFO, "Successfully logged in user: " + user.getId());
			return Response.status(Response.Status.CREATED).entity(result).build();
		} catch (Exception ex) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

}