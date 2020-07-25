package com.rutsubosuki.web;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.rutsubosuki.web.form.UploadForm;
@Controller
public class UploadController {
	@Autowired
    private JdbcTemplate jdbc;//jdbcを使用
	private String getExtension(String filename) {
      int dot = filename.lastIndexOf(".");
      if (dot > 0) {
        return filename.substring(dot).toLowerCase();
      }
      return "";
    }
    private String getUploadFileName(String fileName) { //アップするファイル名に日時を追加
        return fileName + "_" +
                DateTimeFormatter.ofPattern("yyyyMMddHHmm")
                    .format(LocalDateTime.now())
                + getExtension(fileName);
    }
    private void createDirectory() {
        Path path = Paths.get("C:/upload");
        if (!Files.exists(path)) {
          try {
            Files.createDirectory(path);
          } catch (Exception e) {
            //エラー処理は省略
          }
        }
    }
    private void savefile(MultipartFile file) {
      String filename = getUploadFileName(file.getOriginalFilename());

      //DBにアップしたファイル名を追加
      String sql = "INSERT INTO uploads (image_name) VALUES('" + filename + "')";
      jdbc.update(sql);
      //↓URLベタ打ちまずいねえ
      Path uploadfile = Paths.get("C:/pleiades/workspace/rutsubosuki/src/main/resources/static/images/" + filename); //
      try (OutputStream os = Files.newOutputStream(uploadfile, StandardOpenOption.CREATE)) {
        byte[] bytes = file.getBytes();
        os.write(bytes);
      } catch (IOException e) {
        //エラー処理は省略
      }
    }


    private void savefiles(List<MultipartFile> multipartFiles) { //
        createDirectory();
        for (MultipartFile file : multipartFiles) {
            savefile(file);
        }
    }
    @RequestMapping(path = "/upload", method = RequestMethod.GET)
    String uploadview(Model model) {
      model.addAttribute("uploadForm", new UploadForm());
      return "upload";
    }
    @RequestMapping(path = "/imageupload", method = RequestMethod.POST) //アップロード処理
    String upload(Model model, UploadForm form) {
      if (form.getFile()==null || form.getFile().isEmpty()) {
        //エラー処理は省略
        return "upload";
      }
      savefiles(form.getFile());
      return "redirect:/upload";
    }


}