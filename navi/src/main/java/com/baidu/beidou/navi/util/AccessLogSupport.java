package com.baidu.beidou.navi.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.beidou.navi.constant.NaviStatus;
import com.baidu.beidou.navi.util.vo.AccessLog;

/**
 * ClassName: AccessLogSupport <br/>
 * Function: 记录日志辅助类
 * 
 * @author Zhang Xu
 */
public class AccessLogSupport {

    private static final Logger LOG = LoggerFactory.getLogger(AccessLogSupport.class);

    /**
     * 成功码
     */
    public static final int SUCCESS = 0;

    /**
     * 失败码
     */
    public static final int FAIL = 1;

    /**
     * 异步线程
     */
    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
            .availableProcessors() * 2);

    /**
     * 异步提交访问日志
     * 
     * @param log
     * @throws Exception
     */
    public void log(AccessLog log) throws Exception {
        if (log == null) {
            return;
        }
        executorService.submit(new LogTask(log));
    }

    /**
     * ClassName: LogTask <br/>
     * Function: 日志task
     */
    private class LogTask implements Runnable {

        private AccessLog log;

        public LogTask(AccessLog log) {
            this.log = log;
        }

        /**
         * 日志格式：
         * 
         * <pre>
         * 客户端IP \t 
         * 服务名 \t 
         * 接口名 \t 
         * 序列化协议 \t 
         * Track ID \t 
         * 请求信息(仅展示部分) \t 
         * 请求信息长度(bytes) \t 
         * 请求结果状态码 \t
         * 请求结果(仅展示部分)/或者出错信息 \t 
         * 请求服务器内处理时间
         * </pre>
         */
        @Override
        public void run() {
            try {
                StringBuilder accessLog = new StringBuilder();
                accessLog.append(log.getFromIp()).append('\t').append(log.getServiceIntfName())
                        .append('\t');

                if (log.getRequest() != null) {
                    accessLog.append(log.getRequest().getMethod()).append('\t');
                } else {
                    accessLog.append('\t');
                }

                accessLog.append(log.getProtocol()).append('\t');

                if (log.getRequest() != null) {
                    accessLog.append(log.getRequest().getTraceId()).append('\t')
                            .append(AccessStringUtil.toJson(log.getRequest())).append('\t')
                            .append(log.getRequestByteSize()).append('\t');
                } else {
                    accessLog.append('\t').append('\t').append('\t');
                }

                if (log.getResponse() != null) {
                    accessLog.append(log.getResponse().getStatus()).append('\t');
                    if (log.getResponse().getStatus() == NaviStatus.RPC_OK) {
                        accessLog.append(AccessStringUtil.toJson(log.getResponse().getResult()))
                                .append('\t');
                    } else {
                        Throwable throwable = log.getResponse().getError();
                        accessLog.append(throwable.getMessage()).append('\t');
                    }
                } else {
                    accessLog.append('\t').append('\t');
                }

                accessLog.append(log.getEndTime() - log.getStartTime());
                LOG.info(accessLog.toString());
            } catch (Exception e) {
                LOG.error("Error occurred when log access", e);
            } finally {
                log = null;
            }
        }
    }

}
