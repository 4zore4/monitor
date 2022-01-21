package org.monitor.service;

import com.alibaba.fastjson.JSONObject;
import com.sun.deploy.util.StringUtils;
import com.sun.deploy.util.SystemUtils;
import jdk.nashorn.internal.parser.JSONParser;
import sun.plugin2.util.SystemUtil;

import java.io.File;
import java.util.List;


public abstract class AbstractSystemCommander {

    private static AbstractSystemCommander abstractSystemCommander = null;

    public static AbstractSystemCommander getInstance(){
        /***
         * 1 = windows
         * 3 = os x
         * 2 = other
         */
        if (abstractSystemCommander != null){
            return abstractSystemCommander;
        }
        if (SystemUtil.getOSType() == 1){
//            todo new windows abstractSystemCommander
//            abstractSystemCommander = new
        }else if (SystemUtil.getOSType() == 2){
//            to do new linux abstractSystemCommander;

        }else if (SystemUtil.getOSType() == 3){
//            to do
        }else {
            throw new RuntimeException();
        }
        return abstractSystemCommander;
    }

    /**
     * 获取服务器监控信息
     * */
    public abstract JSONObject getAllMonitor();

    /**
     * 获取服务器指定进程信息
     * */
    public abstract List<Object> getProcessList(String processName);

    /**
     * file 日志 文件
     * */
    public abstract String emptyLogFile(File file);


    /**
     * 磁盘
     * */
    protected static String getHardDisk(){
        File[] files = File.listRoots();
        double totalSpace = 0;
        double useAbleSpace = 0;
        for (File file : files){
            double total = file.getTotalSpace();
            totalSpace += total;
            useAbleSpace += total - file.getUsableSpace();
        }
        return totalSpace <= 0 ? "0" : String.format("%.2f", useAbleSpace / totalSpace * 100);
    }



}
