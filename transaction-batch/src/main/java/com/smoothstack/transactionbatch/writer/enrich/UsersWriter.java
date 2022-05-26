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
import com.smoothstack.transactionbatch.generator.UserGenerator;
import com.smoothstack.transactionbatch.model.TransactRead;
import com.smoothstack.transactionbatch.model.UserBase;
import com.thoughtworks.xstream.XStream;

import org.springframework.batch.item.ItemWriter;

import io.micrometer.core.annotation.Timed;

@Timed
public class UsersWriter implements ItemWriter<TransactRead> {
    private final UserGenerator userGenerator = UserGenerator.getInstance();

    private final String basePath = new File("").getAbsolutePath();

    @Override
    public void write(List<? extends TransactRead> items) throws IOException {
        Collection<UserBase> users = items.parallelStream()
            .map(n -> userGenerator.generateUser(n.getUser()))
            .filter(n -> n.isPresent())
            .map(n -> n.get())
            .collect(Collectors.toList());

        if (!users.isEmpty()) writeOut(users);
    }

    private void writeOut(Collection<UserBase> users) throws IOException {
        XStream xStream = new XStream();

        List<String> toWrite = new ArrayList<>();

        toWrite.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Users>");

        toWrite.addAll(users.stream().map(n -> xStream.toXML(n)).collect(Collectors.toList()));

        toWrite.add("</Users>");

        Path path = Paths.get(
            basePath, 
            "output/generation/users", 
            FileDelegator.getFileName("Users")
        );

        Files.write(
            path,
            toWrite,
            new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING}
        );
    }
}
