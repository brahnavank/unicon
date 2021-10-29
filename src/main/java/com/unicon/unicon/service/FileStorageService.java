package com.unicon.unicon.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

//import com.example.fileserver.process.Watermark;
import com.unicon.unicon.exception.FileStorageException;
import com.unicon.unicon.exception.MyFileNotFoundException;
import com.unicon.unicon.payload.UploadFileResponse;
import com.unicon.unicon.process.PDFToHTML;
import com.unicon.unicon.property.FileStorageProperties;

/**
 * 
 * @author brahnavan
 *
 */
@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private final Path fileStorageLocationHtml;
    private final Path fileStorageLocationPdf;
    
    /**
     * Service which creates all the necessary configured directories 
     * @param fileStorageProperties
     */

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        this.fileStorageLocationHtml = Paths.get(fileStorageProperties.getUploadSubDirHtml())
                .toAbsolutePath().normalize();

        this.fileStorageLocationPdf = Paths.get(fileStorageProperties.getUploadSubDirPdf())
                .toAbsolutePath().normalize();
        
        try {
        	// create storage locations
            Files.createDirectories(this.fileStorageLocation);
            Files.createDirectories(this.fileStorageLocationHtml);
            Files.createDirectories(this.fileStorageLocationPdf);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
    
    /**
     * Methods to load HTML files
     * @return List<UploadFileResponse>
     */
    @Autowired
    public List<UploadFileResponse> loadHtmlFiles() {
        try {
        	File folder = Paths.get(this.fileStorageLocation.toString()+File.separator+"html").toFile();
            String[] files = folder.list();
            return Arrays.asList(files).stream().map(file -> new UploadFileResponse(new File(file).getName(), file, "html", 0)).collect(Collectors.toList());
            
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
    
    /**
     * Methods to delete multiple files
     * @return
     */
    @Autowired
    public String deleteFiles() {
        try {
        	File folder = Paths.get(this.fileStorageLocation.toString()+File.separator+"html").toFile();
        	FileUtils.cleanDirectory(folder); 
            return "All files deleted";
            
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }


    /**
     * Method to store file
     * @param file
     * @return String
     */
    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    /**
     * Methods to load file as Resource
     * @param fileName
     * @return Resource
     */
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
    
    /**
     * Method to load HTML file a Resources
     * @param fileName
     * @return Resource
     */
    public Resource loadHtmlFileAsResource(String fileName) {
        try {
            Path filePath = Paths.get(this.fileStorageLocation.toString()+File.separator+"html").resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
    
    /**
     * Methods to handle stamp/watermark file and storage
     * @param file
     * @param stamp
     * @param logger
     * @return String
     * @throws Exception
     */
    public String stampFile(MultipartFile file, MultipartFile stamp, Logger logger) throws Exception {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String stampName = StringUtils.cleanPath(stamp.getOriginalFilename());
        String output = fileName.replace(".pdf", "-stamped.pdf");

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Path targetStampLocation = this.fileStorageLocation.resolve(stampName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(stamp.getInputStream(), targetStampLocation, StandardCopyOption.REPLACE_EXISTING);

            return output;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
    
    /**
     * Method to handle PDF to HTML conversion and storage
     * @param file
     * @param logger
     * @return String
     * @throws Exception
     */
    public String toHtmlFile(MultipartFile file, Logger logger) throws Exception {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String output = fileName.replace(".pdf", ".html");

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = Paths.get(this.fileStorageLocation.toString()+File.separator+"pdf").resolve(fileName);
            Path targetOutputLocation = Paths.get(this.fileStorageLocation.toString()+File.separator+"html").resolve(output);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String[] strArray = new String[] {targetLocation.toString(),targetOutputLocation.toString()};
            PDFToHTML.convert(strArray);
            
            FileUtils.forceDelete(targetLocation.toFile());
            
            return output;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
}
