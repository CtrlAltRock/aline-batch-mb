package com.smoothstack.transactionbatch.processor;

import java.util.Optional;

import com.smoothstack.transactionbatch.dto.MerchantDto;
import com.smoothstack.transactionbatch.generator.MerchantGenerator;
import com.smoothstack.transactionbatch.model.MerchantBase;
import com.smoothstack.transactionbatch.model.TransactRead;

import org.springframework.batch.item.ItemProcessor;

public class MerchantProcessor implements ItemProcessor<TransactRead, MerchantBase> {
    private static MerchantGenerator merchantGenerator = MerchantGenerator.getInstance();
    
    @Override
    public MerchantBase process(TransactRead item) throws Exception {
        String zip;
        String state;

        if (item.getZip().isBlank()) zip = "NA"; else zip = item.getZip().substring(0, 5);
        if (item.getCity().isBlank()) state = "NA"; else state = item.getState();
        
        MerchantDto merchant = new MerchantDto(item.getMerchant(), item.getCity(), state, zip, Integer.toString(item.getMcc()));
        
        Optional<MerchantBase> createdMerchant = merchantGenerator.generateMerchant(merchant);

        if (createdMerchant.isEmpty()) return null; else return createdMerchant.get();
    }
}
