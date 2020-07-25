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
public class TagController {
    @Autowired
    private JdbcTemplate jdbc;

    @RequestMapping("/tag") //タグ一覧ページ、/tag
    private ModelAndView taglist(ModelAndView mav) {
        List<Map<String, Object>> tags =jdbc.queryForList("SELECT * FROM tags");//タグ一覧を取得

        String tagUsed ="select tags.tag_id, tags.tagText, articles.title, articles.tag1, articles.tag2, articles.tag3, articles.tag4, articles.tag5, articles.tag6, articles.tag7 from tags left join articles on tags.tag_id = articles.tag1 or tags.tag_id = articles.tag2 or tags.tag_id = articles.tag3 or tags.tag_id = articles.tag4 or tags.tag_id = articles.tag5 or tags.tag_id = articles.tag6 or tags.tag_id = articles.tag7 order by tags.tag_id asc";
        List<Map<String, Object>> tagsused =jdbc.queryForList(tagUsed);//タグ一覧と対応記事情報を取得

        mav.addObject("tags", tags);//タグ一覧を使用
        mav.setViewName("tag/taglist");

        return mav;
    }

    //タグ追加・編集・削除
    @RequestMapping(value = "/tag/tagEdit")//tagEdit.html
    public void tagEdit(
    		@RequestParam (name = "action", required = false) String submit,//submitボタンのValueを受け取る
    		@NotNull@RequestParam (name = "id1", required = false) String id1,//@RequestParamでフォームからの値を受け取る
    		@NotNull@RequestParam(name = "tag1", required = false) String tag1//＠NotNullで未入力欄をNULL入力に変更
    	    ) {
    	//submitボタンのvalueによって追加・編集・削除の切り替え
    	if("addition".equals(submit)) {//タグ追加
    		String sqlAdd =  "INSERT INTO tags (tagText) VALUES('" + tag1 + "')";
     		jdbc.update(sqlAdd);

    	}else if("update".equals(submit)) {//タグ記事更新
    		String sqlUpdate = "UPDATE tags SET tagText = '" + tag1 + "' WHERE tag_id = " +id1;
     		jdbc.update(sqlUpdate);

    	}else if("delete".equals(submit)) {//タグ削除
    		String sqlDelete =  "DELETE FROM tags WHERE tag_id = " + id1 ;
    	    jdbc.update(sqlDelete);

    	}
    }


}