package quarkus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/accounts")
public class AccountResource {
    private final Set<Account> accounts = new HashSet<>(
            Arrays.asList(new Account(123456789L, 987654321L, "George Baird", new BigDecimal("354.23")),
                    new Account(121212121L, 888777666L, "Mary Taylor", new BigDecimal("560.03")),
                    new Account(545454545L, 222444999L, "Diana Rigg", new BigDecimal("422.00"))));

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Account> allAccounts() {
        return accounts;
    }

    @GET
    @Path("/{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Account getAccount(@PathParam("accountNumber") Long accountNumber) {
        Optional<Account> response = accounts.stream()
                .filter(account -> account.getAccountNumber().equals(accountNumber)).findFirst();

        return response.orElseThrow(
                () -> new WebApplicationException("Account with ID of: " + accountNumber + " does not exist.", 404));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(Account account) {
        if (account.getAccountNumber() == null) {
            throw new WebApplicationException("No account number specified.", 400);
        }

        accounts.add(account);

        return Response.status(201).entity(account).build();
    }
}
