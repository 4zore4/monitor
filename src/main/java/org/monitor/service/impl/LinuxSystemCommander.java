package org.monitor.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import org.monitor.domain.Cpu;
import org.monitor.domain.Memory;
import org.monitor.service.AbstractSystemCommander;
import org.monitor.util.CommandUtil;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LinuxSystemCommander extends AbstractSystemCommander  {

    @Override
    public JSONObject getAllMonitor() {
        String result = CommandUtil.execSystemCommand("top -i -b -n 1");
        if (StrUtil.isEmpty(result)){
            return null;
        }
        String[] split = result.split(StrUtil.LF);
        int length = split.length;
        JSONObject jsonObject = new JSONObject();

        if (length >= 2){
            String cpus = split[2];
            Cpu cpu = getLinuxCpu(cpus);
            jsonObject.put("cpu",cpu);
        }
        if (length >= 3){
            String mem = split[3];
            Memory memory = getLinuxMemory(mem);
            jsonObject.put("memory",memory);
        }
        jsonObject.put("disk",getHardDisk());

        return jsonObject;
    }

    @Override
    public List<Object> getProcessList(String processName) {
        return null;
    }

    @Override
    public String emptyLogFile(File file) {
        return null;
    }


//    获取cpu信息
    public static Cpu getLinuxCpu(String info){
        Cpu cpu = new Cpu();
        if (StrUtil.isEmpty(info)){
            return null;
        }
        HashMap<String,Double> cpuData = getInfoData(info);
//        "%Cpu(s):  3.1 us,  3.1 sy,  0.0 ni, 93.8 id,  0.0 wa,  0.0 hi,  0.0 si,  0.0 st\n" +

        cpu.setUser(cpuData.get("us"));
        cpu.setSys(cpuData.get("sy"));
        cpu.setIdle(cpuData.get("id"));
        cpu.setTotal(100.0-cpuData.get("id"));


        return cpu;
    }


    public static Memory getLinuxMemory(String info) {
        Memory memory = new Memory();
        if (StrUtil.isEmpty(info)) {
            return null;
        }
//        "MiB Mem :   3935.6 total,    607.7 free,    599.8 used,   2728.1 buff/cache\n"
        int i = info.indexOf(CharPool.COLON);
        String[] split = info.substring(i + 1).split(StrUtil.COMMA);

        double total = 0, free = 0, used = 0;
        for (String str : split) {
            str = str.trim();
            if (str.endsWith("free")) {
                // 减去了 buff
                String value = str.replace("free", "").replace("k", "").trim();
                free = Convert.toDouble(value, 0.0);
                memory.setUnused(free);
            }
            if (str.endsWith("total")) {
                String value = str.replace("total", "").replace("k", "").trim();
                total = Convert.toDouble(value, 0.0);
                memory.setTotal(total);
            }
            if (str.endsWith("used")) {
                // 计算出时间使用
                String value = str.replace("used", "").replace("k", "").trim();
                used = Convert.toDouble(value, 0.0);
                memory.setUsed(used);
            }
        }
        return memory;
    }


    private static HashMap<String,Double> getInfoData(String info){
        if (StrUtil.isEmpty(info)){
            return null;
        }
        int i = info.indexOf(CharPool.COLON);
        String[] split = info.substring(i + 1).split(StrUtil.COMMA);
//        %Cpu(s):  3.1 us,  3.1 sy,  0.0 ni, 93.8 id,  0.0 wa,  0.0 hi,  0.0 si,  0.0 st
        HashMap<String,Double> data = new HashMap<>();

        for (String str : split) {
            str = str.trim();

            String value = str.substring(0,str.length() - 2).trim();
            String tag = str.substring(str.length() -2);
            value = value.replace("%","");
            data.put(tag, Convert.toDouble(value,0.0));

        }
        return data;
    }

}
