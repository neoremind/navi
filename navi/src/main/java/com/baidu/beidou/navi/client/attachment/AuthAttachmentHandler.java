package com.baidu.beidou.navi.client.attachment;

import com.baidu.beidou.navi.server.vo.RequestDTO;
import com.baidu.beidou.navi.util.StringUtil;

/**
 * ClassName: AuthAttachmentHandler <br/>
 * Function: 带有权限处理的handler
 * 
 * @author Zhang Xu
 */
public class AuthAttachmentHandler implements AttachmentHandler {

    /**
     * 装饰作用，指代其他的handler
     */
    private AttachmentHandler handler;

    /**
     * Creates a new instance of AuthAttachmentHandler.
     */
    public AuthAttachmentHandler() {
        super();
    }

    /**
     * Creates a new instance of AuthAttachmentHandler.
     * 
     * @param handler
     *            带装饰的handler
     */
    public AuthAttachmentHandler(AttachmentHandler handler) {
        super();
        this.handler = handler;
    }

    /**
     * 处理附加信息
     * 
     * @param requestDTO
     *            请求DTO
     * @param attachment
     *            附加信息
     * @see com.baidu.beidou.navi.client.attachment.AttachmentHandler#handle(com.baidu.beidou.navi.server.vo.RequestDTO,
     *      com.baidu.beidou.navi.client.attachment.Attachment)
     */
    @Override
    public void handle(RequestDTO requestDTO, Attachment attachment) {
        if (handler != null) {
            handler.handle(requestDTO, attachment);
        }
        if (attachment != null && attachment.getAppIdToken() != null
                && StringUtil.isNotEmpty(attachment.getAppIdToken().getAppId())
                && StringUtil.isNotEmpty(attachment.getAppIdToken().getToken())) {
            requestDTO.setAppId(attachment.getAppIdToken().getAppId());
            requestDTO.setToken(attachment.getAppIdToken().getToken());
        }
    }

    public AttachmentHandler getHandler() {
        return handler;
    }

    public void setHandler(AttachmentHandler handler) {
        this.handler = handler;
    }
}
