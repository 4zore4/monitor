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
import java.util.List;

@Component
public class LinuxSystemCommander  {
//    @Override
    public JSONObject getAllMonitor() {
        String result = CommandUtil.execSystemCommand("top -n 1");
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

        return jsonObject;
    }

//    @Override
    public List<Object> getProcessList(String processName) {
        return null;
    }

//    @Override
    public String emptyLogFile(File file) {
        return null;
    }


//    获取cpu信息
    public static Cpu getLinuxCpu(String info){
        Cpu cpu = new Cpu();
        if (StrUtil.isEmpty(info)){
            return null;
        }
            int i = info.indexOf(CharPool.COLON);
            String[] split = info.substring(i + 1).split(StrUtil.COMMA);
            for (String str : split) {
                if (str.contains("idle")) {
                    String value = str.split(StrUtil.SPACE)[1].replace("%", "");
                    double val = Convert.toDouble(value, 0.0);
                    cpu.setIdle(val);
                    cpu.setTotal(100 - val);
                }
                if (str.contains("user")){
                    String value = str.split(StrUtil.SPACE)[1].replace("%", "");
                    double val = Convert.toDouble(value, 0.0);
                    cpu.setUser(val);
                }
                if (str.contains("sys")){
                    String value = str.split(StrUtil.SPACE)[1].replace("%", "");
                    double val = Convert.toDouble(value, 0.0);
                    cpu.setSys(val);
                }
            }
        return cpu;
    }

    public static Memory getLinuxMemory(String info){
        Memory memory = new Memory();
        if (StrUtil.isEmpty(info)){
            return null;
        }
        double used = 0;
        double free = 0;

        int index = info.indexOf(CharPool.COLON) + 1;
        String[] split = info.substring(index).split(StrUtil.COMMA);
        for (String str : split){
            System.out.println(str);
            if (str.contains("unused.")){
                String value = str.split(StrUtil.SPACE)[1].replace("M","");
                free = Convert.toDouble(value, 0.0);
                memory.setUnused(free);
            }else if (str.contains("used")){
                String[] value = str.split(StrUtil.SPACE);
                if (Convert.toInt(value[1].indexOf("M")) > -1){
                    value[1].replace("M","");
                }
                if (Convert.toInt(value[1].indexOf("G")) > -1){
                    String res = value[1].replace("G","");
                    used = Convert.toDouble(res , 0.0) * 1024;

                }
                memory.setUsed(used);
            }
        }
        memory.setTotal(used / (used+ free));
        return memory;
    }
}
