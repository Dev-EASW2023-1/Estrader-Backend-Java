package kr.easw.estrader.app.domain.model.dto.item;

import kr.easw.estrader.app.domain.model.entity.ItemEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemDto {

    @NotBlank
    private String caseNumber;

    @NotBlank
    private String court;

    @NotBlank
    private String location;

    @NotBlank
    private String minimumBidPrice;

    @NotBlank
    private String photo;

    @NotBlank
    private String biddingPeriod;

    @NotBlank
    private String itemType;

    @NotBlank
    private String note;

    @NotBlank
    private String managementNumber;

    @NotBlank
    private String xcoordinate;

    @NotBlank
    private String ycoordinate;

    @NotBlank
    private String district;

    @Builder
    public ItemDto(String caseNumber, String court, String location, String minimumBidPrice, String photo, String biddingPeriod, String itemType, String note, String managementNumber, String xcoordinate, String ycoordinate, String district) {
        this.caseNumber = caseNumber;
        this.court = court;
        this.location = location;
        this.minimumBidPrice = minimumBidPrice;
        this.photo = photo;
        this.biddingPeriod = biddingPeriod;
        this.itemType = itemType;
        this.note = note;
        this.managementNumber = managementNumber;
        this.xcoordinate = xcoordinate;
        this.ycoordinate = ycoordinate;
        this.district = district;
    }

    public ItemDto(ItemEntity item) {
        this.caseNumber = item.getCaseNumber();
        this.court = item.getCourt();
        this.location = item.getLocation();
        this.minimumBidPrice = item.getMinimumBidPrice();
        this.photo = item.getPhoto();
        this.biddingPeriod = item.getBiddingPeriod();
        this.itemType = item.getItemType();
        this.note = item.getNote();
        this.managementNumber = item.getManagementNumber();
        this.xcoordinate = item.getXcoordinate();
        this.ycoordinate = item.getYcoordinate();
        this.district = item.getDistrict();
    }

    public static ItemDto of(ItemEntity item) {
        return new ItemDto(item);
    }

    public ItemEntity toEntity() {
        return ItemEntity.builder()
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
    }
}