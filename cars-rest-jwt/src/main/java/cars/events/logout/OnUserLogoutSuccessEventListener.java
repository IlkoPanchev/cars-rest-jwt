package cars.events.logout;

import cars.exceptions.InvalidTokenRequestException;
import cars.jwt.AuthTokenFilter;
import cars.jwt.LoggedOutJwtTokenCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OnUserLogoutSuccessEventListener implements ApplicationListener<OnUserLogoutSuccessEvent> {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    private final LoggedOutJwtTokenCache tokenCache;

    @Autowired
    public OnUserLogoutSuccessEventListener(LoggedOutJwtTokenCache tokenCache) {
        this.tokenCache = tokenCache;
    }

    public void onApplicationEvent(OnUserLogoutSuccessEvent event)  {
        if (null != event) {
            logger.info(String.format("Log out success event received with token [%s]", event.getAuthToken()));
                tokenCache.markLogoutEventForToken(event);

        }
    }
}
