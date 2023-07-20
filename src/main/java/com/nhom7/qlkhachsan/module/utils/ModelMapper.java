package com.nhom7.qlkhachsan.module.utils;

import com.nhom7.qlkhachsan.app.entity.dto.HotelDTO;
import com.nhom7.qlkhachsan.app.entity.hotel.Hotel;

import java.util.ArrayList;
import java.util.List;

public class ModelMapper {
    public static List<HotelDTO> hotelMapper(List<Hotel> hotels) {
        List<HotelDTO> hotelDTOList = new ArrayList<>();
        for(Hotel hotel: hotels){
            HotelDTO h = new HotelDTO();
            h.setId(hotel.getId());
            h.setName(hotel.getName());
            h.setAddress(hotel.getAddress());
            h.setDescription(hotel.getDescription());
            h.setImagePath(hotel.getImagePath());
            h.setOwner_id(hotel.getOwner().getId());
            hotelDTOList.add(h);
        }
        return hotelDTOList;
    }
    public static HotelDTO hotelMapper1(Hotel hotel) {
        HotelDTO h = new HotelDTO();
        h.setId(hotel.getId());
        h.setName(hotel.getName());
        h.setAddress(hotel.getAddress());
        h.setDescription(hotel.getDescription());
        h.setImagePath(hotel.getImagePath());
        h.setOwner_id(hotel.getOwner().getId());
        return h;
    }

}
