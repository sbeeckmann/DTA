package user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import messaging.requests.AuthenticateUserRequest;
import messaging.requests.RegisterUserRequest;
import messaging.response.BasicResponse;
import user.data.User;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

@Stateless
@LocalBean
public class UserBean {

    @PersistenceContext
    private EntityManager em;

    public String onRegisterUser(RegisterUserRequest registerUserRequest) {
        ObjectMapper mapper = new ObjectMapper();
        BasicResponse response = new BasicResponse();

        // TODO improvement -> can more than user have the same name?
        // could this lead to problems?
        User user = new User();
        user.setName(registerUserRequest.getName());

        // hash password to store it safely in the db
        try {
            // Tutorial https://www.baeldung.com/java-password-hashing
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            user.setSalt(salt);

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] hashedPassword = md.digest(registerUserRequest.getPassword().getBytes(StandardCharsets.UTF_8));
            user.setPassword(new String(hashedPassword));

            this.em.persist(user);

            response.setResponseData(user);
            return mapper.writeValueAsString(response);
        } catch (NoSuchAlgorithmException | JsonProcessingException e) {
            e.printStackTrace();
            response.setError("Failed to create user");
            try {
                return mapper.writeValueAsString(response);
            } catch (JsonProcessingException jsonProcessingException) {
                jsonProcessingException.printStackTrace();
            }
        }
        return "";
    }

    public String onAuthenticateUser(AuthenticateUserRequest request) {
        ObjectMapper mapper = new ObjectMapper();
        BasicResponse response = new BasicResponse();

        // if we have more than one user with the same name this approach is not save enough
        List<User> users = this.em.createNamedQuery(User.NQ_GET_USER_BY_NAME, User.class)
                .setParameter("name", request.getName())
                .getResultList();

        try {
            if (users.isEmpty()) {
                response.setError("Failed");
                mapper.writeValueAsString(response);
            }

            User user = users.get(0);
            byte[] salt = user.getSalt();

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            String hashedPassword = new String(md.digest(request.getPassword().getBytes(StandardCharsets.UTF_8)));
            if (hashedPassword.equals(user.getPassword())) {
                response.setResponseData(user);
                return mapper.writeValueAsString(response);
            }

            response.setError("Failed");
            return mapper.writeValueAsString(response);
        } catch (JsonProcessingException | NoSuchAlgorithmException ex) {

        }
        return "";
    }

}
