package lambda.service;

import request.SignUpRequest;
import response.LoginResponse;

public class SignUpImpl implements SignUpService {
  @Override
  public LoginResponse signUpUser(SignUpRequest signUpRequest) {
    return new LoginResponse(true, "test lambda");
  }
}
