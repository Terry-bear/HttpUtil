package notice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ThreadPoolUtil;

import java.util.concurrent.ExecutorService;

/**
 * Created by zhenghua_miso on 19/05/2017.
 * 红包通知接口
 */
public class NotifyManager {
    static Logger logger = LoggerFactory.getLogger(NotifyManager.class);

    private static NotifyManager instance = new NotifyManager();
    private ExecutorService threadPool = ThreadPoolUtil.getInstance().getThreadPool("notify");

    private NotifyManager(){}

    public static NotifyManager getInstance(){
        return instance;
    }
}
