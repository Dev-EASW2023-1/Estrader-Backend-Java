package kr.easw.estrader.app.domain.controller;

import kr.easw.estrader.app.domain.model.dto.item.ItemDto;
import kr.easw.estrader.app.domain.model.dto.item.ItemListDto;
import kr.easw.estrader.app.domain.model.dto.item.LookUpItemRequest;
import kr.easw.estrader.app.domain.model.dto.item.MapDto;
import kr.easw.estrader.app.domain.model.entity.ItemEntity;
import kr.easw.estrader.app.domain.repository.ItemRepository;
import kr.easw.estrader.app.domain.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemRepository itemRepository;
    private final ItemService itemService;

    // 아이템 파라미터 입력 페이지 출력
    @GetMapping("")
    public String inputPage(){
        return "item/index";
    }

    // 아이템 저장
    @PostMapping("/add")
    public String addItem(
            @RequestParam String caseNumber,
            @RequestParam String court,
            @RequestParam String location,
            @RequestParam String minimumBidPrice,
            @RequestParam String photo,
            @RequestParam String biddingPeriod,
            @RequestParam String itemType,
            @RequestParam String note,
            @RequestParam String managementNumber,
            @RequestParam String xcoordinate,
            @RequestParam String ycoordinate,
            @RequestParam String district

    ) {
        itemService.addItem(
                caseNumber,
                court,
                location,
                minimumBidPrice,
                photo,
                biddingPeriod,
                itemType,
                note,
                managementNumber,
                xcoordinate,
                ycoordinate,
                district
        );
        return "redirect:/item/list";
    }


    // 아이템 리스트 출력
    @GetMapping("/list")
    public String showItemListForModel(
            Model model
    ) {
        List<ItemEntity> itemList = itemRepository.findAll();
        model.addAttribute("itemList", itemList);

        return "item/list";
    }
    @GetMapping("/naver-map")
    public String showNaverMap(
            Model model,
            @AuthenticationPrincipal User user
    ) {
        System.out.println("네이버 지도 확인하는 유저  \n" + user);
        return "item/navermap";
    }

    @GetMapping("/show-list")
    public ResponseEntity<ItemListDto> showItemList(
            @RequestParam String district,
            @RequestParam int page,
            @RequestParam int size) {

        MapDto mapDto = MapDto.withDistrict(district);
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(itemService.findItemList(mapDto, page, size));
    }




    @PostMapping("/show")
    public ResponseEntity<ItemDto> showItem(@RequestBody LookUpItemRequest lookUpItemRequest) {
        return ResponseEntity.ok()
                .headers(getJsonHeader())
                .body(itemService.lookUpItem(lookUpItemRequest));
    }

    private HttpHeaders getJsonHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return headers;
    }
}

