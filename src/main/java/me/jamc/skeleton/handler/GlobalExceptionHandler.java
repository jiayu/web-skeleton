package me.jamc.skeleton.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.jamc.skeleton.exception.ValidationException;
import me.jamc.skeleton.security.Authorization;

/**
 * Created by Jamc on 10/26/16.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private Authorization sign;

    @ExceptionHandler(Exception.class)
    public void defaultHandler(HttpServletRequest req, Exception e) throws Exception {
        LOG.error("Error {}!!", e.getMessage());
        throw e;
    }

    @ExceptionHandler(ValidationException.class)
    public ModelAndView validationErrorHandler(HttpServletRequest request, HttpServletResponse response,
                                       ValidationException ex) throws IOException {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String params = request.getQueryString();
        String secretKey = request.getHeader("accessKey");
        String signature = request.getHeader("signature");
        long timestamp = Long.parseLong(request.getHeader("timestamp"));
        LOG.error("Validation exception expected signature is {} while given one is {}",
                signature, sign.sign(uri,method, params, secretKey, timestamp));

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        return new ModelAndView();
    }
}
