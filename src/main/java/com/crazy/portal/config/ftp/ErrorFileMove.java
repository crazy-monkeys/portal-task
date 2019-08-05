package com.crazy.portal.config.ftp;

import com.crazy.portal.util.system.DateUtil;

import java.io.File;
import java.util.Date;

public class ErrorFileMove {
    public static void moveFile(String errorFilePath,String localPath){
        File errorFile = new File(errorFilePath);
        if(errorFile.isFile()){
            StringBuilder sb = new StringBuilder(localPath);
            sb.append(File.separator).append("error").append(File.separator).append(DateUtil.format(new Date(),DateUtil.webFormat));

            File errorDir = new File(sb.toString());

            if(!errorDir.exists()){
                errorDir.mkdirs();
            }

            File ftpErrorFile = new File(sb.append(File.separator).append(errorFile.getName()).toString());
            errorFile.renameTo(ftpErrorFile);
        }

    }

}
