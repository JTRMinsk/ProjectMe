package org.salim;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUploader {
    private static final Logger LOG = LoggerFactory.getLogger(FileUploader.class);
    private static final String remoteRootPath = "/home/ftpuser/filerepo";
    private static final String localPcName = "MKT_AIR15";
    private static final String localFileBase = "/Users/kievjtr/Desktop/FILEREPO";



    private static void checkLocalFiles (List<File> fileList, File file) {
        if (file.isFile()) {
            LOG.info("<<{}>> Is file", file.getName());
            if (!file.getName().startsWith(".")) {
                fileList.add(file);
            } else {
                LOG.info("Ignore this file:{}", file.getName());
            }
        } else {
            LOG.info("<<{}>> Is directory, go deeper", file.getName());
            for (File nextFile : file.listFiles()) {
                checkLocalFiles(fileList, nextFile);
            }
        }
    }

    private static void cdOrMkdirCd (FTPClient ftpClient, String path) throws IOException {
        if (!ftpClient.changeWorkingDirectory(path)) {
            ftpClient.makeDirectory(path);
            ftpClient.changeWorkingDirectory(path);
        }
    }



    private static void upload (FTPClient ftpClient, File localFile) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(localFile);



    }



    public static void main(String[] args) throws Exception{
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("UTF-8");
        ftpClient.setDataTimeout(120000);
        ftpClient.connect("81.68.160.36");
        ftpClient.login("ftpuser", "JFK193777");
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        Integer replyCode = ftpClient.getReplyCode();

        if (FTPReply.isPositiveCompletion(replyCode)) {
            LOG.info("Got Connection");
        } else {
            LOG.info("No Connection");
            return;
        }
        //default work space
        cdOrMkdirCd(ftpClient, remoteRootPath);

        //find this Local PC folder in remote/ if not, create
        cdOrMkdirCd(ftpClient, localPcName);

        String remoteBasePath = ftpClient.printWorkingDirectory();


        //local files
        File localFileBaseFile = new File(localFileBase);
        List<File> allLocalFiles = new ArrayList<>();
        if (localFileBaseFile.exists() && localFileBaseFile.isDirectory()) {
            //遍历，推荐用递归,列出所有文件
            checkLocalFiles(allLocalFiles, localFileBaseFile);

        } else {
            LOG.error("This path not exists or not directory");
        }

        //Go for upload MKdir/upload
        ftpClient.enterLocalPassiveMode();

        for (File localFile : allLocalFiles) {
            //first, make dirs
            String pathUnderBase = localFile.getCanonicalPath();
            pathUnderBase = pathUnderBase.replace(localFileBase, "");
            pathUnderBase = pathUnderBase.replace(localFile.getName(), "");

            pathUnderBase = pathUnderBase.replaceAll("\\\\", "/");

            LOG.info("Got file folder path under base: {}", pathUnderBase);

            if (pathUnderBase.startsWith("/")) {
                cdOrMkdirCd(ftpClient, remoteBasePath + pathUnderBase);
            } else {
                cdOrMkdirCd(ftpClient, remoteBasePath + "/" + pathUnderBase);
            }

            FileInputStream fileInputStream = new FileInputStream(localFile);
            if (ftpClient.storeFile(localFile.getName(), fileInputStream)) {
                LOG.info("Uploaded file:{}, success", localFile.getName());
            } else {
                LOG.error("Uploaded file:{}, failed", localFile.getAbsolutePath());
            }
            fileInputStream.close();

        }

        ftpClient.logout();
        ftpClient.disconnect();

    }
}
