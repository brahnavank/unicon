package com.unicon.unicon.payload;

/**
 * 
 * Model/Object class for upload stamp/watermark file response
 * 
 * @author brahnavan
 *
 */
public class UploadStampFileResponse {
	
	private String fileName;
	private String stampName;
	private String fileDownloadUri;
	private String fileType;
	private long size;

	public UploadStampFileResponse(String fileName, String stampName, String fileDownloadUri, String fileType,
			long size) {
		this.fileName = fileName;
		this.stampName = stampName;
		this.fileDownloadUri = fileDownloadUri;
		this.fileType = fileType;
		this.size = size;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileDownloadUri() {
		return fileDownloadUri;
	}

	public String getFileType() {
		return fileType;
	}

	public long getSize() {
		return size;
	}

	public String getStampName() {
		return stampName;
	}

	public void setStampName(String stampName) {
		this.stampName = stampName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileDownloadUri(String fileDownloadUri) {
		this.fileDownloadUri = fileDownloadUri;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public void setSize(long size) {
		this.size = size;
	}
}