package kr.easw.estrader.app.domain.model.dto.contract;

import kr.easw.estrader.app.domain.model.entity.ItemEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ContractInfoResponse {

    @NotBlank
    private String biddingPeriod;

    @NotBlank
    private String caseNumber;

    @NotBlank
    private String minimumBidPrice;

    @NotBlank
    private String managementNumber;

    @Builder
    public ContractInfoResponse(String biddingPeriod, String caseNumber, String minimumBidPrice, String managementNumber) {
        this.biddingPeriod = biddingPeriod;
        this.caseNumber = caseNumber;
        this.minimumBidPrice = minimumBidPrice;
        this.managementNumber = managementNumber;
    }

    public ContractInfoResponse(ItemEntity item) {
        this.caseNumber = item.getCaseNumber();
        this.minimumBidPrice = item.getMinimumBidPrice();
        this.biddingPeriod = item.getBiddingPeriod();
        this.managementNumber = item.getManagementNumber();
    }

    public static ContractInfoResponse of(ItemEntity item) {
        return new ContractInfoResponse(item);
    }
}
