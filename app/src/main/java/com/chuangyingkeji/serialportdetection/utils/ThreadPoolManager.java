package com.chuangyingkeji.serialportdetection.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Norton on 2017/11/8.
 * 线程池管理类
 */

public class ThreadPoolManager {
    private static ThreadPoolManager manager;

    private ThreadPoolExecutor threadPoolExecutor = null;

    private ExecutorService fixedThreadPool = null;//是一个核心线程数量固定的线程池

    //最大的优势是它可以根据程序的运行情况自动来调整线程池中的线程数量
    private ExecutorService cachedThreadPool = null;

    private ScheduledExecutorService scheduledExecutorService = null;//定时定期执行任务

    private ThreadPoolManager() {
    }

    /**
     * 获取线程池管理类的实例
     *
     * @return 生成的线程池管理类对象
     */
    public static ThreadPoolManager getManager() {
        if (manager == null) {
            manager = new ThreadPoolManager();
        }
        return manager;
    }

    /**
     * 获取线程池对象
     *
     * @param corePoolSize    核心线程数
     * @param maximumPoolSize 线程池最大非核心线程数
     * @return 生成的线程池对象
     */
    public ThreadPoolExecutor getThreadPoolExecutor(int corePoolSize, int maximumPoolSize, int workQueueSize) {
        if (threadPoolExecutor == null) {
            long keepAliveTime = 60;
            TimeUnit unit = TimeUnit.SECONDS;
            LinkedBlockingDeque<Runnable> workQueue = new LinkedBlockingDeque<>(workQueueSize);
            threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
            //设置核心线程不被回收
            threadPoolExecutor.allowCoreThreadTimeOut(false);
        }
        return threadPoolExecutor;
    }

    /**
     * 获取FixedThreadPool对象
     *
     * @param corePoolSize 核心线程数
     * @return 生成的FixedThreadPool对象
     */
    public ExecutorService getFixedThreadPool(int corePoolSize) {
        if (fixedThreadPool == null) {
            /*fixedThreadPool是一个只有核心线程的线程池
             *核心线程超时时间为0,即永远不会被回收
             *使用的队列为LinkedBlockingDeque,队列的大小为默认(Integer.MAX_VALUE)
             */
            fixedThreadPool = Executors.newFixedThreadPool(corePoolSize);
        }
        return fixedThreadPool;
    }

    /**
     * 获取CachedThreadPool对象
     *
     * @return 生成的CachedThreadPool对象
     */
    public ExecutorService getCachedThreadPool() {
        if (cachedThreadPool == null) {
            /*fixedThreadPool是一个没有核心线程的线程池
             *核心线程超时时间为60秒
             *即意味着在一个任务执行完成后60秒内有新的任务进来,则不会创建新的线程
             *使用的队列为SynchronousQueue,队列的大小为默认(Integer.MAX_VALUE)
             */
            cachedThreadPool = Executors.newCachedThreadPool();
        }
        return cachedThreadPool;
    }

    /**
     * 获取ScheduledExecutorService对象
     *
     * @param corePoolSize 核心线程数
     * @return 生成的ScheduledExecutorService对象
     */
    public ScheduledExecutorService getScheduledExecutorService(int corePoolSize) {
        if (scheduledExecutorService == null) {
            /*scheduleExecutorService是一个具有延迟执行或定时执行的线程池
             *scheduleExecutorService.schedule(Runnable command,long delay, TimeUnit unit);
             * 延迟执行任务,延迟时间为delay
             *scheduleExecutorService.scheduleAtFixedRate(Runnable command,long initialDelay,long period,TimeUnit unit);
             * 延迟initialDelay时间开始执行任务,任务开始执行之后每隔period时间重复一次
             *scheduleExecutorService.scheduleWithFixedDelay(Runnable command,long initialDelay,long delay,TimeUnit unit);
             * 延迟initialDelay时间开始执行任务,任务执行完成之后每隔delay时间重复一次
             */
            scheduledExecutorService = Executors.newScheduledThreadPool(corePoolSize);
        }
        return scheduledExecutorService;
    }
}
