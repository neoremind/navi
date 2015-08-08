package com.baidu.beidou.navimgr.zoo.controller;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.beidou.navimgr.auth.constant.UserWebConstant;
import com.baidu.beidou.navimgr.auth.vo.User;
import com.baidu.beidou.navimgr.common.constant.GlobalResponseStatusMsg;
import com.baidu.beidou.navimgr.common.constant.WebConstant;
import com.baidu.beidou.navimgr.common.controller.BaseController;
import com.baidu.beidou.navimgr.common.vo.JsonObject;
import com.baidu.beidou.navimgr.util.ThreadContext;
import com.baidu.beidou.navimgr.zoo.ZooNode;
import com.baidu.beidou.navimgr.zoo.constant.ZooConstant.ZooEnv;
import com.baidu.beidou.navimgr.zoo.service.ZooService;

/**
 * ClassName: ZooController <br/>
 * Function: zookeeper服务节点controller
 *
 * @author Zhang Xu
 */
@Controller
@RequestMapping(WebConstant.ZOO_URL_PATH)
public class ZooController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(ZooController.class);

    /**
     * 线下zookeepr
     */
    @Resource
    private ZooService offlineZooServiceImpl;

    /**
     * 线上zookeeper
     */
    @Resource
    private ZooService onlineZooServiceImpl;

    /**
     * 获取zookeeper服务节点树
     *
     * @param env 环境
     *
     * @return JsonObject
     */
    @RequestMapping(value = "/getZooTree", method = RequestMethod.GET)
    @ResponseBody
    public JsonObject<?> getZooTree(@RequestParam(value = "env", required = true) String env) {
        LOG.info("Get" + env + " zoo tree");
        JsonObject<?> result = JsonObject.create();
        try {
            ZooNode zooNode = null;
            if (ZooEnv.ONLINE.getValue().equalsIgnoreCase(env)) {
                User sessionVisitor = (User) ThreadContext.getSessionVisitor();
                if (sessionVisitor != null
                        && !sessionVisitor.getAuths().contains(
                        UserWebConstant.UserAuth.ZOO_ONLINE.getValue())) {
                    return sysError(GlobalResponseStatusMsg.AUTH_DENIED.getMessage().getMessage());
                }
                zooNode = onlineZooServiceImpl.getNodeRecrusively();
            } else {
                zooNode = offlineZooServiceImpl.getNodeRecrusively();
            }
            if (CollectionUtils.isEmpty(zooNode.getChildren())) {
                return sysError(GlobalResponseStatusMsg.GET_ZOO_TREE_FAILED.getMessage("找不到任何节点")
                        .getMessage());
            }
            result.addData("zooNodes", new ZooNode[] {zooNode});
        } catch (Exception e) {
            LOG.error("Query zoo tree failed!", e);
            return sysError();
        }
        return result;
    }

}
