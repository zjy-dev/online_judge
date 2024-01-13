package com.example.onlinejudge.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

public class ProcessRunner {
    public ProcessRet runProcess(String... cmd) throws IOException, InterruptedException {
        // 创建进程
        var builder = new ProcessBuilder(cmd);

        // 重定向标准错误(和标准输出合并)
        builder.redirectErrorStream(true);

        // 运行
        var process = builder.start();

        // 读取输出
        String line;
        StringBuilder output = new StringBuilder();
        var bufferReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while ((line = bufferReader.readLine()) != null) {
            output.append(line).append("\n");
        }
        if (output.length() != 0) {
            output.deleteCharAt(output.length() - 1);
        }
        return new ProcessRet(process.waitFor(), output.toString());
    }

    // 单位毫秒
    public ProcessRet runProcessWithTimelimitAndInput(int tl, String input, String... cmd) throws IOException, InterruptedException {
        // 创建进程
        var builder = new ProcessBuilder(cmd);

        // 重定向标准错误(和标准输出合并)
        builder.redirectErrorStream(true);

        // 运行
        var process = builder.start();

        // 指定时间后杀进程
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                process.destroy();
                timer.cancel();
            }
        }, tl);// 设定指定的时间time, 单位毫秒

        // input内容
        var out =  process.getOutputStream();
        out.write(input.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();

        // 读取输出
        StringBuilder output = new StringBuilder();
        String line;
        var bufferReader = new BufferedReader(new InputStreamReader(process.getInputStream()));


        // 以"\n"拼接每一行
        while ((line = bufferReader.readLine()) != null) {
            output.append(line).append("\n");
        }

        if (output.length() != 0) {
            output.deleteCharAt(output.length() - 1);
        }

        int ret = process.waitFor();
        timer.cancel();
        return new ProcessRet(ret, output.toString());
    }
}


