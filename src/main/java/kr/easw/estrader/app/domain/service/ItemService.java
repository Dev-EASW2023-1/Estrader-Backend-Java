package kr.easw.estrader.app.domain.service;

import kr.easw.estrader.app.domain.exception.exceptions.NotFoundException;
import kr.easw.estrader.app.domain.model.dto.error.ErrorCode;
import kr.easw.estrader.app.domain.model.dto.item.ItemDto;
import kr.easw.estrader.app.domain.model.dto.item.ItemListDto;
import kr.easw.estrader.app.domain.model.dto.item.ItemPageRequestDTO;
import kr.easw.estrader.app.domain.model.dto.item.LookUpItemRequest;
import kr.easw.estrader.app.domain.model.entity.ItemEntity;
import kr.easw.estrader.app.domain.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    public void addItem(
            String caseNumber,
            String court,
            String location,
            String minimumBidPrice,
            String photo,
            String biddingPeriod,
            String itemType,
            String note,
            String managementNumber,
            String xcoordinate,
            String ycoordinate,
            String district
    ) {
        ItemEntity item = ItemEntity.builder()
                .caseNumber(caseNumber)
                .court(court)
                .location(location)
                .minimumBidPrice(minimumBidPrice)
                .photo(photo)
                .biddingPeriod(biddingPeriod)
                .itemType(itemType)
                .note(note)
                .managementNumber(managementNumber)
                .xcoordinate(xcoordinate)
                .ycoordinate(ycoordinate)
                .district(district)
                .build();
        itemRepository.save(item);
    }

    public ItemListDto findItemList(ItemPageRequestDTO itemPageRequestDTO) {
        Pageable pageable = PageRequest.of(
                itemPageRequestDTO.getPage(),
                itemPageRequestDTO.getSize(),
                Sort.by("id").descending()
        );

        System.out.println("페이지 출력 " + pageable);

        Page<ItemEntity> pagedResult
                = itemRepository.findAll(pageable);

        List<ItemDto> listItemDto = pagedResult.getContent().stream()
                .map(ItemDto::of)
                .collect(Collectors.toList());

        return ItemListDto.builder()
                .itemDto(listItemDto)
                .build();
    }

    public ItemListDto findItemList() {
        List<ItemDto> listItemDto = itemRepository.findAll().stream()
                .map(ItemDto::of)
                .collect(Collectors.toList());

        return ItemListDto.builder()
                .itemDto(listItemDto)
                .build();
    }

    public ItemDto lookUpItem(LookUpItemRequest lookUpItemRequest) {
        ItemEntity isItemExists =
                itemRepository.findByPhoto(lookUpItemRequest.getPhoto())
                        .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        return ItemDto.of(isItemExists);
    }
}
