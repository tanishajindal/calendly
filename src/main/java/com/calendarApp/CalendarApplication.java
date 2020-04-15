package com.calendarApp;
import com.calendarApp.resource.CalendarResource;
import com.calendarApp.resource.LoginResource;
import com.calendarApp.state.DatabaseMock;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class CalendarApplication extends Application<CalendarConfiguration> {

    public static void main(String[] args) throws Exception {
        new CalendarApplication().run(new String[] {"server"});
    }

    public void run(CalendarConfiguration configuration, Environment environment) {
        // Initialize database mock class
        DatabaseMock.getInstance();
        // setup resources
        environment.jersey().register(new LoginResource());
        environment.jersey().register(new CalendarResource());
    }
}