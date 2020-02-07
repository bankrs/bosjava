package com.bankrs.bosjava;

import com.bankrs.bosjava.model.UserLoginParams;
import com.bankrs.bosjava.model.UserLoginResponse;
import org.junit.Test;
import org.springframework.util.Assert;

public class ClientTest {

    public static final String userAgent = "bosjava/tests";
    public static final String staginBankrsUrl = "https://api.staging.bankrs.com";

    public static final String testUsername = "no@bankrs.com";
    public static final String testPassword = "no";
    public static final String testApplicationKey = "no";

    @Test
    public void testUserLogin() {
        UserLoginResponse response =
            Client.newClient(staginBankrsUrl, userAgent)
                .newAppClient(testApplicationKey)
                    .loginUser(UserLoginParams.builder()
                            .username(testUsername)
                            .password(testPassword)
                            .build())
                    .block();

        Assert.notNull(response, "expected not null user login response");
        Assert.hasText(response.getToken(), "expected not empty value");
        Assert.hasText(response.getId(), "expected not empty value");

        System.out.printf("output: %s", response);

    }
}
