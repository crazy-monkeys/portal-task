package com.crazy.portal.controller;

import com.alibaba.fastjson.JSON;
import org.hyperic.sigar.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.*;

//@RestController
public class SocketController {

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    private static final Logger logger = LoggerFactory.getLogger(SocketController.class);

    private static RuntimeMXBean jvmRuntime;
    private static MemoryMXBean jvmMem;
    private static com.sun.management.OperatingSystemMXBean systemMem;
    private static Map<String, String> systemParamsMap = new HashMap<>();

    private static NumberFormat fmtI = new DecimalFormat("###,###", new DecimalFormatSymbols(Locale.ENGLISH));
    private static NumberFormat fmtD = new DecimalFormat("###,##0.000", new DecimalFormatSymbols(Locale.ENGLISH));
    private static final int Kb = 1024;
    private static File[] disks;
    private static Sigar sigar;
    private static String osName;
    private static String jdkHome;

    static {
        try {
            String sigarFilePath = SocketController.class.getResource("/").getPath()+"/sigarFile/";
            System.out.println();
            String path = System.getProperty("java.library.path");
            if (!path.contains(sigarFilePath)) {
                if (isOSWin()) {
                    path += ";" + sigarFilePath;
                } else {
                    path += ":" + sigarFilePath;
                }
                System.setProperty("java.library.path", path);
            }
            jvmRuntime = ManagementFactory.getRuntimeMXBean();
            jvmMem = ManagementFactory.getMemoryMXBean();
            sigar = new Sigar();
            systemMem = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            //不常变动的系统信息
            systemParamsMap.put("ip", InetAddress.getLocalHost().getHostAddress());
            systemParamsMap.put("tomcatHome", System.getProperty("catalina.home"));
            systemParamsMap.put("cpuGhz",String.valueOf(String.format("%.2f", sigar.getCpuInfoList()[0].getMhz()/1000d)+"Ghz"));
            Properties props = System.getProperties();
            osName = props.getProperty("os.name");
            jdkHome = System.getProperty("java.home");
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public static boolean isOSWin(){
        String OS = System.getProperty("os.name").toLowerCase();
        return OS.indexOf("win") >= 0?true:false;
    }

    @Scheduled(cron = "* * * * * ?")
    @SendTo("/topic/getResponse")
    public Object getHeatbeat() throws Exception {
        //实时获取的跳跃服务器数据信息h
        systemParamsMap.put("heapMemoryUsed", String.valueOf(jvmMem.getHeapMemoryUsage().getUsed() / Kb));
        systemParamsMap.put("heapMemoryMax", String.valueOf(jvmMem.getHeapMemoryUsage().getMax() / Kb));
        systemParamsMap.put("heapMemoryCommitted", String.valueOf(jvmMem.getHeapMemoryUsage().getCommitted() / Kb));

        systemParamsMap.put("jvmRunTime", toDuration(jvmRuntime.getUptime()));


        Mem mem = sigar.getMem();
        //系统信息  单位:kb
        systemParamsMap.put("systemTotalRAM", String.valueOf(mem.getTotal()  / 1024));
        systemParamsMap.put("systemAvailableRAM", String.valueOf(mem.getUsed() / 1024));
        systemParamsMap.put("systemFreeRAM",String.valueOf(mem.getFree() / 1024));

        //单块CPU的使用比
        systemParamsMap.put("cpuTotalUsedRate",String.valueOf(CpuPerc.format(sigar.getCpuPerc().getCombined())));

        try{
            //磁盘信息 单位kb
            List<DiskInfo> diskInfos = new ArrayList<>();
            FileSystem fslist[] = sigar.getFileSystemList();
            for (int i = 0; i < fslist.length; i++) {
                FileSystem fs = fslist[i];
                FileSystemUsage usage;
                if(fs.getType() == 2){
                    usage = sigar.getFileSystemUsage(fs.getDirName());
                    DiskInfo diskInfo = new DiskInfo();
                    diskInfo.setDiskName(fs.getDevName());
                    diskInfo.setSysTypeName(fs.getSysTypeName());
                    diskInfo.setTotalSpace(usage.getTotal());
                    diskInfo.setFreeSpace(usage.getFree());
                    diskInfo.setUsableSpace(usage.getUsed());
                    diskInfo.setUsePercent(usage.getUsePercent() * 100D);
                    diskInfo.setDiskReads(usage.getDiskReads());
                    diskInfo.setDiskWrites(usage.getDiskWrites());
                    diskInfos.add(diskInfo);
                }
            }
            systemParamsMap.put("diskInfos", JSON.toJSONString(diskInfos));
        }catch (SigarPermissionDeniedException e){
            logger.error("sigar无权限访问");
        }
        messagingTemplate.convertAndSend("/topic/getResponse",systemParamsMap);
       return "callBack";
    }

    class DiskInfo{
        private String diskName;
        private String sysTypeName;
        private Long totalSpace;
        private Long freeSpace;
        private Long usableSpace;
        private Double usePercent; //百分比
        private Long diskReads;
        private Long DiskWrites;


        public String getSysTypeName() {
            return sysTypeName;
        }

        public void setSysTypeName(String sysTypeName) {
            this.sysTypeName = sysTypeName;
        }

        public String getDiskName() {
            return diskName;
        }

        public void setDiskName(String diskName) {
            this.diskName = diskName;
        }

        public Long getTotalSpace() {
            return totalSpace;
        }

        public void setTotalSpace(Long totalSpace) {
            this.totalSpace = totalSpace;
        }

        public Long getFreeSpace() {
            return freeSpace;
        }

        public void setFreeSpace(Long freeSpace) {
            this.freeSpace = freeSpace;
        }

        public Long getUsableSpace() {
            return usableSpace;
        }

        public void setUsableSpace(Long usableSpace) {
            this.usableSpace = usableSpace;
        }

        public Double getUsePercent() {
            return usePercent;
        }

        public void setUsePercent(Double usePercent) {
            this.usePercent = usePercent;
        }

        public Long getDiskReads() {
            return diskReads;
        }

        public void setDiskReads(Long diskReads) {
            this.diskReads = diskReads;
        }

        public Long getDiskWrites() {
            return DiskWrites;
        }

        public void setDiskWrites(Long diskWrites) {
            DiskWrites = diskWrites;
        }
    }

    protected static String toDuration(double uptime) {
        uptime /= 1000;
        if (uptime < 60) {
            return fmtD.format(uptime) + " seconds";
        }
        uptime /= 60;
        if (uptime < 60) {
            long minutes = (long) uptime;
            String s = fmtI.format(minutes) + (minutes > 1 ? " minutes" : " minute");
            return s;
        }
        uptime /= 60;
        if (uptime < 24) {
            long hours = (long) uptime;
            long minutes = (long) ((uptime - hours) * 60);
            String s = fmtI.format(hours) + (hours > 1 ? " hours" : " hour");
            if (minutes != 0) {
                s += " " + fmtI.format(minutes) + (minutes > 1 ? " minutes" : " minute");
            }
            return s;
        }
        uptime /= 24;
        long days = (long) uptime;
        long hours = (long) ((uptime - days) * 24);
        String s = fmtI.format(days) + (days > 1 ? " days" : " day");
        if (hours != 0) {
            s += " " + fmtI.format(hours) + (hours > 1 ? " hours" : " hour");
        }
        return s;
    }
}
