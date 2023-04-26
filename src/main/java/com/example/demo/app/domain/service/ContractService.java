package com.example.demo.app.domain.service;

import com.example.demo.app.domain.model.dto.contract.*;
import com.example.demo.app.domain.model.dto.item.ItemDto;
import com.example.demo.app.domain.model.dto.item.ItemListDto;
import com.example.demo.app.domain.model.entity.ContractEntity;
import com.example.demo.app.domain.model.entity.ItemEntity;
import com.example.demo.app.domain.model.entity.RealtorEntity;
import com.example.demo.app.domain.model.entity.UserEntity;
import com.example.demo.app.domain.repository.ContractRepository;
import com.example.demo.app.domain.repository.ItemRepository;
import com.example.demo.app.domain.repository.RealtorRepository;
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
    private final RealtorRepository realtorRepository;
    private final ItemRepository itemRepository;
    private final ContractRepository contractRepository;

    @Transactional
    public ContractResponse registerContract(ContractRequest contractRequest) {

        Optional<UserEntity> isUserExists =
                userRepository.findByUserId(contractRequest.getUserId());

        Optional<RealtorEntity> isRealtorExists =
                realtorRepository.findByRealtorId(contractRequest.getRealtorId());

        Optional<ItemEntity> isItemExists =
                itemRepository.findByPhoto(contractRequest.getItemId());

        Optional<ContractEntity> isContractExists =
                contractRepository.findItemByThreeParams(
                        contractRequest.getUserId(),
                        contractRequest.getRealtorId(),
                        contractRequest.getItemId()
                );

        System.out.println("URL은 잘 왔나요~~?" + contractRequest.getItemId());

        if (isUserExists.isEmpty() || isRealtorExists.isEmpty() || isItemExists.isEmpty()) {
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
                        .realtor(isRealtorExists.get())
                        .build()
        );

        return ContractResponse.builder()
                .isSuccess(true)
                .message("계약에 성공하였습니다.")
                .build();
    }

    public ItemDto findItemByContract(ItemInContractDto itemInContractDto) {
        Optional<ContractEntity> contract = contractRepository.findItemByThreeParams(
                itemInContractDto.getUserId(),
                itemInContractDto.getRealtorId(),
                itemInContractDto.getItemId()
        );

        if(contract.isEmpty()){
            throw new IllegalArgumentException();
        }

        return ItemDto.builder()
                .caseNumber(contract.get().getItem().getCaseNumber())
                .court(contract.get().getItem().getCourt())
                .location(contract.get().getItem().getLocation())
                .minimumBidPrice(contract.get().getItem().getMinimumBidPrice())
                .photo(contract.get().getItem().getPhoto())
                .biddingPeriod(contract.get().getItem().getBiddingPeriod())
                .itemType(contract.get().getItem().getItemType())
                .note(contract.get().getItem().getNote())
                .managementNumber(contract.get().getItem().getManagementNumber())
                .build();
    }

    // Contract 테이블 JPQL @Query 사용, Fetch Join 으로 N+1 문제 방지
    public ItemListDto ContractTest1(String userId, String realtorId, String caseNumber) {
        List<ContractEntity> listContract = contractRepository.findAllByUserIdAndRealtorIdAndCaseNumber(
                userId,
                realtorId,
                caseNumber
        );

        List<ItemDto> listItemDto = listContract.stream()
                .map(m -> new ItemDto(
                        m.getItem().getCaseNumber(),
                        m.getItem().getCourt(),
                        m.getItem().getLocation(),
                        m.getItem().getMinimumBidPrice(),
                        m.getItem().getPhoto(),
                        m.getItem().getBiddingPeriod(),
                        m.getItem().getItemType(),
                        m.getItem().getNote(),
                        m.getItem().getManagementNumber()
                ))
                .collect(Collectors.toList());

        return ItemListDto.builder()
                .itemDto(listItemDto)
                .build();
    }

    // Contract 테이블 쿼리 메소드 사용
    public ItemDto ContractTest2(String userId, String realtorId, String caseNumber) {
        Optional<ContractEntity> test = contractRepository.findAllByItem_CaseNumberAndRealtor_RealtorIdAndUser_UserId(
                caseNumber,
                realtorId,
                userId
        );

        if (test.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return ItemDto.builder()
                .caseNumber(test.get().getItem().getCaseNumber())
                .court(test.get().getItem().getCourt())
                .location(test.get().getItem().getLocation())
                .minimumBidPrice(test.get().getItem().getMinimumBidPrice())
                .photo(test.get().getItem().getPhoto())
                .biddingPeriod(test.get().getItem().getBiddingPeriod())
                .itemType(test.get().getItem().getItemType())
                .note(test.get().getItem().getNote())
                .managementNumber(test.get().getItem().getManagementNumber())
                .build();
    }

    public ContractInfoResponse findContractInfo(ContractInfoRequest contractInfoRequest){
        Optional<ContractEntity> contract = contractRepository.findItemByThreeParams(
                contractInfoRequest.getUserId(),
                contractInfoRequest.getRealtorId(),
                contractInfoRequest.getItemId()
        );

        System.out.println("아이디는 잘 온 건가요? " + contractInfoRequest.getUserId());

        if(contract.isEmpty()){
            throw new IllegalArgumentException();
        }

        return ContractInfoResponse.builder()
                .caseNumber(contract.get().getItem().getCaseNumber())
                .biddingPeriod(contract.get().getItem().getBiddingPeriod())
                .minimumBidPrice(contract.get().getItem().getMinimumBidPrice())
                .managementNumber(contract.get().getItem().getManagementNumber())
                .build();
    }
}
