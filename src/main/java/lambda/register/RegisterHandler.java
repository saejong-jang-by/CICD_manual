package lambda.register;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import service.SignUpImpl;
import serviceinterfaces.SignUpService;
import request.SignUpRequest;
import response.LoginResponse;

// aws request handler
public class RegisterHandler implements RequestHandler<SignUpRequest, LoginResponse> {

    @Override
    public LoginResponse handleRequest(SignUpRequest signUpRequest, Context context) {
        SignUpService signUpService = new SignUpImpl();

        LoginResponse loginResponse = signUpService.signUpUser(signUpRequest);
        if(loginResponse.isSuccess()){
            return loginResponse;
        }

        return new LoginResponse(false, loginResponse.getMessage());
    }
}
