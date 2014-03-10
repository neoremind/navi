package net.neoremind.navi.server.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Navi Rpc Annotation based configuration defination
 * 
 * @author Zhang Xu
 */
@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NaviRpcService {
	
	public abstract Class<?> serviceInterface();
	
}
