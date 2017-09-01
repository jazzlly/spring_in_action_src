package spittr.jdbc;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Logger for all restful api
 */
// @Aspect
// @Component
public class RestfulLogService {
    protected transient Log logger = LogFactory.getLog(getClass());

    @Pointcut("execution(* org.springframework.jdbc.datasource.DataSourceTransactionManager.*(..))")
    public void restfulLog() {}

    @Before("restfulLog()")
    public void doBefore(JoinPoint jp) throws Throwable {
        logger.info("restful api invoked: " + jp.toLongString());
        if (jp.getArgs() != null) {
            logger.info("args: " + Arrays.toString(jp.getArgs()));
        }
    }
}

