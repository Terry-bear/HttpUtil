package util;

/**
 * Created by zhenghua_miso on 19/05/2017.
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 线程池配置管理
 *
 * @author Charlie
 */
public class ThreadPoolConfig {
    static Logger logger = LoggerFactory.getLogger(ThreadPoolConfig.class);

    private static ThreadPoolConfig instance = new ThreadPoolConfig();
    private static Map<String, ThreadPoolConfigDetails> threadPoolConfigs = new HashMap<String, ThreadPoolConfigDetails>();

    protected ThreadPoolConfig() {
    }

    public static ThreadPoolConfig getInstance() {
        List<ThreadPoolConfigDetails> threadConfigList = (List<ThreadPoolConfigDetails>) SysConfigManager.getInstance().getSysConfigMap().get("threadPool");
        for (ThreadPoolConfigDetails threadPoolConfigDetails : threadConfigList) {
            threadPoolConfigs.put(threadPoolConfigDetails.getType(), threadPoolConfigDetails);
        }
        return instance;
    }

    /**
     * 根据线程池类型获取线程池配置
     *
     * @param type
     * @return
     */
    public ThreadPoolConfigDetails getThreadPoolConfigDetailsByType(String type) {
        return threadPoolConfigs.get(type);
    }

    public static RejectedExecutionHandler getRejectExecuteHandler(String handlerName) {
        if ("AbortPolicy".equals(handlerName)) {
            // 抛出java.util.concurrent.RejectedExecutionException异常
            return new ThreadPoolExecutor.AbortPolicy();
        } else if ("CallerRunsPolicy".equals(handlerName)) {
            // 重试添加当前的任务，他会自动重复调用execute()方法
            return new ThreadPoolExecutor.CallerRunsPolicy();
        } else if ("DiscardOldestPolicy".equals(handlerName)) {
            // 抛弃旧的任务
            return new ThreadPoolExecutor.DiscardOldestPolicy();
        } else if ("DiscardPolicy".equals(handlerName)) {
            // 抛弃当前的任务
            return new ThreadPoolExecutor.DiscardPolicy();
        } else {
            return null;
        }
    }

    public static TimeUnit getTimeUnit(String unit) {
        if ("DAYS".equals(unit)) {
            return TimeUnit.DAYS;
        } else if ("HOURS".equals(unit)) {
            return TimeUnit.HOURS;
        } else if ("MICROSECONDS".equals(unit)) {
            return TimeUnit.MICROSECONDS;
        } else if ("MILLISECONDS".equals(unit)) {
            return TimeUnit.MILLISECONDS;
        } else if ("MINUTES".equals(unit)) {
            return TimeUnit.MINUTES;
        } else if ("NANOSECONDS".equals(unit)) {
            return TimeUnit.NANOSECONDS;
        } else if ("SECONDS".equals(unit)) {
            return TimeUnit.SECONDS;
        } else {
            return null;
        }
    }

}

