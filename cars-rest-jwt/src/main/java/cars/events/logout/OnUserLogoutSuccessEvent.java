package cars.events.logout;


import org.springframework.context.ApplicationEvent;

public class OnUserLogoutSuccessEvent extends ApplicationEvent {

    private final String authToken;

    public OnUserLogoutSuccessEvent(Object source, String authToken) {
        super(source);
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }
}
