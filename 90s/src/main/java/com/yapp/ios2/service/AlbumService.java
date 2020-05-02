package com.yapp.ios2.service;

import com.yapp.ios2.dto.AlbumDto;
import com.yapp.ios2.repository.*;
import com.yapp.ios2.vo.Album;
import com.yapp.ios2.vo.AlbumOrder;
import com.yapp.ios2.vo.AlbumOrderPaperType;
import com.yapp.ios2.vo.AlbumOwner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlbumService{

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    AlbumOrderRepository albumOrderRepository;

    @Autowired
    AlbumOrderPaperTypeRepository albumOrderPaperTypeRepository;

    @Autowired
    AlbumOwnerRepository albumOwnerRepository;

    @Autowired
    AlbumOrderPostTypeRepository albumOrderPostTypeRepository;

    @Autowired
    AlbumOrderStatusRepository albumOrderStatusRepository;

    public Album create(String name, Integer photoLimit, Long user, Long layoutUid, LocalDate endDate) {

        Album newAlbum = Album.builder()
                .name(name)
                .photoLimit(photoLimit)
                .layoutUid(layoutUid)
                .endDate(endDate)
                .count(0)
                .build();

        albumRepository.save(newAlbum);

        addOwner(newAlbum.getUid(), user, "ROLE_CREATOR");

        return newAlbum;
    }

    public AlbumOwner addOwner(Long albumUid, Long user, String role){

        AlbumOwner albumOwner = AlbumOwner.builder()
                .albumUid(albumUid)
                .userUid(user)
                .role(role)
                .build();

        albumOwnerRepository.save(albumOwner);

        return albumOwner;
    }

    public List<Album> getAlbums(Long userUid) {

        List<AlbumOwner> albumOwners = albumOwnerRepository.findByUserUid(userUid);
        List<Album> albums = new ArrayList<>();

        for(AlbumOwner owner : albumOwners){
            albums.add(
                    albumRepository.findById(owner.getAlbumUid()).get()
            );
        }

        return albums;
    }

    public AlbumOrder createAlbumOrder(AlbumDto.AlbumOrderInfo albumOrderInfo){

        AlbumOrder newAlbumOrder = AlbumOrder.builder()
                .cost(albumOrderInfo.getCost())
                .recipient(albumOrderInfo.getRecipient())
                .postalCode(albumOrderInfo.getPostalCode())
                .address(albumOrderInfo.getAddress())
                .addressDetail(albumOrderInfo.getAddressDetail())
                .phoneNum(albumOrderInfo.getPhoneNum())
                .message(albumOrderInfo.getMessage())
                .paperType(albumOrderPaperTypeRepository.findById(albumOrderInfo.getPaperType()).get())
                .postType(albumOrderPostTypeRepository.findById(albumOrderInfo.getPostType()).get())
                .build();

        albumOrderRepository.save(newAlbumOrder);

        return newAlbumOrder;
    }

    public void changeAlbumOrderStatus(Long albumUid, boolean status){

        AlbumOrder albumOrder = albumOrderRepository.findByAlbum(albumRepository.findById(albumUid).get())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        if(status){
//            forward
            albumOrder.setStatus(
                    albumOrderStatusRepository.findById(
                            albumOrder.getStatus().getUid() + 1
                    ).orElseThrow(() -> new IllegalArgumentException("이미 완료된 주문건입니다."))
            );

        }else{
//            backward
            albumOrder.setStatus(
                    albumOrderStatusRepository.findById(
                            albumOrder.getStatus().getUid() - 1
                    ).orElseThrow(() -> new IllegalArgumentException("이미 입금 대기 주문건입니다."))
            );
        }



    }
}
