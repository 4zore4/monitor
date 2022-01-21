package org.monitor.util;


import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandUtil {

    /**
     * 系统命令
     * */
    private static final List<String> COMMAND = new ArrayList<>();

    /**
     * 文件后缀
     * */
    public static final String SUFFIX;

    static {
        if (SystemUtil.getOsInfo().isLinux()){
            COMMAND.add("/bin/sh");
            COMMAND.add("-c");
        }else if (SystemUtil.getOsInfo().isMac()){
            COMMAND.add("/bin/sh");
            COMMAND.add("-c");
        }else {
            COMMAND.add("cmd");
            COMMAND.add("/c");
        }
        if (SystemUtil.getOsInfo().isWindows()){
            SUFFIX = "bat";
        }else {
            SUFFIX = "sh";
        }

    }

    public static List<String> getCommand(){
        return ObjectUtil.clone(COMMAND);
    }

    public static String execSystemCommand(String command){
        return execSystemCommand(command,null);
    }

    public static String execSystemCommand(String command, File file){
        String newCommand = StrUtil.replace(command,StrUtil.CRLF,StrUtil.SPACE);
        newCommand = StrUtil.replace(newCommand,StrUtil.LF, StrUtil.SPACE);
        String result = "error";
        try {
            List<String> commands = getCommand();
            commands.add(newCommand);
            String[] cmd = commands.toArray(new String[]{});
            result = exec(cmd, file);
        }catch (Exception e){
            throw new RuntimeException();
        }
        return result;
    }

    private static String exec(String[] cmd, File file) throws IOException {
        Process process = new ProcessBuilder(cmd).directory(file).redirectErrorStream(true).start();
        String result = RuntimeUtil.getResult(process);
        return result;
    }

}
