package com.rutsubosuki.web;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class imageController {
	@Autowired
    private JdbcTemplate jdbc;//jdbcを使用


	//データテーブルのuploadsをリストに格納し返す→画像一覧（/image）
	@RequestMapping(value = "/image")
	private ModelAndView imagelist(ModelAndView mav) {
        List<Map<String, Object>> images =jdbc.queryForList("SELECT * FROM uploads");
        mav.addObject("images", images);
        mav.setViewName("image/imagelist");
		return mav;
	}


    //画像削除（/image/imageEdit）フォーム入力されたidのデータをデータテーブルuploadsから削除
    @RequestMapping(value = "/image/imageEdit")
    public void imageEdit(
    		@RequestParam (name = "action", required = false) String submit,//submitボタンのValueを受け取る
    	    @NotNull@RequestParam(name = "image1", required = false) String image1,
    	    @NotNull@RequestParam(name = "image2", required = false) String image2,
    	    @NotNull@RequestParam(name = "image3", required = false) String image3,
    	    @NotNull@RequestParam(name = "image4", required = false) String image4
    	    ) {
	String sqlDelete =  "DELETE FROM uploads WHERE image_id in ('" + image1 + "','" + image2 + "','" + image3 + "','" + image4 + "') ";
	jdbc.update(sqlDelete);


    }
}