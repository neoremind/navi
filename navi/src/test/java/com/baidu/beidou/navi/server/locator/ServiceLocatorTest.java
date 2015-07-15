package com.baidu.beidou.navi.server.locator;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import org.junit.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.baidu.beidou.navi.it.service.BookService;
import com.baidu.beidou.navi.it.service.impl.BookServiceImpl;
import com.baidu.beidou.navi.it.service.impl.CompanyServiceImpl;
import com.baidu.beidou.navi.server.NaviRpcExporter;
import com.baidu.beidou.navi.server.locator.impl.MethodSignatureKeyServiceLocator;
import com.baidu.beidou.navi.util.MethodUtil;
import com.baidu.beidou.navi.util.MethodWrapper;
import com.baidu.beidou.navi.util.ReflectionUtil;
import com.baidu.beidou.navi.vo.Book;
import com.baidu.beidou.navi.vo.Company;

@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class ServiceLocatorTest extends AbstractJUnit4SpringContextTests {

    private ServiceLocator<String, NaviRpcExporter> locator = new MethodSignatureKeyServiceLocator();

    @Test
    public void testRegisterService() {
        Map<String, NaviRpcExporter> map = (Map<String, NaviRpcExporter>) applicationContext
                .getBeansOfType(NaviRpcExporter.class);
        Collection<NaviRpcExporter> values = map.values();
        for (NaviRpcExporter service : values) {
            locator.regiserService(service);
        }

        boolean foundFlag = false;
        Method[] ms = ReflectionUtil.getAllInstanceMethods(BookServiceImpl.class);
        for (Method m : ms) {
            MethodWrapper w = new MethodWrapper(BookService.class.getName(), m.getName(),
                    MethodUtil.getArgsTypeName(m));
            MethodDescriptor<String> desc = locator.getServiceDescriptor(w.toString());
            System.out.println(desc);
            if (desc.getServiceId().equals("com.baidu.beidou.navi.it.service.BookService-get-int")) {
                foundFlag = true;
                assertThat(desc.getMethod().getName(), is(m.getName()));
                assertThat(AopUtils.getTargetClass(desc.getTarget()).getName(),
                        is(BookServiceImpl.class.getName()));
                assertThat(desc.getArgumentClass()[0], is(int.class.getClass()));
                assertThat(desc.getReturnClass(), is(Book.class.getClass()));
            }
            if (desc.getServiceId().equals(
                    "com.baidu.beidou.navi.it.service.CompanyService-get-int")) {
                foundFlag = true;
                assertThat(desc.getMethod().getName(), is(m.getName()));
                assertThat(desc.getTarget().getClass().getName(),
                        is(CompanyServiceImpl.class.getName()));
                assertThat(desc.getArgumentClass()[0], is(int.class.getClass()));
                assertThat(desc.getReturnClass(), is(Company.class.getClass()));
            }
        }

        if (!foundFlag) {
            fail("should not enter here!");
        }
    }
}
