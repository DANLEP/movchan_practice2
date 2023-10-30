package ua.nure.it.demo.parser;

public interface Const {

    String TAG_LOCATIONS = "locations";
    String TAG_LOCATION = "location";
    String ATTR_ID = "id";
    String ATTR_IS_RECOMMENDED = "isRecommended";
    String ATTR_RATING = "rating";
    String TAG_TITLE = "title";
    String TAG_DESCRIPTION = "description";
    String TAG_AREA = "area";
    String TAG_COORDINATE = "coordinate";
    String TAG_LATITUDE = "Latitude";
    String TAG_LONGITUDE = "Longitude";
    String TAG_PLACES = "places";
    String TAG_PLACE = "place";
    String TAG_TYPE = "type";
    String TAG_ADDRESS = "address";
    String TAG_STREET = "Street";
    String TAG_HOUSE_NUMBER = "HouseNumber";
    String TAG_NUMBER = "Number";
    String TAG_NUMBER_WITH_LETTER = "NumberWithLetter";
    String TAG_CITY = "City";
    String TAG_POSTAL_CODE = "PostalCode";
    String TAG_VISIT_TIME = "visitTime";
    String TAG_ENTRANCE_FEE = "entranceFee";
    String TAG_PRICE = "Price";
    String TAG_CURRENCY = "Currency";
    String TAG_PHOTOS = "photos";
    String TAG_PHOTO = "photo";
    String TAG_SEASONALITY = "seasonality";
    String TAG_TAGS = "tags";
    String TAG_TAG = "Tag";

    String XML_FILE = "locations.xsd.xml";
    String INVALID_XML_FILE = "invalid_locations.xml";
    String XSD_FILE = "locations.xsd";
    Class<?> OBJECT_FACTORY = ua.nure.it.xml.entity.location.ObjectFactory.class;

    String LOCATIONS_NAMESPACE_URI = "http://it.nure.ua/xml/entity/location/";
    String SCHEMA_LOCATION__ATTR_NAME = "schemaLocation";
    String SCHEMA_LOCATION__ATTR_FQN = "xsi:schemaLocation";
    String XSI_SPACE__PREFIX = "xsi";
    String SCHEMA_LOCATION__URI = "http://it.nure.ua/xml/entity/location/ locations.xsd";
    String PLACES_NAMESPACE_URI = "http://it.nure.ua/xml/entity/place/";
    String PLACE_NS = "pl:";
    String PLACES_SCHEMA_LOCATION_URI = "http://it.nure.ua/xml/entity/place/ place.xsd";

    // validation features
    public static final String FEATURE__TURN_VALIDATION_ON =
            "http://xml.org/sax/features/validation";
    public static final String FEATURE__TURN_SCHEMA_VALIDATION_ON =
            "http://apache.org/xml/features/validation/schema";
}
