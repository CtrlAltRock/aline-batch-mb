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

import com.smoothstack.transactionbatch.generator.UserGenerator;
import com.thoughtworks.xstream.XStream;


public class UserWriter {
    public static void write(UserGenerator users, String filePath) throws IOException {
        XStream xStream = new XStream();

        List<String> toWrite = new ArrayList<>();
        toWrite.addAll(Arrays.asList("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<GeneratedUsers>"));
        toWrite.addAll(users.getContext().values().stream().filter(n -> n != null).map(n -> xStream.toXML(n)).collect(Collectors.toList()));
        toWrite.add("</GeneratedUsers>");
        Files.write(
            Paths.get(filePath, "output/generation/GeneratedUsers.xml"), 
            toWrite, 
            new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING}
        );
    }
}
