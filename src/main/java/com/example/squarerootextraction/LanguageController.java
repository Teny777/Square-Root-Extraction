package com.example.squarerootextraction;

import javafx.util.Pair;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
public class LanguageController {
    /*
    Ужасно конечно, по хорошему нужен еще класс

    ru ->
        name = Русский
        precision = Точность
    en ->
        name = Английски
        ...
     */
    private HashMap<String, Properties> localizations;
    private List<Pair<String, String>> languageNames;

    public LanguageController(String languageFolderPath)
    {
        localizations = new HashMap<>();
        languageNames = new ArrayList<>();
        try {
            Files.walk(Paths.get(languageFolderPath))
                    .filter(Files::isRegularFile)
                    .filter(f -> f.toString().toLowerCase().endsWith(Strings.PropertiesExtension))
                    .forEach(f -> {
                        String shortLanguageName = f.getFileName().toString().split("\\.")[0].toString();
                        Properties languageProps = loadLanguageProperties(f);
                        localizations.put(shortLanguageName, languageProps);

                        String languageName = languageProps.getProperty("name");
                        languageNames.add(new Pair<>(languageName,shortLanguageName));
                    });
        }
        catch (IOException e)
        {
            return;
        }
    }

    private Properties loadLanguageProperties(Path path)
    {
        Properties props = null;
        try {
            props = new Properties();
            props.load(new InputStreamReader(new FileInputStream(path.toAbsolutePath().toString()), "UTF-8"));
        }
        catch(IOException ioe) {
            System.out.println("Error while loading lang props");
        }

        return props;
    }

    public List<Pair<String, String>> getListOfLanguages()
    {
        return languageNames;
    }

    public String getLocalizedName(String shortLanguageName, String key)
    {
        return localizations.get(shortLanguageName).getProperty(key);
    }
}
