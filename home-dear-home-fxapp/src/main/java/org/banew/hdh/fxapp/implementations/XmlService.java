package org.banew.hdh.fxapp.implementations;

import jakarta.xml.bind.*;
import org.banew.hdh.fxapp.implementations.xml.XmlStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class XmlService {
    @Autowired
    private JAXBContext context;

    public void generateSchema() {
        try {
            context.generateSchema(new SchemaOutputResolver() {
                @Override
                public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
                    suggestedFileName = "schema.xsd";
                    StreamResult result = new StreamResult(new File(suggestedFileName));
                    result.setSystemId(suggestedFileName);
                    return result;
                }
            });
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveToXml(XmlStorage xmlStorage, File file) throws JAXBException {

        Marshaller mar = context.createMarshaller();

        // Щоб XML був гарним, а не в один рядок
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        mar.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "com.banew/home-dear-home/storage schema.xsd");

        mar.marshal(xmlStorage, file);
    }

    public Optional<XmlStorage> loadFromXml(File file) {
        try {
            Unmarshaller unmar = context.createUnmarshaller();

            return Optional.of((XmlStorage) unmar.unmarshal(file));
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }
}
