package com.unicon.unicon.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.unicon.unicon.payload.UploadFileResponse;
import com.unicon.unicon.payload.UploadStampFileResponse;
import com.unicon.unicon.service.FileStorageService;

/**
 * 
 * @author brahnavan
 *
 */

@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;
    
    /**
     * API to upload file
     * 
     * @param file
     * @return UploadFileResponse
     */
    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }
    
    /**
     * API to stamp or watermark pdf
     * 
     * 
     * @param file
     * @param stamp
     * @return UploadStampFileResponse
     * @throws Exception
     */
    @PostMapping("/stamp")
    public UploadStampFileResponse uploadPdfFile(@RequestParam("file") MultipartFile file, @RequestParam("stamp") MultipartFile stamp) throws Exception {
        String fileName = fileStorageService.storeFile(file);
        String stampName = fileStorageService.storeFile(stamp);
        
        String stampedName = fileStorageService.stampFile(file, stamp, logger);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(stampedName)
                .toUriString();

        return new UploadStampFileResponse(fileName, stampName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }
    
    /**
     * API to convert pdf to HTML
     * 
     * @param file
     * @return UploadFileResponse
     * @throws Exception
     */
    @PostMapping("/toHtml")
    public UploadFileResponse convertPdfFile(@RequestParam("file") MultipartFile file) throws Exception {        
        String stampedName = fileStorageService.toHtmlFile(file,logger);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadHtmlFile/")
                .path(stampedName)
                .toUriString();

        return new UploadFileResponse(stampedName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    /**
     * API to upload multiple files and return list uploaded of files
     * 
     * @param files
     * @return List<UploadFileResponse>
     */
    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    /**
     * API to download a given file with file name
     * 
     * @param fileName
     * @param request
     * @return ResponseEntity<Resource>
     */
    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    
    /**
     * 
     * API to download a given HTML file with file name
     * 
     * @param fileName
     * @param request
     * @return ResponseEntity<Resource>
     */
    @GetMapping("/downloadHtmlFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadHtmlFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadHtmlFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    
    /**
     * API to load all HTML Files
     * @return  List<UploadFileResponse>
     */
	@GetMapping("/files")
	public List<UploadFileResponse> showFiles() {
		return fileStorageService.loadHtmlFiles();
	}
	
	
	/**
	 * 
	 * API to delete all files 
	 * 
	 * @return String
	 */
	@GetMapping("/deleteAll")
	public String deleteFiles() {
		return fileStorageService.deleteFiles();
	}
}
