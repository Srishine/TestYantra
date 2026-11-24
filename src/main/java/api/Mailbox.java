
package api;

public class Mailbox {

    private String email;
    private String password;
    private String token;
    private String accountId;

 /*
This class stores the email account details returned by the API.
Responsible for storing generated data.
- Email address
- Password
- Authentication token
- Account ID */

    public Mailbox(String email, String password,
                   String token, String accountId) {
        this.email = email;
        this.password = password;
        this.token = token;
        this.accountId = accountId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public String getAccountId() {
        return accountId;
    }
}
