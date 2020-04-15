package user;

import messaging.requests.AuthenticateUserRequest;
import messaging.requests.RegisterUserRequest;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("user")
public class UserService {

    @EJB
    private UserBean userBean;

    @POST
    @Path("authenticate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String onAuthenticateRequest(AuthenticateUserRequest request) {
        return userBean.onAuthenticateUser(request);
    }

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String onRegister(RegisterUserRequest registerUserRequest) {
        return userBean.onRegisterUser(registerUserRequest);
    }
}
