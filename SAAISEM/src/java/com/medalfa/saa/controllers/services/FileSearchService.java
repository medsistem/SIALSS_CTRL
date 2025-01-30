/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.saa.controllers.services;

import com.medalfa.saa.vo.FileSearch;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP-MEDALFA
 */
public class FileSearchService {

    final String rootDirectoryReceipt = "Y:\\";
    
    final String rootDirectorySended = "X:\\";
    
    private String getRootDirectory(String type){
        if(type.equals("entregado")){
            return this.rootDirectorySended;
        }
        return this.rootDirectoryReceipt;
    }

    public List<File> searchFileNames(String fileName, File directorySearch, String type) {
        File directory;
        List<File> result = new ArrayList<File>();
        if (directorySearch == null) {
            directory = new File(this.getRootDirectory(type));

        } else {
            directory = directorySearch;
        }
        System.out.println("buscando en directorio " + directory.getAbsolutePath());
        MyFilenameFilter filter = new MyFilenameFilter(fileName);
        File[] fileList = directory.listFiles();
        for (File file : fileList) {
            if (file.isDirectory()) {
                result.addAll(this.searchFileNames(fileName, file, type));
            }
        }
        File[] fileNameList = directory.listFiles(filter);
        if (fileNameList != null) {
            result.addAll(Arrays.asList(fileNameList));
        }
        return result;
    }

    public File searchFile(String fileName, File directorySearch, String type) {
        File directory, result = null;

        if (directorySearch == null) {
            directory = new File(this.getRootDirectory(type));

        } else {
            directory = directorySearch;
        }
        System.out.println("buscando en directorio " + directory.getAbsolutePath());
        MyFilenameFilter filter = new MyFilenameFilter(fileName);
        File[] fileNameList = directory.listFiles(filter);
        if (fileNameList != null) {
            if (fileNameList.length > 0) {
                return fileNameList[0];
            }
        }

        File[] fileList = directory.listFiles();
        for (File file : fileList) {
            if (file.isDirectory()) {
                result = this.searchFile(fileName, file, type);
                if(result != null){
                    return result;
                }
            }
        }
        return result;
    }

    public FileSearch downloadFile(String fileName, String type) {
        FileSearch result = new FileSearch();
        try {
            File file = this.searchFile(fileName, null, type);
            result.setFileName(file.getName());
            result.setFileBytes(this.getFileBytes(file));
            return result;
        } catch (IOException ex) {
            Logger.getLogger(FileSearchService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public byte[] getFileBytes(File file) throws IOException {
        byte[] bytes = new byte[(int) file.length()];

        FileInputStream fis = null;
        try {

            fis = new FileInputStream(file);
            fis.read(bytes);

        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return bytes;
    }

    class MyFilenameFilter implements FilenameFilter {

        String initials;

        public MyFilenameFilter(String initials) {
            this.initials = initials;
        }

        public boolean accept(File dir, String name) {
            return name.contains(initials);
        }
    }
}
