package util;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by zhenghua_miso on 19/05/2017.
 * <p>Title: 线程池工具</p>
 * <p>Description: 负责根据线程池配置生成线程池</p>
 * <p>Copyright: Copyright (c) 2017.5.19</p>
 * <p>Company: SI-TECH</p>
 *
 */

public class ThreadPoolUtil {
    static Logger logger = LoggerFactory.getLogger(ThreadPoolUtil.class);

    private ConcurrentMap<String, ThreadPoolExecutor> threadPools = new ConcurrentHashMap<String, ThreadPoolExecutor>(4);
    private static ThreadPoolUtil instance = new ThreadPoolUtil();

    private ThreadPoolUtil() {

    }

    /**
     * 获取实例
     *
     * @return
     */
    public static ThreadPoolUtil getInstance() {
        return ThreadPoolUtil.instance;
    }

    /**
     * 获取指定类型的线程池
     *
     * @param type
     * @return
     */
    public static ThreadPoolExecutor getThreadPool(String type) {
        ThreadPoolExecutor pool = instance.threadPools.get(type);
        if (pool == null) {
            ThreadPoolConfigDetails threadPoolConfigDetails = ThreadPoolConfig.getInstance().getThreadPoolConfigDetailsByType(type);
            if (threadPoolConfigDetails == null) {
                logger.error("Attempt to create a thread pool for type {}, but not configured!", type);
                return null;
            }

            pool = instance.createThreadPool(threadPoolConfigDetails);
            ThreadPoolExecutor exPool = instance.threadPools.putIfAbsent(type, pool);
            if (exPool != null) {
                return exPool;
            }
        }

        return pool;
    }

    /**
     * 创建线程池
     *
     * @return
     */
    public ThreadPoolExecutor createThreadPool(ThreadPoolConfigDetails threadPoolConfigDetails) {
        ThreadPoolExecutor threadPool = null;
        try {
            // 取得参数
            int corePoolSize = threadPoolConfigDetails.getCorePoolSize();
            int maximumPoolSize = threadPoolConfigDetails.getMaximumPoolSize();
            long keepAliveTime = threadPoolConfigDetails.getKeepAliveTime();
            TimeUnit unit = ThreadPoolConfig.getTimeUnit(threadPoolConfigDetails.getUnit());
            int workQueueSize = threadPoolConfigDetails.getWorkQueueSize();
            RejectedExecutionHandler handler = ThreadPoolConfig.getRejectExecuteHandler(threadPoolConfigDetails.getHandler());

            // 创建线程池
            threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit,
                    new ArrayBlockingQueue<Runnable>(workQueueSize), handler);

        } catch (Exception e) {
            logger.error("createThreadPool error:", e);
            e.printStackTrace();
            return null;
        }

        return threadPool;
    }

}
