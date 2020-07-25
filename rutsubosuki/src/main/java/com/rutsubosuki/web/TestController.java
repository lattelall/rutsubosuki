package com.rutsubosuki.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {


    @RequestMapping("/test") //タグ一覧ページ、/tag
    public String test(){
    	return "test";
    }

    @RequestMapping("/testBootstrap") //タグ一覧ページ、/tag
    public String testBootstrap(){
    	return "testBootstrap";
    }


}