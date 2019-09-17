package com.github.ankurpathak.api.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;

public class OpenCsvBeanReader<T> implements AutoCloseable{
    private static final Logger log = LoggerFactory.getLogger(OpenCsvBeanReader.class);
    private final  Resource resource;
    private final Class<T> tClass;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    private CSVReader csvReader;
    private CsvToBean<T> csvToBean;
    private Iterator<T> it;

    public OpenCsvBeanReader(Resource resource, Class<T> tClass) {
        require(resource, notNullValue());
        require(tClass, notNullValue());
        this.resource = resource;
        this.tClass = tClass;
    }

    public void init() throws IOException {
        this.inputStreamReader = new InputStreamReader(resource.getInputStream());
        this.bufferedReader = new BufferedReader(this.inputStreamReader);
        this.csvReader = new CSVReaderBuilder(bufferedReader)
        .build();
        HeaderColumnNameMappingStrategy<T> ms = new HeaderColumnNameMappingStrategy<>();
        ms.setType(this.tClass);

        this.csvToBean = new CsvToBeanBuilder<T>(this.csvReader)
            .withType(tClass)
            .withIgnoreLeadingWhiteSpace(true)
            .withThrowExceptions(true)
            .withMappingStrategy(ms)
            .build();
    }


    public void shutDown(){
        try {
            if(inputStreamReader != null){
                inputStreamReader.close();
            }
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(csvReader!= null){
                csvReader.close();
            }
        } catch (IOException e) {
            LogUtil.logInfo(log, "Problem in shutting down " + OpenCsvBeanReader.class.getSimpleName());
        }
    }



    public Optional<T> readLine() throws IOException {
        if (this.csvToBean == null){
            init();
            it = csvToBean.iterator();
        }

        return it.hasNext() ? Optional.of(it.next()): Optional.empty();
    }

    public List<T> readLines() throws IOException {
        if(this.it == null)
            init();
        return this.csvToBean.parse();
    }

    @Override
    public void close() throws IOException {
        shutDown();
    }
}
