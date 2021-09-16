package cars.events.logout;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OnUserLogoutSuccessEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public OnUserLogoutSuccessEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
    public void publishLogout(String authToken){
        OnUserLogoutSuccessEvent onUserLogoutSuccessEvent = new OnUserLogoutSuccessEvent(this, authToken);
        this.applicationEventPublisher.publishEvent(onUserLogoutSuccessEvent);
    }

}
