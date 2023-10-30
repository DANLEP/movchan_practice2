package ua.nure.it.demo.parser;

public interface Const {

    String TAG_LOCATION = "Location";

    String XML_FILE = "location.xml";
    String INVALID_XML_FILE = "invalid_orders.xml";
    String XSD_FILE = "location.xsd";
    Class<?> OBJECT_FACTORY = ua.nure.it.xml.entity.location.ObjectFactory.class;

    String LOCATION_NAMESPACE_URI = "http://it.nure.ua/xml/entity/location";
    String SCHEMA_LOCATION__ATTR_NAME = "schemaLocation";
    String SCHEMA_LOCATION__ATTR_FQN = "xsi:schemaLocation";
    String XSI_SPACE__PREFIX = "xsi";
    String SCHEMA_LOCATION__URI = "http://it.nure.ua/xml/entity/location location.xsd";

    // validation features
    public static final String FEATURE__TURN_VALIDATION_ON =
            "http://xml.org/sax/features/validation";
    public static final String FEATURE__TURN_SCHEMA_VALIDATION_ON =
            "http://apache.org/xml/features/validation/schema";
}
