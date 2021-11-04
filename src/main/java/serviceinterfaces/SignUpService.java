package serviceinterfaces;


import request.SignUpRequest;
import response.LoginResponse;

public interface SignUpService {
  LoginResponse signUpUser(SignUpRequest signUpRequest);
}
