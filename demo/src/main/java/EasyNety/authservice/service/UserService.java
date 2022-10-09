package EasyNety.authservice.service;

import EasyNety.authservice.exception.AppException;

public interface UserService {


    void sendverifyMail(String userName) throws AppException;
}
