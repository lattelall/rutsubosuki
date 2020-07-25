package com.rutsubosuki.web.form;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserForm implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 3, max = 64, message = "アカウントは、3文字以上64文字以下で入力してください。")
    private String username;

    @NotNull
    @Size(min = 6, max = 128, message = "パスワードは、6文字以上128文字以下で入力してください。")
    private String password;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}