/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.saa.controllers;

import com.google.gson.Gson;
import com.medalfa.saa.controllers.services.FileSearchService;
import com.medalfa.saa.vo.FileSearch;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author HP-MEDALFA
 */
@WebServlet(name = "FileSearchController", urlPatterns = {"/fileSearch"})
public class FileSearchController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String requestData = request.getReader().lines().collect(Collectors.joining());
        Gson gson = new Gson();
        FileSearch fileSearch = gson.fromJson(requestData, FileSearch.class);
        FileSearchService service = new FileSearchService();
        switch (fileSearch.getAction()) {
            case "searchFile": {
                List<String> fileNames = this.searchFiles(fileSearch.getFileName(), fileSearch.getType());
                fileNames = fileNames.stream().filter(f -> f.contains(".pdf")).collect(Collectors.toList());
//                fileNames = fileNames.stream().filter(f  -> f.contains(".pdf")).collect(Collectors.toList());
                response.getWriter().print(gson.toJson(fileNames));
                break;
            }
            case "downloadFile": {
                if (fileSearch.getFileName().toLowerCase().contains(".pdf")) {
                    fileSearch = service.downloadFile(fileSearch.getFileName(), fileSearch.getType());

                    response.setContentType("application/pdf");

                    response.setHeader("Content-disposition", "filename='" + fileSearch.getFileName() + "'");
                    response.getOutputStream().write(fileSearch.getFileBytes());
                } else {
                    response.sendError(400);
                }
                break;
            }
        }

    }

    private List<String> searchFiles(String fileName, String type) {
        FileSearchService service = new FileSearchService();
        List<File> files = service.searchFileNames(fileName, null, type);
        List<String> fileNames = new ArrayList<String>();
        for (File file : files) {
            System.out.println(file.getName());
            fileNames.add(file.getName());
        }
        return fileNames;
    }
}
