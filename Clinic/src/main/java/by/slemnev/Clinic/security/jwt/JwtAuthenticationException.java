package by.slemnev.Clinic.security.jwt;

import javax.naming.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {
//    public JwtAuthenticationException(String msg, Throwable t) {
//        super(msg, t);
//    }

    public JwtAuthenticationException(String msg) {
        super(msg);
    }
}
