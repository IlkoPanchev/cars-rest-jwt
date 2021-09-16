package cars.jwt;

import cars.events.logout.OnUserLogoutSuccessEvent;
import cars.exceptions.InvalidTokenRequestException;
import io.jsonwebtoken.Claims;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class LoggedOutJwtTokenCache {

    private ExpiringMap<String, OnUserLogoutSuccessEvent> tokenEventMap;

    private final JwtProvider tokenProvider;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Autowired
    public LoggedOutJwtTokenCache(JwtProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
        this.tokenEventMap = ExpiringMap.builder()
                .variableExpiration()
                .maxSize(1000)
                .build();
    }

    public void markLogoutEventForToken(OnUserLogoutSuccessEvent event)  {
        String authToken = event.getAuthToken();
        if (tokenEventMap.containsKey(authToken)) {
            logger.info(String.format("Log out token [%s] is already present in the cache", event.getAuthToken()));

        } else {
            Date tokenExpiryDate = tokenProvider.getTokenExpiryFromJWT(authToken);
            long ttlForToken = getTTLForToken(tokenExpiryDate);
            logger.info(String.format("Logout token cache set  [%s] with a TTL of [%s] seconds. Token is due expiry at [%s]", event.getAuthToken(), ttlForToken, tokenExpiryDate));
            tokenEventMap.put(authToken, event, ttlForToken, TimeUnit.SECONDS);
        }
    }

    public OnUserLogoutSuccessEvent getLogoutEventForToken(String token) {

        OnUserLogoutSuccessEvent event = tokenEventMap.get(token);
        return event;
    }

    private long getTTLForToken(Date date) {
        long secondAtExpiry = date.toInstant().getEpochSecond();
        long secondAtLogout = Instant.now().getEpochSecond();
        return Math.max(0, secondAtExpiry - secondAtLogout);
    }
}
