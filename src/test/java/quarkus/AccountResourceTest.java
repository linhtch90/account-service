package quarkus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;

@QuarkusTest
public class AccountResourceTest {
    @Test
    void testRetrieveAll() {
        Response result = given().when().get("/accounts").then().statusCode(200)
                .body(containsString("George Baird"), containsString("Mary Taylor"), containsString("Diana Rigg"))
                .extract().response();

        List<Account> accounts = result.jsonPath().getList("$");
        assertFalse(accounts.isEmpty());
        assertTrue(accounts.size() == 3);
    }

    @Test
    void testGetAccount() {
        Account account = given().when().get("/accounts/{accountNumber}", 545454545).then().statusCode(200).extract()
                .as(Account.class);

        assertTrue(account.getAccountNumber().equals(545454545L));
        assertTrue(account.getCustomerName().equals("Diana Rigg"));
        assertTrue(account.getBalance().equals(new BigDecimal("422.00")));
        assertTrue(account.getAccountStatus().equals(AccountStatus.OPEN));
    }

}
