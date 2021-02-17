package org.salim;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUploader {
    private static final Logger LOG = LoggerFactory.getLogger(FileUploader.class);

    private static void checkFile (List<File> fileList, File file) {
        if (file.isFile()) {
            LOG.info("<<{}>> Is file", file.getName());
            fileList.add(file);
        } else {
            LOG.info("<<{}>> Is directory, go deeper", file.getName());
            for (File nextFile : file.listFiles()) {
                checkFile(fileList, nextFile);
            }
        }
    }

    public static void main(String[] args) throws Exception{
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("UTF-8");
        ftpClient.setDataTimeout(120000);
        ftpClient.connect("81.68.160.36");
        ftpClient.login("ubuntu", "Jfk_181235");
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        Integer replyCode = ftpClient.getReplyCode();

        if (FTPReply.isPositiveCompletion(replyCode)) {
            LOG.info("Got Connection");
        } else {
            LOG.info("No Connection");
        }

        //local files
        File localFileBase = new File("D:\\filerepo\\");
        if (localFileBase.exists() && localFileBase.isDirectory()) {
            //遍历，推荐用递归,列出所有文件
            List<File> allFiles = new ArrayList<>();
            checkFile(allFiles, localFileBase);

            System.out.println("Fin");
        } else {
            LOG.error("This path not exists or not directory");
        }

        //remote files


    }
}
