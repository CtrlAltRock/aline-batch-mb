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
import com.smoothstack.transactionbatch.generator.CardGenerator;
import com.smoothstack.transactionbatch.model.CardBase;
import com.smoothstack.transactionbatch.model.TransactRead;
import com.thoughtworks.xstream.XStream;

import org.springframework.batch.item.ItemWriter;

public class CardsWriter implements ItemWriter<TransactRead> {
    private final CardGenerator cardGenerator = CardGenerator.getInstance();

    private final String basePath = new File("").getAbsolutePath();

    @Override
    public void write(List<? extends TransactRead> items) throws IOException {
        Collection<CardBase> cards = items.parallelStream()
            .map(n -> cardGenerator.generateCard(n.getUser(), n.getCard()))
            .filter(n -> n.isPresent())
            .map(n -> n.get())
            .collect(Collectors.toList());

        if (!cards.isEmpty()) writeOut(cards);
    }

    private void writeOut(Collection<CardBase> cards) throws IOException {
        XStream xStream = new XStream();

        List<String> toWrite = new ArrayList<>();

        toWrite.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Cards>");

        toWrite.addAll(cards.stream().map(n -> xStream.toXML(n)).collect(Collectors.toList()));

        toWrite.add("</Cards>");

        Path path = Paths.get(
            basePath, 
            "output/generation/cards", 
            FileDelegator.getFileName("Cards")
        );

        Files.write(
            path,
            toWrite,
            new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING}
        );
    }
}
