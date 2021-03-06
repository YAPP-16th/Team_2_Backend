package com.yapp.ios2.service;

import com.yapp.ios2.config.exception.AlbumNotFoundException;
import com.yapp.ios2.dto.AlbumDto;
import com.yapp.ios2.repository.*;
import com.yapp.ios2.vo.Album;
import com.yapp.ios2.vo.AlbumOrder;
import com.yapp.ios2.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class AlbumOrderService {

    @Autowired
    AlbumOrderRepository albumOrderRepository;
    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    AlbumOrderStatusRepository albumOrderStatusRepository;
    @Autowired
    AlbumOrderPaperType1Repository albumOrderPaperType1Repository;
    @Autowired
    AlbumOrderPaperType2Repository albumOrderPaperType2Repository;
    @Autowired
    AlbumOrderPostTypeRepository albumOrderPostTypeRepository;

    public AlbumOrder createAlbumOrder(AlbumDto.AlbumOrderInfo albumOrderInfo, User user){

        Album album = albumRepository.findById(albumOrderInfo.getAlbumUid()).get();

        AlbumOrder newAlbumOrder = AlbumOrder.builder()
                .album(album)
                .user(user)
                .amount(albumOrderInfo.getAmount())
                .orderCode(UUID.randomUUID().toString())
                .cost(albumOrderInfo.getCost())
                .recipient(albumOrderInfo.getRecipient())
                .postalCode(albumOrderInfo.getPostalCode())
                .address(albumOrderInfo.getAddress())
                .addressDetail(albumOrderInfo.getAddressDetail())
                .phoneNum(albumOrderInfo.getPhoneNum())
                .message(albumOrderInfo.getMessage())
                .paperType1(albumOrderPaperType1Repository.findById(albumOrderInfo.getPaperType1()).get())
                .paperType2(albumOrderPaperType2Repository.findById(albumOrderInfo.getPaperType2()).get())
                .postType(albumOrderPostTypeRepository.findById(albumOrderInfo.getPostType()).get())
                .build();

        albumOrderRepository.save(newAlbumOrder);

        album.setOrderStatus(albumOrderStatusRepository.findById(2L).get());

        albumRepository.save(album);

        return newAlbumOrder;
    }

    public void deleteAlbumOrder(Long albumOrderUid){
        AlbumOrder albumOrder = albumOrderRepository.findById(albumOrderUid).orElseThrow(
                () -> new AlbumNotFoundException("없는 엘범 인데!")
        );

        Album album = albumOrder.getAlbum();
        album.setOrderStatus(albumOrderStatusRepository.findById(1L).get());
        albumRepository.save(album);

    }

    public void changeAlbumOrderStatus(Long albumUid, boolean status) {

        AlbumOrder albumOrder = albumOrderRepository.findByAlbum(albumRepository.findById(albumUid).get())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        if (status) {
//            forward
            albumOrder.setStatus(
                    albumOrderStatusRepository.findById(
                            albumOrder.getStatus().getUid() + 1
                    ).orElseThrow(() -> new IllegalArgumentException("이미 완료된 주문건입니다."))
            );

        } else {
//            backward
            albumOrder.setStatus(
                    albumOrderStatusRepository.findById(
                            albumOrder.getStatus().getUid() - 1
                    ).orElseThrow(() -> new IllegalArgumentException("이미 입금 대기 주문건입니다."))
            );
        }
    }
}
