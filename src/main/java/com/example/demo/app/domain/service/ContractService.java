package com.example.demo.app.domain.service;

import com.example.demo.app.domain.model.dto.*;
import com.example.demo.app.domain.model.entity.ContractEntity;
import com.example.demo.app.domain.model.entity.ItemEntity;
import com.example.demo.app.domain.model.entity.RepresentativeEntity;
import com.example.demo.app.domain.model.entity.UserEntity;
import com.example.demo.app.domain.repository.ContractRepository;
import com.example.demo.app.domain.repository.ItemRepository;
import com.example.demo.app.domain.repository.RepresentativeRepository;
import com.example.demo.app.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final UserRepository userRepository;
    private final RepresentativeRepository representativeRepository;
    private final ItemRepository itemRepository;
    private final ContractRepository contractRepository;

    @Transactional
    public ContractResponse registerContract(ContractRequest contractRequest) {

        Optional<UserEntity> isUserExists =
                userRepository.findByUserid(contractRequest.getUserId());

        Optional<RepresentativeEntity> isRepresentativeExists =
                representativeRepository.findByUsername(contractRequest.getRepresentativeId());

        Optional<ItemEntity> isItemExists =
                itemRepository.findByPicture(contractRequest.getItemId());

        List<ContractEntity> isContractExists =
                contractRepository.findItemByThreeParams(
                        contractRequest.getUserId(),
                        contractRequest.getRepresentativeId(),
                        contractRequest.getItemId()
                );

        System.out.println("URL은 잘 왔나요~~?" + contractRequest.getItemId());

        if (isUserExists.isEmpty() || isRepresentativeExists.isEmpty() || isItemExists.isEmpty()) {
            return ContractResponse.builder()
                    .isSuccess(false)
                    .message("계약에 실패하였습니다.")
                    .build();
        }

        if (!isContractExists.isEmpty()) {
            return ContractResponse.builder()
                    .isSuccess(false)
                    .message("이미 계약이 존재합니다.")
                    .build();
        }

        contractRepository.save(
                ContractEntity.builder()
                        .user(isUserExists.get())
                        .item(isItemExists.get())
                        .representative(isRepresentativeExists.get())
                        .build()
        );

        return ContractResponse.builder()
                .isSuccess(true)
                .message("계약에 성공하였습니다.")
                .build();
    }

    public ItemListDto findItemByContract(ItemInContractDto itemInContractDto) {
        List<ContractEntity> listItem = contractRepository.findItemByThreeParams(
                itemInContractDto.getUserId(),
                itemInContractDto.getRepresentativeId(),
                itemInContractDto.getItemId()
        );

        List<ItemDto> listItemDto = listItem.stream()
                .map(m -> new ItemDto(
                        m.getItem().getPicture(),
                        m.getItem().getInformation(),
                        m.getItem().getPeriod(),
                        m.getItem().getLocation(),
                        m.getItem().getReserveprice(),
                        m.getItem().getAuctionperiod()
                ))
                .collect(Collectors.toList());

        return ItemListDto.builder()
                .itemDto(listItemDto)
                .build();
    }

    // Contract 테이블 JPQL @Query 사용, Fetch Join 으로 N+1 문제 방지
    public ItemListDto ContractTest1(String userId, String representativeId, String itemId) {
        List<ContractEntity> listContract = contractRepository.findAllByLocationAndRepresentativeAndUserId(
                userId,
                representativeId,
                itemId
        );

        List<ItemDto> listItemDto = listContract.stream()
                .map(m -> new ItemDto(
                        m.getItem().getPicture(),
                        m.getItem().getInformation(),
                        m.getItem().getPeriod(),
                        m.getItem().getLocation(),
                        m.getItem().getReserveprice(),
                        m.getItem().getAuctionperiod()
                ))
                .collect(Collectors.toList());

        return ItemListDto.builder()
                .itemDto(listItemDto)
                .build();
    }

    // Contract 테이블 쿼리 메소드 사용
    public ItemDto ContractTest2(String userId, String representativeId, String itemId) {
        Optional<ContractEntity> test = contractRepository.findAllByItem_InformationAndRepresentative_UsernameAndUser_Userid(
                itemId,
                representativeId,
                userId
        );

        if (test.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return ItemDto.builder()
                .picture(test.get().getItem().getPicture())
                .information(test.get().getItem().getInformation())
                .period(test.get().getItem().getPeriod())
                .location(test.get().getItem().getLocation())
                .reserveprice(test.get().getItem().getReserveprice())
                .auctionperiod(test.get().getItem().getAuctionperiod())
                .build();
    }
}
