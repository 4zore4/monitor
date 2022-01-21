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
			Cpu cpu = getLinuxCpu(cpus);
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

}
