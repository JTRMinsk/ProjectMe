package org.salim;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FileUploader {
    public static void main(String[] args) throws Exception{
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("UTF-8");
        ftpClient.setDataTimeout(120000);
        ftpClient.connect("81.68.160.36");
        ftpClient.login("ubuntu", "Jfk_181235");
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        Integer replyCode = ftpClient.getReplyCode();

        if (FTPReply.isPositiveCompletion(replyCode)) {
            System.out.println("Yep");
        } else {
            System.out.println("Nop");
        }

        //local files

        //remote files


    }
}
