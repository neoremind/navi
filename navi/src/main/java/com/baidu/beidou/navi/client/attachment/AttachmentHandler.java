package com.baidu.beidou.navi.client.attachment;

import com.baidu.beidou.navi.server.vo.RequestDTO;

/**
 * ClassName: AttachmentHandler <br/>
 * Function: 附加属性对于{@link RequestDTO}的装饰
 * 
 * @author Zhang Xu
 */
public interface AttachmentHandler {

    /**
     * 处理附加信息
     * 
     * @param requestDTO
     *            请求DTO
     * @param attachment
     *            附加信息
     */
    void handle(RequestDTO requestDTO, Attachment attachment);

}
