package com.rassame.trader.client.wise;

import com.rassame.trader.client.wise.protocol.*;
import com.rassame.trader.config.UnirestConfig;
import com.rassame.trader.services.MultiWiseService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TransferClient extends BaseWiseClient {

    public TransferClient(MultiWiseService multiWiseService, UnirestConfig unirestConfig) {
        super(multiWiseService, unirestConfig);
    }

//    @PostConstruct
//    public void setup() {
//        System.out.println(createTransfer(147919003L, "94199f5d-f874-4a67-868a-49e0896dc385"));
//    }

    public Transfer createTransfer(Long accountId, String quoteId) {
        Map<String, String> details = new HashMap<>();
        details.put("reference", "ref");
        details.put("transferPurpose", "web service pay");
        details.put("sourceOfFunds", "freelance");
        CreateTransferRequest createQuoteRequest = new CreateTransferRequest()
                .setCustomerTransactionId(UUID.randomUUID().toString())
                .setDetails(details)
                .setQuoteUuid(quoteId)
                .setTargetAccount(accountId);

        Transfer transfer = (Transfer) post(createQuoteRequest, "/v1/transfers", Transfer.class);
        return transfer;
    }

    public FundTransferResponse fundTransfer(Long transferId) {
        String path = "/v3/profiles/" + multiWiseService.getWise().getProfileId() + "/transfers/" + transferId + "/payments";
        FundTransferResponse response = (FundTransferResponse) post(new FundTransferRequest(), path, FundTransferResponse.class);
        return response;
    }
}
