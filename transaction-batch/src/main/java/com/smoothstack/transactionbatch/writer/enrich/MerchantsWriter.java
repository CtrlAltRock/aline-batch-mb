package com.smoothstack.transactionbatch.writer.enrich;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.smoothstack.transactionbatch.config.FileDelegator;
import com.smoothstack.transactionbatch.dto.MerchantDto;
import com.smoothstack.transactionbatch.generator.MerchantGenerator;
import com.smoothstack.transactionbatch.model.MerchantBase;
import com.smoothstack.transactionbatch.model.TransactRead;
import com.thoughtworks.xstream.XStream;

import org.springframework.batch.item.ItemWriter;

import io.micrometer.core.annotation.Timed;

@Timed
public class MerchantsWriter implements ItemWriter<TransactRead> {
    private final MerchantGenerator merchGenerator = MerchantGenerator.getInstance();

    private final String basePath = new File("").getAbsolutePath();

    @Override
    public void write(List<? extends TransactRead> items) throws IOException {
        Collection<MerchantBase> merchants = items.parallelStream()
            .map(n -> merchGenerator.generateMerchant(
                new MerchantDto(
                    n.getMerchant(), 
                    n.getCity(), 
                    n.getState(), 
                    n.getZip(), 
                    n.getMcc()
                )
            ))
            .filter(n -> n.isPresent())
            .map(n -> n.get())
            .collect(Collectors.toList());

        if (!merchants.isEmpty()) writeOut(merchants);
    }

    private void writeOut(Collection<MerchantBase> merchants) throws IOException {
        XStream xStream = new XStream();

        List<String> toWrite = new ArrayList<>();

        toWrite.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Merchants>");

        toWrite.addAll(merchants.stream().map(n -> xStream.toXML(n)).collect(Collectors.toList()));

        toWrite.add("</Merchants>");

        Path path = Paths.get(
            basePath, 
            "output/generation/merchants", 
            FileDelegator.getFileName("Merchants")
        );

        Files.write(
            path,
            toWrite,
            new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING}
        );
    }
}
