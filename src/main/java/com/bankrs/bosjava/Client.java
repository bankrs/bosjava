package com.bankrs.bosjava;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Contains the high-level attributes of the client, reused on each
 * client derived from it.
 */
public class Client {
    // Version is the current version of the bosgo library.
    private final String Version = "0.1";
    // DefaultUserAgent is the default user agent header used by the bosgo library.
    private final String DefaultUserAgent = "bosgo-bankrs-os-bosjava/" + Version;
    private final String appV1 = "/v1";

    private String addr;
    private String ua;
    private String appVersion = appV1;
    private WebClient wc;

    private Client() {
    }

    private String getUserAgent() {
        if(StringUtils.isEmpty(this.ua)) {
            return DefaultUserAgent;
        }
        return DefaultUserAgent + " " + this.ua;
    }

    /**
     * Creates a new Bankrs API client using the provided API host address and User-Agent header
     */
    public static Client newClient(final String addr, final String ua) {
        Client c = new Client();
        c.addr = addr;
        c.ua = ua;
        c.wc = WebClient
                .builder()
                .baseUrl(c.addr + c.appVersion)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .defaultHeader(HttpHeaders.USER_AGENT, c.getUserAgent())
                .build();
        return c;
    }

    public AppClient newAppClient(final String applicationKey) {
        return AppClient.newAppClient(wc, applicationKey);
    }
}
