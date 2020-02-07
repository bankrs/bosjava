package com.bankrs.bosjava;

import com.bankrs.bosjava.model.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class UserClientTest {

    private static UserClient userClient;

    @BeforeClass
    public static void init() {
        UserLoginParams params =
                UserLoginParams.builder()
                        .username(ClientTest.testUsername)
                        .password(ClientTest.testPassword)
                        .build();

        AppClient appClient = Client.newClient(ClientTest.staginBankrsUrl, ClientTest.userAgent)
                .newAppClient(ClientTest.testApplicationKey);

        UserLoginResponse response =
                appClient.loginUser(params).block();

        userClient = appClient.newUserClient(response.getToken());
    }


    @Test
    public void testAccesses() {
        BankAccess accesses = userClient
                .accessService()
                .getAccess(2)
                .block();

        System.out.printf("user accesses: %s", accesses);
    }

    @Test
    public void testListTransactions() {
        final long accessId = 2;
        final int limit = 10;
        TransactionPage firstPage = userClient.transactionService()
                .list(
                        ListTransactionsReq.builder()
                                .accessId(accessId)
                                .limit(0)
                                .offset(0)
                                .build()
                )
                .block();
        int pages = (int) (Math.floor(firstPage.getTotal() / limit) +
                (Math.ceil(firstPage.getTotal() % limit) > 0 ? 1 : 0));

        List<TransactionPage.Transaction> trxs = IntStream.rangeClosed(1, pages)
                .mapToObj(i -> {
                            Assert.assertTrue((i * limit) < firstPage.getTotal() + limit);
                            return Arrays.stream(userClient.transactionService()
                                    .list(ListTransactionsReq.builder()
                                            .accessId(accessId)
                                            .limit(limit)
                                            .offset((i - 1) * limit)
                                            .build()
                                    )
                                    .block()
                                    .getData());
                        }
                )
                .flatMap(Function.identity())
                .collect(Collectors.toList());

        Assert.assertTrue("expected transactions should equal retrieved transactions",
                firstPage.getTotal() == trxs.size());


    }

    @Test
    public void testListRepeatedTransactions() {
        final long accessId = 2;
        RepeatedTransactionPage repTrxs = userClient.repeatedTransactionsService()
                .list(ListTransactionsReq.builder()
                        .accessId(accessId)
                        .build())
                .block();

        Assert.assertTrue("expected 1 repeated transactions", repTrxs.getTotal() == 1);
    }

    @Test
    public void testListScheduledTransactions() {
        final long accessId = 2;
        TransactionPage.Transaction[] schTrxs = userClient.scheduledTransactionsService()
                .list(ListScheduledTransactionsReq.builder()
                        .accessId(accessId)
                        .build())
                .block();

        Assert.assertTrue("expected 0 scheduled transactions", schTrxs.length == 0);
    }


}
