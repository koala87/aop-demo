package com.alibaba.jvm.sandbox.module.debug;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.LoadCompleted;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;



/**
 * 基于GRPC的SQL日志
 */
@MetaInfServices(Module.class)
@Information(id = "debug-grpc-logger", version = "0.0.1", author = "jinyingqi@126.com")
public class GrpcLoggerModule implements Module, LoadCompleted {
    private final Logger smLogger = LoggerFactory.getLogger("DEBUG-GRPC-LOGGER");

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    @Override
    public void loadCompleted() {
        monitorGrpc();
    }

    private void monitorGrpc() {
        new EventWatchBuilder(moduleEventWatcher)
                .onClass("io.grpc.examples.helloworld.HelloWorldServer$GreeterImpl")
                .onBehavior("sayHello")
                .onWatch(new AdviceListener() {
                    @Override
                    public void before(Advice advice) {
                        String method = advice.getBehavior().getName();
                        smLogger.error(advice.getBehavior().getName());
                        Integer cnt = advice.getParameterArray().length;
                        smLogger.error(cnt.toString());

                    }

                    @Override
                    public void afterReturning(Advice advice) {
                        smLogger.error("potter 111:");
                        String method = advice.getBehavior().getName();
                        smLogger.error(advice.getBehavior().getName());
                        Integer cnt = advice.getParameterArray().length;
                        smLogger.error(cnt.toString());
                        smLogger.error("potter 11:");
                    }

                    @Override
                    public void afterThrowing(Advice advice) {
                        smLogger.error("potter 21:");
                    }

                });
    }
}