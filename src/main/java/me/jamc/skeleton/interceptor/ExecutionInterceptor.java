package me.jamc.skeleton.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Jamc on 10/26/16.
 * This is used for tracking performance of each endpoint
 */
@Component
public class ExecutionInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(ExecutionInterceptor.class);
    private static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOG.info("received a request to {}", request.getRequestURI());
        request.setAttribute(START_TIME, System.currentTimeMillis());
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        long endTime = System.currentTimeMillis();
        //Make duration to -1 if for some reason there is no start time in request attribute
        Object strStartTime = request.getAttribute(START_TIME);
        long startTime =
                strStartTime == null ?  endTime + 1 : Long.parseLong(strStartTime.toString());

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String url = request.getRequestURL().toString();
        String params = request.getQueryString();
        LOG.info("Request info: url {}, uri {}, method {}, params {}, time spent {}ms",
                url, uri, method, params, endTime - startTime);

    }
}
