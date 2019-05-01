/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.util;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.Arrays;
import java.util.UUID;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author sdrahnea
 */
@Service
@EnableScheduling
public class BackUpper {

    private String DEFAULT_PATH = "root/dbbackups/";
    private static final int REMOVE_LAST = 5;
    
    public BackUpper() {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            this.DEFAULT_PATH = "D:/dbbackups/";
        }
    }

    private void backUp() throws IOException, InterruptedException {
        File dir = new File(DEFAULT_PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }

        //Process runtimeProcess = Runtime.getRuntime().exec("mysqldump -uroot -proot tms > " + DEFAULT_PATH + getRandomUUID() + ".sql");
        ///usr/LOCAL/mysql/BIN/
        Process runtimeProcess = Runtime.getRuntime().exec("/usr/LOCAL/mysql/BIN/mysqldump -uroot -proot tms > " + DEFAULT_PATH + getRandomUUID() + ".sql");
        runtimeProcess.waitFor();
    }

    private void restore(final String fileName) throws IOException, InterruptedException {
        if (fileName != null) {

            File backupFile = new File(DEFAULT_PATH + fileName);
            String[] command = new String[]{"mysql ", "-uroot", "-proot", "tms"};

            ProcessBuilder processBuilder = new ProcessBuilder(Arrays.asList(command));
            processBuilder.redirectError(Redirect.INHERIT);
            processBuilder.redirectInput(Redirect.from(backupFile));

            Process process = processBuilder.start();
            process.waitFor();
        }

    }

    public void restoreLastBackUp() throws IOException, InterruptedException {
        File dir = new File(DEFAULT_PATH);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files.length > 0) {
                Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
                restore(files[0].getName());
            }

        }
    }

    /**
     * remove oldest files (where file age is more than one week), but lastest
     * 10 files should remains. In case when age of latest files are more then
     * one week and folder contains only 10 files then files will not be
     * removed.
     */
    private void removeOldestFiles() {
        File dir = new File(DEFAULT_PATH);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            final int arraySize = files.length;
            if (arraySize > REMOVE_LAST) {
                Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
                for (int index = REMOVE_LAST; index < arraySize; index++) {
                    files[index].delete();
                }
            }
        }
    }

    private String getRandomUUID() {
        return UUID.randomUUID().toString().toLowerCase();
    }

    /**
     * run everyday
     */
    @Scheduled(cron = "*/5 * * * * ?") //every 5 seconds
    //@Scheduled(cron = "0 0 * * * ?") //every day start with 00:00
    public void createBackUpFileEveryDay() {
        try {
            backUp();
            removeOldestFiles();
        } catch (Exception ex) {
            System.out.println(">>>" + ex);
        }
    }

}
