package com.example.demo.app.domain.service;

import com.example.demo.app.domain.exception.exceptions.ContractFailureException;
import com.example.demo.app.domain.model.dto.contract.*;
import com.example.demo.app.domain.model.dto.error.ErrorCode;
import com.example.demo.app.domain.model.dto.item.ItemDto;
import com.example.demo.app.domain.model.dto.item.ItemListDto;
import com.example.demo.app.domain.model.entity.ContractEntity;
import com.example.demo.app.domain.model.entity.ItemEntity;
import com.example.demo.app.domain.model.entity.UserEntity;
import com.example.demo.app.domain.model.util.PDFUtil;
import com.example.demo.app.domain.repository.ContractRepository;
import com.example.demo.app.domain.repository.ItemRepository;
import com.example.demo.app.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ContractRepository contractRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final PDFUtil pdfUtil;

    @Transactional
    public ContractResponse registerContract(ContractRequest contractRequest) {

        UserEntity isUserExists =
                userRepository.findByUserId(contractRequest.getUserId())
                        .orElseThrow(() -> new ContractFailureException(ErrorCode.CONTRACT_FAILURE));
        UserEntity isRealtorExists =
                userRepository.findByUserId(contractRequest.getRealtorId())
                        .orElseThrow(() -> new ContractFailureException(ErrorCode.CONTRACT_FAILURE));
        ItemEntity isItemExists =
                itemRepository.findByPhoto(contractRequest.getItemId())
                        .orElseThrow(() -> new ContractFailureException(ErrorCode.CONTRACT_FAILURE));

        Optional<ContractEntity> isContractExists =
                contractRepository.findItemByThreeParams(
                        contractRequest.getUserId(),
                        contractRequest.getRealtorId(),
                        contractRequest.getItemId()
                );

        send(isRealtorExists.getFcmToken(), contractRequest);

        log.info("전송 from (구매자) {} to (대리인) {}", contractRequest.getUserId(), contractRequest.getRealtorId());

        if (isContractExists.isPresent()) {
            return ContractResponse.builder()
                    .isSuccess(false)
                    .message("이미 계약이 존재합니다.")
                    .name(isRealtorExists.getName())
                    .build();
        }

        contractRepository.save(
                ContractEntity.builder()
                        .user(isUserExists)
                        .item(isItemExists)
                        .realtor(isRealtorExists)
                        .build()
        );

        log.info("계약 성공");

        return ContractResponse.builder()
                .isSuccess(true)
                .message("계약에 성공하였습니다.")
                .name(isRealtorExists.getName())
                .build();
    }

    private void send(String targetToken, ContractRequest contractRequest) {
        try {
            firebaseCloudMessageService.sendMessageTo(
                    targetToken,
                    contractRequest
            );
        } catch (IOException e) {
            e.printStackTrace();
            log.error("전송 실패");
            throw new ContractFailureException(ErrorCode.CONTRACT_FAILURE);
        }
    }

    public ItemDto findItemByContract(ItemInContractDto itemInContractDto) {
        ContractEntity contract = contractRepository.findItemByThreeParams(
                itemInContractDto.getUserId(),
                itemInContractDto.getRealtorId(),
                itemInContractDto.getItemId()
        ).orElseThrow(() -> new ContractFailureException(ErrorCode.CONTRACT_FAILURE));

        return ItemDto.of(contract.getItem());
    }

    // Contract 테이블 JPQL @Query 사용, Fetch Join 으로 N+1 문제 방지
    public ItemListDto ContractTest1(String userId, String realtorId, String caseNumber) {
        List<ContractEntity> listContract = contractRepository.findAllByUserIdAndRealtorIdAndCaseNumber(
                userId,
                realtorId,
                caseNumber
        );

        List<ItemDto> listItemDto = listContract.stream()
                .map(m -> ItemDto.of(m.getItem()))
                .collect(Collectors.toList());

        return ItemListDto.builder()
                .itemDto(listItemDto)
                .build();
    }

    // Contract 테이블 쿼리 메소드 사용
    public ItemDto ContractTest2(String userId, String realtorId, String caseNumber) {
        ContractEntity test = contractRepository.findAllByItem_CaseNumberAndRealtor_UserIdAndUser_UserId(
                caseNumber,
                realtorId,
                userId
        ).orElseThrow(() -> new ContractFailureException(ErrorCode.CONTRACT_FAILURE));

        return ItemDto.of(test.getItem());
    }

    public ContractInfoResponse findContractInfo(ContractInfoRequest contractInfoRequest){
        ContractEntity contract = contractRepository.findItemByThreeParams(
                contractInfoRequest.getUserId(),
                contractInfoRequest.getRealtorId(),
                contractInfoRequest.getItemId()
        ).orElseThrow(() -> new ContractFailureException(ErrorCode.CONTRACT_FAILURE));

        return ContractInfoResponse.of(contract.getItem());
    }

    public Resource createPdfResource(){
        return pdfUtil.createPdf();
    }
}
