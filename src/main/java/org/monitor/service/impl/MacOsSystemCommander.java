/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Code Technology Studio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
import java.util.ArrayList;
import java.util.List;

import static org.monitor.service.impl.LinuxSystemCommander.getLinuxCpu;
import static org.monitor.service.impl.LinuxSystemCommander.getLinuxMemory;

/**
 * @author User
 */
@Service
public class MacOsSystemCommander {

//	@Override
	public JSONObject getAllMonitor() {
		String result = CommandUtil.execSystemCommand("top -l 1 | head");
		System.out.println(result);
		if (StrUtil.isEmpty(result)) {
			return null;
		}
		String[] split = result.split(StrUtil.LF);
		int length = split.length;
		JSONObject jsonObject = new JSONObject();
		if (length > 3) {
			String cpus = split[3];
			// cpu占比
			Cpu cpu = getMacOsCpu(cpus);
			jsonObject.put("cpu", cpu);
		}
		if (length > 6){
			String mem = split[6];
			Memory memory = getLinuxMemory(mem);
			jsonObject.put("memory", memory);
		}

		return jsonObject;
	}

//	@Override
	public List<Object> getProcessList(String processName) {
		return null;
	}

//	@Override
	public String emptyLogFile(File file) {
		return null;
	}

	public static Cpu getMacOsCpu(String info){
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
