package util;

/**
 * Created by zhenghua_miso on 19/05/2017.
 */

import java.io.InputStream;
import java.util.HashMap;

import org.yaml.snakeyaml.Yaml;

public class SysConfigManager {

    public final static String CONFIG_FILE_NAME_TEST = "sysConfTest.yaml";
    public final static String CONFIG_FILE_NAME = "sysConf.yaml";

    public static SysConfigManager instance;
    private static byte[] LOCK = {};

    private HashMap<String, Object> sysConfigMap;

    public SysConfigManager(HashMap<String, Object> sysConfigMap) {
        this.sysConfigMap = sysConfigMap;
    }

    public HashMap<String, Object> getSysConfigMap() {
        return sysConfigMap;
    }

    public static SysConfigManager getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    InputStream is = null;
                    is = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE_NAME_TEST);

                    if (is == null) {
                        is = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
                    }

                    if (is == null) {
                        throw new RuntimeException("系统配置未找到!");
                    }
                    HashMap<String, Object> sysConfigMap = new HashMap<String, Object>();
                    try {
                        Yaml yaml = new Yaml();
                        sysConfigMap = (HashMap<String, Object>) yaml.load(is);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("系统配置读取有误!");
                    }
                    if (!sysConfigMap.isEmpty()) {
                        instance = new SysConfigManager(sysConfigMap);
                    }
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        System.out.println(SysConfigManager.getInstance().getSysConfigMap());
    }
}

