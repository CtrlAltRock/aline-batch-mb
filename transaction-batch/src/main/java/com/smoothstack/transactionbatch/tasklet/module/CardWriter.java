package com.smoothstack.transactionbatch.tasklet.module;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.smoothstack.transactionbatch.generator.CardGenerator;
import com.smoothstack.transactionbatch.model.CardBase;
import com.thoughtworks.xstream.XStream;

public class CardWriter {
    public static void write(CardGenerator cards, String filePath) throws IOException {
        XStream xStream = new XStream();

        List<String> toWrite = new ArrayList<>();
        toWrite.addAll(Arrays.asList("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<GeneratedCards>"));
        List<String> card = cards.getContext().values().stream()
            .flatMap((AbstractMap<Long, CardBase> n) -> n.values().stream())
            .map(n -> xStream.toXML(n))
            .collect(Collectors.toList());

        toWrite.addAll(card);
        toWrite.add("</GeneratedCards>");
        
        Files.write(
            Paths.get(filePath, "output/generation/GeneratedCards.xml"), 
            toWrite, 
            new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING}
        );
    }
}
