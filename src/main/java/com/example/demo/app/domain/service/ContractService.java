package com.example.demo.app.domain.service;

import com.example.demo.app.domain.model.dto.ContractRequest;
import com.example.demo.app.domain.model.dto.ContractResponse;
import com.example.demo.app.domain.model.dto.ItemDto;
import com.example.demo.app.domain.model.dto.ItemListDto;
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

        Optional<ContractEntity> isContractExists =
                contractRepository.findAllByItem_InformationAndRepresentative_UsernameAndUser_Userid(
                        contractRequest.getItemId(),
                        contractRequest.getRepresentativeId(),
                        contractRequest.getUserId()
                );

        if (isUserExists.isEmpty() || isRepresentativeExists.isEmpty() || isItemExists.isEmpty()) {
            return ContractResponse.builder()
                    .isSuccess(false)
                    .message("계약에 실패하였습니다.")
                    .build();
        }

        if (isContractExists.isPresent()) {
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

    public ItemListDto findItem() {
        List<ContractEntity> listItem = contractRepository.findAllWithItem();

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
}
