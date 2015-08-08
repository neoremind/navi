package com.baidu.beidou.navimgr.page.controller;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.beidou.navimgr.common.constant.GlobalResponseStatusMsg;
import com.baidu.beidou.navimgr.common.constant.WebConstant;
import com.baidu.beidou.navimgr.common.controller.BaseController;
import com.baidu.beidou.navimgr.common.vo.JsonObject;
import com.baidu.beidou.navimgr.page.PageFetcher;

/**
 * ClassName: PageController <br/>
 * Function: 抓取页面
 * 
 * @author Zhang Xu
 */
@Controller
@RequestMapping(WebConstant.PAGE_URL_PATH)
public class PageController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(PageFetcher.class);

    /**
     * 页面抓取器
     */
    @Resource
    private PageFetcher pageFetcher;

    /**
     * 根据url获取页面html
     * 
     * @param url
     *            url
     * @return JsonObject
     */
    @RequestMapping(value = "/getPageHtml", method = RequestMethod.GET)
    @ResponseBody
    public JsonObject<?> getPageHtml(@RequestParam(value = "url", required = true) String url) {
        JsonObject<?> result = JsonObject.create();
        LOG.info("Start to get page html of url=" + url);
        try {
            result.addData("html", pageFetcher.getPageHtml(url));
        } catch (RuntimeException e) {
            LOG.error("Failed to get page html of url=" + url + " due to " + e.getMessage());
            return sysError(GlobalResponseStatusMsg.GET_HTML_PAGE_FAILED.getMessage(url)
                    .getMessage());
        }
        return result;
    }

    /**
     * 看是否页面可抓取
     * 
     * @param url
     *            url
     * @return JsonObject
     */
    @RequestMapping(value = "/isPageHtmlAvailable", method = RequestMethod.GET)
    @ResponseBody
    public JsonObject<?> isPageHtmlAvailable(
            @RequestParam(value = "url", required = true) String url) {
        JsonObject<?> result = JsonObject.create();
        LOG.info("Start to get page html of url=" + url);
        try {
            result.addData("isAvailable", StringUtils.isNotEmpty(pageFetcher.getPageHtml(url)));
        } catch (RuntimeException e) {
            LOG.error("Failed to get page html of url=" + url + " due to " + e.getMessage(), e);
            return sysError(GlobalResponseStatusMsg.GET_HTML_PAGE_FAILED.getMessage(url)
                    .getMessage());
        }
        return result;
    }

}
