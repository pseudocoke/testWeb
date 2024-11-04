package com.test.vo;

import java.net.URLEncoder;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mass.db.annotation.ExtColumnName;
import com.mass.db.annotation.ExtSearchParam;
import com.mass.db.annotation.ExtTableName;
import com.mass.db.util.ConvertUtil;

/*
 * DBGenBaFile : BA_FILE 처리를 위한 DB VO
 * 
 * 20230605
 * 
 */

@ExtTableName("BA_FILE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DBGenBaFile {

	@ExtColumnName("FILE_NO")	@ExtSearchParam("FILE_NO")	private String fileNo;	//	파일번호
	@ExtColumnName("FILE_SEQ_NO")	@ExtSearchParam("FILE_SEQ_NO")	private String fileSeqNo;	//	파일일렬번호
	@ExtColumnName("NAME")	private String name;	//	파일명
	@ExtColumnName("EXTENTION")	private String extention;	//	확장자
	@ExtColumnName("S3_UPLOAD_URL")	private String s3UploadUrl;	//	S3 업로드 URL
	@ExtColumnName("CREATE_DATE")	private String createDate;	//	생성일자
	@ExtColumnName("CREATE_BY")	private String createBy;	//	생성자
	@ExtColumnName("UPDATE_DATE")	private String updateDate;	//	수정일자
	@ExtColumnName("UPDATE_BY")	private String updateBy;	//	수정자
	
	@ExtColumnName("TEMP_YN") private String tempYn;	//	임시파일여부 
	@ExtColumnName("S3_KEY")	private String s3Key;	//	S3 업로드 Key
	@ExtColumnName("FILE_SIZE")	private String fileSize;	//	파일크기
	
	//	Specified Getter & Setter
	public String getFileSizeForUI() {
		return ConvertUtil.getFileSizeForUI(fileSize);
	}
	
	public String getS3UploadUrlForConnection() {
		//	s3UploadUrl 에서 마지막 파일이름만 url encoding을 하고 내려받을 수 있도록 처리한다.
		//	name이 s3UploadUrl에서 최종 파일명 이후의 정보를 재처리하는 것을 활용한다.
		String nameEncode = URLEncoder.encode(name);
		return StringUtils.replace(s3UploadUrl, name, nameEncode);
	}
	
	//	Auto-Generated

	public String getFileNo() {
		return fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	public String getFileSeqNo() {
		return fileSeqNo;
	}

	public void setFileSeqNo(String fileSeqNo) {
		this.fileSeqNo = fileSeqNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtention() {
		return extention;
	}

	public void setExtention(String extention) {
		this.extention = extention;
	}

	public String getS3UploadUrl() {
		return s3UploadUrl;
	}

	public void setS3UploadUrl(String s3UploadUrl) {
		this.s3UploadUrl = s3UploadUrl;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getTempYn() {
		return tempYn;
	}

	public void setTempYn(String tempYn) {
		this.tempYn = tempYn;
	}

	public String getS3Key() {
		return s3Key;
	}

	public void setS3Key(String s3Key) {
		this.s3Key = s3Key;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}




}