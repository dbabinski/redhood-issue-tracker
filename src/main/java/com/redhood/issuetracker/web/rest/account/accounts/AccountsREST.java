package com.redhood.issuetracker.web.rest.account.accounts;

import com.redhood.issuetracker.repository.account.accounts.entity.Accounts;
import com.redhood.issuetracker.service.account.accounts.AccountsService;
import com.redhood.issuetracker.web.utils.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@RequestMapping(path = "/api/admin", produces="application/json")
public class AccountsREST {
    //------------------------------------------------------------------------------------------------------------------
    // Fields
    //------------------------------------------------------------------------------------------------------------------
    private final Logger log = LoggerFactory.getLogger(AccountsREST.class);
    private AccountsService accountsService;
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------------------------------
    @Autowired
    public AccountsREST(AccountsService accountsService) {
        this.accountsService = accountsService;
    }
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // REST Controller - GET
    //------------------------------------------------------------------------------------------------------------------
    /**
     * {@code GET /api/admin/account/accounts} : get all users with all the details - calling this are only allowed for the administrators.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body containing data (all users), message and transaction status.
     */
    @GetMapping("/account/accounts")
    public ResponseEntity getAllAccounts() {
        final List<Accounts> accounts = accountsService.findAll();
        if (accounts.equals(null) || accounts == null) {
            return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND);
        } else {
            return ResponseHandler.generateResponse(HttpStatus.OK, accounts);
        }
    }

    /**
     * {@code GET /api/admin/account/accounts/{login}} : get user with specific "login".
     * @param login the login of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200(OK)} and with body containing data (user), message and transaction status.
     */
    @GetMapping("/account/accounts/{login}")
    public ResponseEntity getAccount(@PathVariable @Pattern(regexp = Accounts.LOGIN_REGEX) String login) {
        final Accounts account = accountsService.findByLogin(login);
        if (account.equals(null) || account == null) {
            return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND);
        }
        return ResponseHandler.generateResponse(HttpStatus.OK, account);
    }
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // REST Controller - POST
    //------------------------------------------------------------------------------------------------------------------
    /**
     * {@code POST /api/admin/account/accounts} : Creates new user.
     * <p>
     *
     * @return
     */
    /*
    @PostMapping("/account/accounts")
    public ResponseEntity getKonta(@Valid @RequestBody AccountsDTO userDTO) throws URISyntaxException {
        try {
            List<Accounts> list = accountsService.findAll()
                    .stream()
                    .collect(Collectors.toCollection(LinkedList::new));
            if (list != null) {

                odpowiedz.setDane(new JSONObject().put(
                        "accounts", new JSONArray().toList().add(list)
                ));
            }
        } catch (Exception ex) {
            odpowiedz.setBlad(true).setKomunikat(ex.getMessage());
        }
        return ResponseHandler.generateResponse();


        log.debug("REST request to save User : {}", userDTO);

        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            User newUser = userService.createUser(userDTO);
            mailService.sendCreationEmail(newUser);
            return ResponseEntity
                    .created(new URI("/api/admin/users/" + newUser.getLogin()))
                    .headers(
                            HeaderUtil.createAlert(applicationName, "A user is created with identifier " + newUser.getLogin(), newUser.getLogin())
                    )
                    .body(newUser);
        }


    }

     */

    /*
    @PostMapping("/account/accounts/")
    public Accounts addAccount(@RequestBody Accounts accounts) {
        accounts.setId(0);
        accountsService.save(accounts);
        return accounts;
    }

    */
    //------------------------------------------------------------------------------------------------------------------
    // REST Controller - PUT
    //------------------------------------------------------------------------------------------------------------------
    /*
    @PutMapping("/account/accounts/")
    public Accounts updateAccount(@RequestBody Accounts accounts) {
        accountsService.save(accounts);
        return accounts;
    }
    */
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // REST Controller - DELETE
    //------------------------------------------------------------------------------------------------------------------
    /*
    @DeleteMapping("/account/accounts/{accountId}")
    public String deleteAccount(@PathVariable int accountId) {
        Accounts account = accountsService.findById(accountId);
        if (account == null) {
            //throw new ErrorException("Account id: " + accountId + ", not found!");
            return null;
        } else {
            accountsService.deleteById(accountId);
            return "Deleted customer id: " + accountId;
        }
    }
    */
    //------------------------------------------------------------------------------------------------------------------
}
