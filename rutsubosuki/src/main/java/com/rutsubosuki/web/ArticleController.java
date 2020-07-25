package com.rutsubosuki.web;


import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ArticleController {
    @Autowired
    private JdbcTemplate jdbc;
    @InitBinder //フォーム未入力のStringをNULLにするためのメソッド
    public void initbinder(WebDataBinder binder){
           binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping("/article") //list.htmlに記事一覧を返し、アドレス/articleで表示
    private ModelAndView list(ModelAndView mav) {
    	String sql = "select articles.article_id, articles.title, uploads1.image_name as articleimage1, uploads2.image_name as articleimage2, uploads3.image_name as articleimage3, uploads4.image_name as articleimage4 from articles left outer join uploads as uploads1 on articles.articleImage1 = uploads1.image_id left outer join uploads as uploads2 on articles.articleImage2 = uploads2.image_id left outer join uploads as uploads3 on articles.articleImage3 = uploads3.image_id left outer join uploads as uploads4 on articles.articleImage4 = uploads4.image_id";

        List<Map<String, Object>> articles =jdbc.queryForList(sql);
        mav.addObject("articles", articles);
        mav.setViewName("article/list");

        return mav;
    }

    @RequestMapping("/article/{article_id}")
    private ModelAndView detail(
        	@PathVariable("article_id") Integer article_id,ModelAndView mav) { //アドレス"article_id"を変数として扱う、
        	String sql = "SELECT * FROM articles WHERE article_id = ?"; //DBのarticleテーブルから、対応したIDの全情報を検索

        	//記事内容をarticleに全部投げ込むjdbc.queryForMapでテーブル内の1行分データを取得
        	Map<String, Object> article = jdbc.queryForMap(sql, new Object[]{article_id});

        	//上記sqlで取得したtag1~tag7で取得したタグIDで検索する
        	for(int i= 1; i < 8; i++){ //7週ループ
        		String tagtag = "tag" + i ; //"tag1~7"のString変数を作成
        		Object tagNumber = article.get(tagtag);
        		if(tagNumber != null) {
        			int tagNum =  (int) tagNumber; //articleから取得したタグナンバーを入れる
        			if(tagNum > 0) { //タグのIDは1始まりなので0は除外
        				String sqlTag = "SELECT tagText FROM tags WHERE tag_id = " + tagNum; //タグナンバーに対応したIDのタグを検索
        				String tagContent = jdbc.queryForObject(sqlTag, String.class); //変数tagContentにSQLの結果を取得
        				article.put(tagtag, tagContent); //articleMAPのタグ項目を上書きしていく
        			}
        		}
        	}

        	//DBテーブルarticlesから得た画像対応番号を取得、対応した画像のタイトルを取得しMapに上書き
        	for(int j= 1; j < 5; j++){ //4週ループ
        		String imagetag = "articleImage" + j ; //"articleImage1~4"のString変数を作成
        		Object imageNumber = article.get(imagetag); //articleimage1~4に格納された値を取得
        		if(imageNumber != null) { //nullチェック
        			int imageNum = (int) imageNumber;
        			if(imageNum > 0) {
        				String sqlImage = "SELECT image_name FROM uploads WHERE image_id = " + imageNum; //記事の画像ナンバーに対応したIDの画像名を検索
        				String imageContent = jdbc.queryForObject(sqlImage, String.class); //変数imageContentにSQLの結果を取得
        				article.put(imagetag, imageContent); //articleMAPの画像項目を上書きしていく
        			}
        		}
        	}

    	mav.addObject("article", article); //.addObjectでViewへの戻り値を設定（"変数",オブジェクト）
    	mav.setViewName("article/detail"); //.setViewNameで送り先指定、article/detailに戻す

    	return mav;

    }

    //記事編集画面,フォームから情報を受け取り記事変更・追加
    @RequestMapping(value = "/article/articleEdit")
    public void articleEdit(
    		@RequestParam (name = "action", required = false) String submit,//submitボタンのValueを受け取る
    		@RequestParam (name = "articleid", required = false) String articleID,//@RequestParamでフォームからの値を受け取る
    	    @RequestParam(name = "title", required = false) String title,
    	    @NotNull@RequestParam(name = "text", required = false) String text,//＠NotNullで未入力欄をNULL入力に変更
    	    @NotNull@RequestParam(name = "tag1", required = false) String tag1,
    	    @NotNull@RequestParam(name = "tag2", required = false) String tag2,
    	    @NotNull@RequestParam(name = "tag3", required = false) String tag3,
    	    @NotNull@RequestParam(name = "tag4", required = false) String tag4,
    	    @NotNull@RequestParam(name = "tag5", required = false) String tag5,
    	    @NotNull@RequestParam(name = "tag6", required = false) String tag6,
    	    @NotNull@RequestParam(name = "tag7", required = false) String tag7,
    	    @NotNull@RequestParam(name = "image1", required = false) String image1,
    	    @NotNull@RequestParam(name = "image2", required = false) String image2,
    	    @NotNull@RequestParam(name = "image3", required = false) String image3,
    	    @NotNull@RequestParam(name = "image4", required = false) String image4
    	    ) {
    	//記事追加、更新、削除処理
    	if("addition".equals(submit)) {//記事追加
    		String sqlAdd =  "INSERT INTO articles (title,text,tag1,tag2,tag3,tag4,"
     	 	+ "tag5,tag6,tag7,articleImage1,articleImage2,articleImage3,articleImage4)"
     	 	+ "VALUES('" + title + "','" + text + "'," + tag1 + "," + tag2 + ","
     	 	+ tag3 + "," + tag4 + "," + tag5 + "," + tag6 + "," + tag7 + ","
     	 	+ image1 + "," + image2 + "," + image3 + "," + image4 + ")";
     		jdbc.update(sqlAdd);

    	}else if("update".equals(submit)) {//記事更新
    		String sqlUpdate = "UPDATE articles SET title = '" + title + "', text = '" + text +
    		"' , tag1 = " + tag1 + ", tag2 = " + tag2 + ", tag3 = " + tag3 +
    		", tag4 = " + tag4 + ", tag5 = " + tag5 + ", tag6 = " + tag6 + ", tag7 = " + tag7 +
    		", articleImage1 = " + image1 + ", articleImage2 = " + image2 +
    		", articleImage3 = " + image3 + ", articleImage4 = " + image4 +
    		 " WHERE article_id = " + articleID ;
    		jdbc.update(sqlUpdate);

    	}else if("delete".equals(submit)) {//記事削除
    		String sqlDelete =  "DELETE FROM articles WHERE article_id = " + articleID ;
    	    jdbc.update(sqlDelete);

    	}
    }
}