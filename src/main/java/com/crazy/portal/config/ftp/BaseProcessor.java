package com.crazy.portal.config.ftp;

import org.apache.camel.Exchange;
import org.apache.camel.component.file.GenericFileMessage;
import org.springframework.stereotype.Component;

import java.io.RandomAccessFile;

@Component
public class BaseProcessor {

    public String getCurrentFilePath(Exchange exchange){
        GenericFileMessage<RandomAccessFile> inFileMessage = (GenericFileMessage<RandomAccessFile>) exchange.getIn();
        return inFileMessage.getGenericFile().getFileName();
    }

}
