package com.rassame.trader.client.wise;

import com.rassame.trader.client.wise.protocol.CreateRecipientRequest;
import com.rassame.trader.client.wise.protocol.Recipient;
import com.rassame.trader.config.UnirestConfig;
import com.rassame.trader.services.MultiWiseService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RecipientClient extends BaseWiseClient {

    public RecipientClient(MultiWiseService multiWiseService, UnirestConfig unirestConfig) {
        super(multiWiseService,unirestConfig);
    }

//    @PostConstruct
//    public void setup() {
//        System.out.println(createRecipient("ann.johnson@gmail.com", "Ann Johnson", "USD"));
//    }

    public Recipient createRecipient(String email, String holder, String currency) {
        Map<String, Object> details = new HashMap<>();
        details.put("email", email);
        CreateRecipientRequest createQuoteRequest = new CreateRecipientRequest()
                .setProfile(Integer.valueOf(multiWiseService.getWise().getProfileId()))
                .setCurrency(currency)
                .setDetails(details)
                .setAccountHolderName(holder);

        Recipient recipient = (Recipient) post(createQuoteRequest, "/v1/accounts", Recipient.class);
        return recipient;
    }

    public Recipient createRecipient(String accountNumber,String routingNumber, String holder, String currency) {
        Map<String, String> address = new HashMap<>();
        address.put("country", "US");
        address.put("countryCode", "US");
        address.put("firstLine", "19 W 24th Street");
        address.put("postCode", "10010");
        address.put("city", "New York");
        address.put("state", "NY");

        Map<String, Object> details = new HashMap<>();
        details.put("accountNumber", accountNumber);
        details.put("abartn", routingNumber);
        details.put("accountType", "CHECKING");
        details.put("address", address);

        CreateRecipientRequest createQuoteRequest = new CreateRecipientRequest()
                .setProfile(Integer.valueOf(multiWiseService.getWise().getProfileId()))
                .setCurrency(currency)
                .setDetails(details)
                .setType("aba")
                .setAccountHolderName(holder);

        Recipient recipient = (Recipient) post(createQuoteRequest, "/v1/accounts", Recipient.class);
        return recipient;
    }
}
