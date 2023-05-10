package com.nhom7.qlkhachsan.dto;

import java.util.Date;

public interface UserBookingDTO {
    Long getId();
    String getName();
    String getIdentify_card_number();
    String getPhoneNumber();
    String getRoomName();
    Double getPrice();
    Date getTimeBegin();
    Date getTimeEnd();
}
