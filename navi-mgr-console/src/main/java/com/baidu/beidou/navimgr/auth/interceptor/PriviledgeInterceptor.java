package com.baidu.beidou.navimgr.auth.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baidu.beidou.navimgr.auth.constant.UserWebConstant;
import com.baidu.beidou.navimgr.common.interceptor.AbstractExcludePatternsInterceptor;

/**
 * ClassName: PriviledgeInterceptor <br/>
 * Function: 访问权限验证拦截器
 * 
 * @author Zhang Xu
 */
public class PriviledgeInterceptor extends AbstractExcludePatternsInterceptor {

    // private static final Logger LOG = LoggerFactory.getLogger(PriviledgeInterceptor.class);

    /**
     * 不用json返回的請求PATH
     */
    private List<String> notJsonPathList;

    /**
     * Creates a new instance of PriviledgeInterceptor.
     * 
     * @param excludePatterns
     */
    public PriviledgeInterceptor(String[] excludePatterns) {
        super(excludePatterns);
    }

    /**
     * @see com.baidu.beidou.navimgr.common.interceptor.AbstractExcludePatternsInterceptor#doPreHandle(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean doPreHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        if (!UserWebConstant.ENABLE_AUTH) {
            return true;
        }

        // Visitor visitor = ThreadContext.getSessionVisitor();
        // Integer userId = ThreadContext.getContext(UserWebConstant.CTX_USERID);
        // Preconditions.checkNotNull(visitor, "Visitor should NOT be null in ThreadContext!");
        // Preconditions.checkNotNull(userId, "UserId should NOT be null in ThreadContext!");
        //
        // /**
        // * Note:当操作者和被操作用户不是一个人的时候，不验证权限，会通过 <br/>
        // * {@link UserManageInterceptor}进行验证。
        // */
        // if (visitor.getUserid() != userId) {
        // return true;
        // }
        //
        // Set<String> authSet = visitor.getAuths();
        // if (CollectionUtils.isEmpty(authSet)) {
        // LOG.error("Visitor does NOT get any auths, userid=" + visitor.getUserid());
        // returnJsonSystemError(request, response, GlobalResponseStatusMsg.AUTH_DENIED);
        // return false;
        // }
        //
        // HandlerMethod handlerMethod = (HandlerMethod) handler;
        // Privilege privilege = handlerMethod.getMethodAnnotation(Privilege.class);
        // if (privilege != null) {
        // if (authSet.contains(privilege.value())) {
        // return true;
        // }
        // LOG.error("Visitor does NOT have auth={} on controller={}, userid={}", new Object[] { privilege.value(),
        // getBeanTypeAndMethodName(handlerMethod), visitor.getUserid() });
        // returnJsonSystemError(request, response, GlobalResponseStatusMsg.AUTH_DENIED);
        // return false;
        // }
        //
        // MultiPrivilege multiPri = handlerMethod.getMethodAnnotation(MultiPrivilege.class);
        // if (multiPri != null) {
        // for (String val : multiPri.value()) {
        // if (authSet.contains(val)) {
        // return true;
        // }
        // }
        // LOG.error(
        // "Visitor does NOT have auth={} on controller={}, userid={}",
        // new Object[] { Arrays.toString(multiPri.value()), getBeanTypeAndMethodName(handlerMethod),
        // visitor.getUserid() });
        // returnJsonSystemError(request, response, GlobalResponseStatusMsg.AUTH_DENIED);
        // return false;
        // }
        //
        // privilege = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Privilege.class);
        // if (privilege != null) {
        // if (authSet.contains(privilege.value())) {
        // return true;
        // }
        // LOG.error("Visitor does NOT have auth={} on controller={}, userid={}", new Object[] { privilege.value(),
        // getBeanTypeAndMethodName(handlerMethod), visitor.getUserid() });
        // returnJsonSystemError(request, response, GlobalResponseStatusMsg.AUTH_DENIED);
        // return false;
        // }
        //
        // multiPri = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), MultiPrivilege.class);
        // if (multiPri != null) {
        // for (String val : multiPri.value()) {
        // if (authSet.contains(val)) {
        // return true;
        // }
        // }
        // LOG.error(
        // "Visitor does NOT have auth={} on controller={}, userid={}",
        // new Object[] { Arrays.toString(multiPri.value()), getBeanTypeAndMethodName(handlerMethod),
        // visitor.getUserid() });
        // returnJsonSystemError(request, response, GlobalResponseStatusMsg.AUTH_DENIED);
        // return false;
        // }

        return true;
    }

    // /**
    // * 获取controller类名以及方法名
    // *
    // * @param handlerMethod
    // * @return
    // */
    // private String getBeanTypeAndMethodName(HandlerMethod handlerMethod) {
    // StringBuilder result = new StringBuilder();
    // result.append(handlerMethod.getBeanType().getName());
    // result.append("#");
    // result.append(handlerMethod.getMethod().getName());
    // return result.toString();
    // }

    /**
     * @return the notJsonPathList
     */
    public List<String> getNotJsonPathList() {
        return notJsonPathList;
    }

    /**
     * @param notJsonPathList
     *            the notJsonPathList to set
     */
    public void setNotJsonPathList(List<String> notJsonPathList) {
        this.notJsonPathList = notJsonPathList;
    }

}
