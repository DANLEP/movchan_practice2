package ua.nure.it.demo.parser;

import jakarta.xml.bind.*;
import org.xml.sax.SAXException;
import ua.nure.it.xml.entity.location.Locations;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;

public class JAXBParser {
    public static Locations loadLocations(final String xmlFileName,
                                    final String xsdFileName, Class<?> objectFactory) throws JAXBException, SAXException {
        JAXBContext jc = JAXBContext.newInstance(objectFactory);
        Unmarshaller unmarshaller = jc.createUnmarshaller();

        // obtain schema
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        if (xsdFileName != null) { // <-- setup validation on
            Schema schema = null;
            if ("".equals(xsdFileName)) {
                // setup validation against XSD pointed in XML
                schema = sf.newSchema();
            } else {
                // setup validation against external XSD
                schema = sf.newSchema(new File(xsdFileName));
            }

            unmarshaller.setSchema(schema); // <-- set XML schema for validation

            // set up handler
            unmarshaller.setEventHandler(new ValidationEventHandler() {
                // this method will be invoked if XML is NOT valid
                @Override
                public boolean handleEvent(ValidationEvent event) {
                    // at this point we have NOT valid XML document
                    // just print message
                    System.err.println("====================================");
                    System.err.println(xmlFileName + " is NOT valid against "
                            + xsdFileName + ":\n" + event.getMessage());
                    System.err.println("====================================");

                    // if we place 'return false;' unmarshal method throws
                    // exception
                    // 'return true;' does not imply any exceptions
                    return true;
                }
            });
        }

        // do unmarshal
        Locations locations = (Locations) unmarshaller.unmarshal(new File(xmlFileName));
        return locations; // <-- filled container
    }

    public static void saveLocations(Locations locations, final String xmlFileName,
                                  final String xsdFileName, Class<?> objectFactory) throws JAXBException, SAXException {
        JAXBContext jc = JAXBContext.newInstance(objectFactory);
        Marshaller marshaller = jc.createMarshaller();

        // obtain schema
        SchemaFactory sf = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // setup validation against XSD
        if (xsdFileName != null) {
            Schema schema = sf.newSchema(new File(xsdFileName));

            marshaller.setSchema(schema);
            marshaller.setEventHandler(new ValidationEventHandler() {
                @Override
                public boolean handleEvent(ValidationEvent event) {
                    // at this point we have NOT valid XML document
                    // just print message
                    System.err.println("====================================");
                    System.err.println(xmlFileName + " is NOT valid against "
                            + xsdFileName + ":\n" + event.getMessage());
                    System.err.println("====================================");

                    // if we place 'return false;' marshal method throws
                    // exception
                    // 'return true;' does not imply any exceptions
                    return false;
                }
            });
        }

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, Const.SCHEMA_LOCATION__URI);
        marshaller.marshal(locations, new File(xmlFileName));
    }

    public static void main(String[] args) throws JAXBException, SAXException {
        System.out.println("--== JAXB Parser ==--");
        // load Orders object from NOT valid XML (success, just prints validation
        // warning)
        Locations locations = loadLocations(Const.XML_FILE, Const.XSD_FILE, Const.OBJECT_FACTORY);

        // we have Orders object at this point
        System.out.println("====================================");
        System.out.println("Here is the locations: \n" + locations);
        System.out.println("====================================");

        try {
            saveLocations(locations, Const.XML_FILE + ".jaxb.xml", Const.XSD_FILE, Const.OBJECT_FACTORY);
        } catch (Exception ex) {
            System.err.println("====================================");
            System.err.println("Object tree not valid against XSD.");
            System.err.println(ex.getClass().getName());
            System.err.println("====================================");
        }
    }
}
