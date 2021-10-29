package com.unicon.unicon.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties class to handle file storage locations from property file
 * 
 * @author brahnavan
 *
 */

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
	
    private String uploadDir;
    private String uploadSubDirPdf;
    private String uploadSubDirHtml;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

	public String getUploadSubDirPdf() {
		return uploadSubDirPdf;
	}

	public void setUploadSubDirPdf(String uploadSubDirPdf) {
		this.uploadSubDirPdf = uploadSubDirPdf;
	}

	public String getUploadSubDirHtml() {
		return uploadSubDirHtml;
	}

	public void setUploadSubDirHtml(String uploadSubDirHtml) {
		this.uploadSubDirHtml = uploadSubDirHtml;
	}
}