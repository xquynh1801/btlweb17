package com.nhom7.qlkhachsan.app.middleware.gmail;

public class GmailForm {
    public static String mailForm(String name, int code){
        String res="<meta charset=\"UTF-8\">";
        res+="<h2> Xin chào "+name+"</h2>";
        res+="<h4>Chúng tôi rất vui khi bạn đến với trang web của chúng tôi</h4>";
        res+="<h4>Mã số xác thực là <b style=\"color:red\">"+code+"</b></h4>";
        res+="<h4>Xin cảm ơn!</h4>";
        return res;
    }
}
