package com.rutsubosuki.web.form;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;


public class UploadForm implements Serializable {
  private List<MultipartFile> file;

  public List<MultipartFile> getFile() {
    return this.file;
  }

  public void setFile(List<MultipartFile> file) {
    this.file = file;
  }
}