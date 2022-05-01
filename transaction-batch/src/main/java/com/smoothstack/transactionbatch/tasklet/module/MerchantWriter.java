package com.smoothstack.transactionbatch.tasklet.module;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.smoothstack.transactionbatch.generator.MerchantGenerator;
import com.thoughtworks.xstream.XStream;

public class MerchantWriter {
    public static void write(String filePath) throws IOException {
        XStream xStream = new XStream();

        final MerchantGenerator merchants = MerchantGenerator.getInstance();

        List<String> toWrite = new ArrayList<>();
        toWrite.addAll(Arrays.asList("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<GeneratedMerchants>"));
        toWrite.addAll(merchants.getContext().stream().filter(n -> n != null).map(n -> xStream.toXML(n)).collect(Collectors.toList()));
        toWrite.add("</GeneratedMerchants>");
        
        Files.write(
            Paths.get(filePath, "output/generation/GeneratedMerchants.xml"), 
            toWrite, 
            new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING}
        );
    }
}
