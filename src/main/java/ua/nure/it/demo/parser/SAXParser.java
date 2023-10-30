package ua.nure.it.demo.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ua.nure.it.xml.entity.location.Location;
import ua.nure.it.xml.entity.place.*;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SAXParser extends DefaultHandler {
    private static final boolean LOG_ENABLED = false;

    public static void log(Object o) {
        if (LOG_ENABLED) {
            System.out.println(o);
        }
    }

    private String current;
    private String titleParent;
    private String coordinateParent;
    private List<Location> locations;
    private Location location;
    private CoordinateType coordinateType;
    private PriceDetail entranceFee;
    private Place place;
    private Place.Photos photos;

    private List<Location> getLocations() {
        return locations;
    }

    @Override
    public void error(org.xml.sax.SAXParseException e) {
        System.err.println(e.getMessage());
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

        current = localName;

        if (Const.TAG_LOCATIONS.equals(current)) {
            locations = new ArrayList<>();
        } else if (Const.TAG_LOCATION.equals(current)) {
            titleParent = Const.TAG_LOCATION;
            location = new Location();
            if (attributes.getLength() > 0)
                location.setId(Integer.parseInt(attributes.getValue("id")));
        } else if (Const.TAG_AREA.equals(current)) {
            coordinateParent = Const.TAG_AREA;
            location.setArea(new Location.Area());
        } else if (Const.TAG_COORDINATE.equals(current)) {
            coordinateType = new CoordinateType();
        } else if (Const.TAG_PLACES.equals(current)) {
            location.setPlaces(new Location.Places());
        } else if (Const.TAG_PLACE.equals(current)) {
            titleParent = Const.TAG_PLACE;
            coordinateParent = Const.TAG_PLACE;
            place = new Place();
            if (attributes.getLength() > 0) {
                place.setId(Integer.parseInt(attributes.getValue(Const.ATTR_ID)));
                place.setIsRecommended(Boolean.parseBoolean(attributes.getValue(Const.ATTR_IS_RECOMMENDED)));
                if (attributes.getValue(Const.ATTR_RATING) != null)
                    place.setRating(new BigDecimal(attributes.getValue(Const.ATTR_RATING)));
            }
        } else if (Const.TAG_ENTRANCE_FEE.equals(current)){
            entranceFee = new PriceDetail();
        } else if (Const.TAG_PHOTOS.equals(current)){
            photos = new Place.Photos();
        } else if (Const.TAG_TAGS.equals(current)){
            place.setTags(new Place.Tags());
        } else if (Const.TAG_ADDRESS.equals(current)){
            AddressType addressType = new AddressType();
            addressType.setHouseNumber(new AddressType.HouseNumber());
            place.setAddress(addressType);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String value = new String(ch, start, length);
        if (value.isBlank()) {
            return;
        }
        if (Const.TAG_TITLE.equals(current)){
            if (Const.TAG_LOCATION.equals(titleParent))
                location.setTitle(value);
            if (Const.TAG_PLACE.equals(titleParent))
                place.setTitle(value);
        } else if (Const.TAG_LATITUDE.equals(current)){
            coordinateType.setLatitude(new BigDecimal(value));
        } else if (Const.TAG_LONGITUDE.equals(current)){
            coordinateType.setLongitude(new BigDecimal(value));
        } else if (Const.TAG_DESCRIPTION.equals(current)){
            place.setDescription(value);
        } else if (Const.TAG_TYPE.equals(current)){
            place.setType(value);
        } else if (Const.TAG_VISIT_TIME.equals(current)){
            place.setVisitTime(value);
        } else if (Const.TAG_PRICE.equals(current)){
            entranceFee.setPrice(new BigDecimal(value));
        } else if (Const.TAG_CURRENCY.equals(current)){
            entranceFee.setCurrency(value);
        } else if (Const.TAG_PHOTO.equals(current)){
            photos.getPhoto().add(value);
        } else if (Const.TAG_SEASONALITY.equals(current)){
            place.setSeasonality(SeasonalityType.fromValue(value));
        } else if (Const.TAG_TAG.equals(current)){
            place.getTags().getTag().add(value);
        } else if (Const.TAG_CITY.equals(current)){
            place.getAddress().setCity(value);
        } else if (Const.TAG_STREET.equals(current)){
            place.getAddress().setStreet(value);
        } else if (Const.TAG_POSTAL_CODE.equals(current)){
            place.getAddress().setPostalCode(value);
        } else if (Const.TAG_NUMBER.equals(current)){
            place.getAddress().getHouseNumber().setNumber(Integer.parseInt(value));
        } else if (Const.TAG_NUMBER_WITH_LETTER.equals(current)){
            place.getAddress().getHouseNumber().setNumberWithLetter(value);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (Const.TAG_LOCATION.equals(localName)) {
            locations.add(location);
            log(current + " " + location);
        } else if (Const.TAG_COORDINATE.equals(localName)) {
            if (Const.TAG_AREA.equals(coordinateParent))
                location.getArea().getCoordinate().add(coordinateType);
            if (Const.TAG_PLACE.equals(coordinateParent))
                place.setCoordinate(coordinateType);
        } else if (Const.TAG_PLACE.equals(localName)){
            location.getPlaces().getPlace().add(place);
        } else if (Const.TAG_ENTRANCE_FEE.equals(localName)){
            place.setEntranceFee(entranceFee);
        } else if (Const.TAG_PHOTOS.equals(localName)){
            place.setPhotos(photos);
        }
    }

    private List<Location> parse(FileInputStream in, Schema schema) throws ParserConfigurationException, SAXException, IOException {

        /**
         * SAXParserFactory factory = SAXParserFactory.newInstance();
         *
         * // to be compliant, completely disable DOCTYPE declaration:
         * factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
         *
         * // or completely disable external entities declarations:
         * factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
         * factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
         *
         * // or prohibit the use of all protocols by external entities:
         * SAXParser parser = factory.newSAXParser(); // Noncompliant
         * parser.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
         * parser.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
         *
         */
        // XML parsers should not be vulnerable to XXE attacks
        // Fix by yourself
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);

        // make parser validating
//		factory.setFeature(Const.FEATURE__TURN_VALIDATION_ON, true);
//		factory.setFeature(Const.FEATURE__TURN_SCHEMA_VALIDATION_ON, true);

        factory.setSchema(schema);
        javax.xml.parsers.SAXParser parser = factory.newSAXParser();
        parser.parse(in, this);

        return locations;
    }

    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
        // Create against validation schema
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(new File(Const.XSD_FILE));

        System.out.println("--== SAX Parser ==--");
        SAXParser parser = new SAXParser();
        parser.parse(new FileInputStream("locations.xsd.xml"), schema);
        List<Location> locations = parser.getLocations();
        System.out.println("====================================");
        System.out.println("Here is the locations: \n" + locations);
        System.out.println("====================================");
        parser.parse(new FileInputStream(Const.INVALID_XML_FILE), schema);
        locations = parser.getLocations();
        System.out.println("====================================");
        System.out.println("Here is the invalid locations: \n" + locations);
        System.out.println("====================================");
    }
}
