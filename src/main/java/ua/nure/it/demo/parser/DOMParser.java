package ua.nure.it.demo.parser;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import ua.nure.it.xml.entity.location.Location;
import ua.nure.it.xml.entity.location.Locations;
import ua.nure.it.xml.entity.place.*;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static ua.nure.it.demo.parser.Const.SCHEMA_LOCATION__URI;

public class DOMParser {
    private static boolean logEnabled = true;

    private static void log(Object o) {
        if (logEnabled) {
            System.out.println(o);
        }
    }

    private List<Location> parse(InputStream in, Schema schema) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);

        // to be compliant, completely disable DOCTYPE declaration:
        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

        dbf.setSchema(schema);

        DocumentBuilder db = dbf.newDocumentBuilder();

        db.setErrorHandler(new DefaultHandler() {
            @Override
            public void error(SAXParseException e) throws SAXException {
                System.err.println(e.getMessage()); // log error
                //	throw e;
            }
        });

        // get the top of the xml tree
        Document root = db.parse(in);

        List<Location> locations = new ArrayList<>();

        Element e = root.getDocumentElement();
        NodeList xmlLocations = e.getElementsByTagNameNS(Const.LOCATIONS_NAMESPACE_URI, Const.TAG_LOCATION);
        for (int i = 0; i < xmlLocations.getLength(); i++) {
            locations.add(parseLocation(xmlLocations.item(i)));
        }
        return locations;
    }

    private Location parseLocation(Node node) {
        System.out.println("\n\nLocation:");
        Location location = new Location();

        if (node.hasAttributes()) {
            NamedNodeMap attrs = node.getAttributes();
            Node item = attrs.getNamedItem(Const.ATTR_ID);
            log(item.getLocalName() + " = " + item.getTextContent());
            location.setId(Integer.parseInt(item.getTextContent()));
        }

        NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node item = nodes.item(i);
            if (Const.TAG_TITLE.equals(item.getLocalName())) {
                log(item.getLocalName() + " = " + item.getTextContent());
                location.setTitle(item.getTextContent());
            }
            if (Const.TAG_AREA.equals(item.getLocalName())) {
                if (item.getChildNodes().getLength() > 3) {
                    location.setArea(parseArea(item));
                }
            }
            if (Const.TAG_PLACES.equals(item.getLocalName())) {
                location.setPlaces(parsePlaces(item));
            }
        }

        return location;
    }

    private Location.Places parsePlaces(Node item) {
        List<Place> places = new ArrayList<>();
        Location.Places place = new Location.Places();

        NodeList nodes = item.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node placeItem = nodes.item(i);
            if (Const.TAG_PLACE.equals(placeItem.getLocalName())) {
                places.add(parsePlace(placeItem));
            }
        }
        place.place = places;

        return place;
    }

    private Place parsePlace(Node node) {
        System.out.println();
        Place place = new Place();

        if (node.hasAttributes()) {
            NamedNodeMap attrs = node.getAttributes();
            for (int i = 0; i < attrs.getLength(); i++) {
                Node attr = attrs.item(i);
                if (Const.ATTR_ID.equals(attr.getLocalName())) {
                    place.setId(Integer.parseInt(attr.getTextContent()));
                } else if (Const.ATTR_IS_RECOMMENDED.equals(attr.getLocalName())) {
                    log(attr.getNodeName() + " = " + attr.getTextContent());
                    place.setIsRecommended(Boolean.parseBoolean(attr.getTextContent()));
                } else if (Const.ATTR_RATING.equals(attr.getLocalName())) {
                    log(attr.getNodeName() + " = " + attr.getTextContent());
                    BigDecimal rating = new BigDecimal(attr.getTextContent());

                    if (rating.compareTo(new BigDecimal("0")) == 1) {
                        place.setRating(rating);
                    }
                }
            }
        }

        NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node item = nodes.item(i);

            if (Const.TAG_TITLE.equals(item.getLocalName())) {
                log(item.getNodeName() + " = " + item.getTextContent());
                place.setTitle(item.getTextContent());
            } else if (Const.TAG_DESCRIPTION.equals(item.getLocalName())) {
                log(item.getNodeName() + " = " + item.getTextContent());
                place.setDescription(item.getTextContent());
            } else if (Const.TAG_TYPE.equals(item.getLocalName())) {
                log(item.getNodeName() + " = " + item.getTextContent());
                place.setType(item.getTextContent());
            } else if (Const.TAG_ADDRESS.equals(item.getLocalName())) {
                AddressType addressType = new AddressType();
                NodeList addressNodes = item.getChildNodes();

                for (int ai = 0; ai < addressNodes.getLength(); ai++) {
                    Node addressItem = addressNodes.item(ai);

                    if (Const.TAG_STREET.equals(addressItem.getLocalName())) {
                        log(addressItem.getNodeName() + " = " + addressItem.getTextContent());
                        addressType.setStreet(addressItem.getTextContent());
                    } else if (Const.TAG_HOUSE_NUMBER.equals(addressItem.getLocalName())) {
                        AddressType.HouseNumber houseNumber = new AddressType.HouseNumber();

                        NodeList houseNumberNodes = addressItem.getChildNodes();
                        for (int hnni = 0; hnni < houseNumberNodes.getLength(); hnni++) {
                            Node houseNumberNode = houseNumberNodes.item(hnni);
                            if (Const.TAG_NUMBER_WITH_LETTER.equals(houseNumberNode.getLocalName())) {
                                log(houseNumberNode.getNodeName() + " = " + houseNumberNode.getTextContent());
                                houseNumber.setNumberWithLetter(houseNumberNode.getTextContent());
                            } else if (Const.TAG_NUMBER.equals(houseNumberNode.getLocalName())) {
                                log(houseNumberNode.getNodeName() + " = " + houseNumberNode.getTextContent());
                                houseNumber.setNumber(Integer.parseInt(houseNumberNode.getTextContent()));
                            }
                        }
                        addressType.setHouseNumber(houseNumber);
                    } else if (Const.TAG_CITY.equals(addressItem.getLocalName())) {
                        log(addressItem.getNodeName() + " = " + addressItem.getTextContent());
                        addressType.setCity(addressItem.getTextContent());
                    } else if (Const.TAG_POSTAL_CODE.equals(addressItem.getLocalName())) {
                        log(addressItem.getNodeName() + " = " + addressItem.getTextContent());
                        addressType.setPostalCode(addressItem.getTextContent());
                    }
                }

                place.setAddress(addressType);
            } else if (Const.TAG_COORDINATE.equals(item.getLocalName())) {
                place.setCoordinate(parseCoordinate(item));
            } else if (Const.TAG_VISIT_TIME.equals(item.getLocalName())) {
                place.setVisitTime(item.getTextContent());
            } else if (Const.TAG_ENTRANCE_FEE.equals(item.getLocalName())) {
                NodeList entNodes = item.getChildNodes();
                PriceDetail priceDetail = new PriceDetail();

                for (int ei = 0; ei < entNodes.getLength(); ei++) {
                    Node entNode = entNodes.item(ei);
                    if (Const.TAG_PRICE.equals(entNode.getLocalName())) {
                        log(entNode.getNodeName() + " = " + entNode.getTextContent());
                        priceDetail.setPrice(new BigDecimal(entNode.getTextContent()));
                    } else if (Const.TAG_CURRENCY.equals(entNode.getLocalName())) {
                        log(entNode.getNodeName() + " = " + entNode.getTextContent());
                        priceDetail.setCurrency(entNode.getTextContent());
                    }
                }

                place.setEntranceFee(priceDetail);
            } else if (Const.TAG_PHOTOS.equals(item.getLocalName())) {
                NodeList photoNodes = item.getChildNodes();
                Place.Photos photos = new Place.Photos();
                List<String> arrPhotos = new ArrayList<>();

                for (int pi = 0; pi < photoNodes.getLength(); pi++) {
                    Node photo = photoNodes.item(pi);

                    if (Const.TAG_PHOTO.equals(photo.getLocalName())) {
                        arrPhotos.add(photo.getTextContent());
                    }
                }
                photos.photo = arrPhotos;

                place.setPhotos(photos);
            } else if (Const.TAG_SEASONALITY.equals(item.getLocalName())) {
                place.setSeasonality(SeasonalityType.fromValue(item.getTextContent()));
            } else if (Const.TAG_TAGS.equals(item.getLocalName())) {
                Place.Tags tags = new Place.Tags();
                List<String> arrTags = new ArrayList<>();

                NodeList tagNodes = item.getChildNodes();

                for (int ti = 0; ti < tagNodes.getLength(); ti++) {
                    Node tagNode = tagNodes.item(ti);

                    if (Const.TAG_TAG.equals(tagNode.getLocalName())) {
                        arrTags.add(tagNode.getTextContent());
                    }
                }

                tags.tag = arrTags;
                place.setTags(tags);
            }
        }
        return place;
    }

    private Location.Area parseArea(Node node) {
        List<CoordinateType> coordinates = new ArrayList<>();
        Location.Area area = new Location.Area();

        NodeList nodes = node.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node item = nodes.item(i);
            if (Const.TAG_COORDINATE.equals(item.getLocalName())) {
                coordinates.add(parseCoordinate(item));
            }
        }
        area.coordinate = coordinates;

        return area;
    }

    private CoordinateType parseCoordinate(Node node) {
        CoordinateType coordinate = new CoordinateType();

        NodeList nodes = node.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node item = nodes.item(i);
            if (Const.TAG_LATITUDE.equals(item.getLocalName())) {
                log(item.getLocalName() + " = " + item.getTextContent());
                coordinate.setLatitude(new BigDecimal(item.getTextContent()));
            } else if (Const.TAG_LONGITUDE.equals(item.getLocalName())) {
                log(item.getLocalName() + " = " + item.getTextContent());
                coordinate.setLongitude(new BigDecimal(item.getTextContent()));
            }
        }

        return coordinate;
    }

    public static void saveLocations(List<Location> locations, OutputStream out) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();

        Element rootElement = doc.createElementNS(Const.LOCATIONS_NAMESPACE_URI, Const.TAG_LOCATIONS);
        doc.appendChild(rootElement);

        rootElement.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:xsi", XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);

        rootElement.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:pl", Const.PLACES_NAMESPACE_URI);

        rootElement.setAttributeNS(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, Const.SCHEMA_LOCATION__ATTR_FQN, Const.SCHEMA_LOCATION__URI);

        // Locations
        for (Location location : locations) {
            Element locationElement = doc.createElement(Const.TAG_LOCATION);
            locationElement.setAttribute(Const.ATTR_ID, String.valueOf(location.getId()));
            rootElement.appendChild(locationElement);

            Element titleElement = doc.createElement(Const.TAG_TITLE);
            titleElement.appendChild(doc.createTextNode(location.getTitle()));
            locationElement.appendChild(titleElement);

            // AREA
            Location.Area area = location.getArea();
            if (area != null) {
                Element areaElement = doc.createElement(Const.TAG_AREA);
                for (CoordinateType coordinate : area.getCoordinate()) {
                    Element coordinateElement = doc.createElement(Const.TAG_COORDINATE);

                    Element latitudeElement = doc.createElement(Const.PLACE_NS + Const.TAG_LATITUDE);
                    latitudeElement.appendChild(doc.createTextNode(String.valueOf(coordinate.getLatitude())));
                    coordinateElement.appendChild(latitudeElement);

                    Element longitudeElement = doc.createElement(Const.PLACE_NS +Const.TAG_LONGITUDE);
                    longitudeElement.appendChild(doc.createTextNode(String.valueOf(coordinate.getLongitude())));
                    coordinateElement.appendChild(longitudeElement);

                    areaElement.appendChild(coordinateElement);
                }
                locationElement.appendChild(areaElement);
            }

            // PLACES
            Location.Places places = location.getPlaces();
            if (places != null) {
                Element placesElement = doc.createElement(Const.TAG_PLACES);
                for (Place place : places.getPlace()) {
                    Element placeElement = doc.createElement(Const.TAG_PLACE);
                    placeElement.setAttribute(Const.ATTR_ID, String.valueOf(place.getId()));
                    placeElement.setAttribute(Const.ATTR_IS_RECOMMENDED, String.valueOf(place.isIsRecommended()));
                    if (place.getRating().compareTo(new BigDecimal("0")) == 1)
                        placeElement.setAttribute(Const.ATTR_RATING, String.valueOf(place.getRating()));

                    // Для Title
                    Element placeTitleElement = doc.createElement(Const.PLACE_NS + Const.TAG_TITLE);
                    placeTitleElement.appendChild(doc.createTextNode(place.getTitle()));
                    placeElement.appendChild(placeTitleElement);

                    // Для Description
                    Element descriptionElement = doc.createElement(Const.PLACE_NS + Const.TAG_DESCRIPTION);
                    descriptionElement.appendChild(doc.createTextNode(place.getDescription()));
                    placeElement.appendChild(descriptionElement);

                    // Для Type
                    Element typeElement = doc.createElement(Const.PLACE_NS + Const.TAG_TYPE);
                    typeElement.appendChild(doc.createTextNode(place.getType()));
                    placeElement.appendChild(typeElement);

                    // Для Address
                    AddressType address = place.getAddress();
                    if (address != null) {
                        Element addressElement = doc.createElement(Const.PLACE_NS + Const.TAG_ADDRESS);

                        Element streetElement = doc.createElement(Const.PLACE_NS + Const.TAG_STREET);
                        streetElement.appendChild(doc.createTextNode(address.getStreet()));
                        addressElement.appendChild(streetElement);

                        AddressType.HouseNumber houseNumber = address.getHouseNumber();
                        Element houseNumberElement = doc.createElement(Const.PLACE_NS + Const.TAG_HOUSE_NUMBER);
                        if (houseNumber.getNumber() != null) {
                            Element numberElement = doc.createElement(Const.PLACE_NS + Const.TAG_NUMBER);
                            numberElement.appendChild(doc.createTextNode(String.valueOf(houseNumber.getNumber())));
                            houseNumberElement.appendChild(numberElement);
                        } else if (houseNumber.getNumberWithLetter() != null){
                            Element numberWithLetterElement = doc.createElement(Const.PLACE_NS + Const.TAG_NUMBER_WITH_LETTER);
                            numberWithLetterElement.appendChild(doc.createTextNode(houseNumber.getNumberWithLetter()));
                            houseNumberElement.appendChild(numberWithLetterElement);
                        }

                        addressElement.appendChild(houseNumberElement);

                        Element cityElement = doc.createElement(Const.PLACE_NS + Const.TAG_CITY);
                        cityElement.appendChild(doc.createTextNode(address.getCity()));
                        addressElement.appendChild(cityElement);

                        Element postalCodeElement = doc.createElement(Const.PLACE_NS + Const.TAG_POSTAL_CODE);
                        postalCodeElement.appendChild(doc.createTextNode(address.getPostalCode()));
                        addressElement.appendChild(postalCodeElement);

                        placeElement.appendChild(addressElement);
                    }

                    // Для Coordinate
                    CoordinateType coordinate = place.getCoordinate();
                    if (coordinate != null) {
                        Element coordinateElement = doc.createElement(Const.PLACE_NS + Const.TAG_COORDINATE);

                        Element latitudeElement = doc.createElement(Const.PLACE_NS + Const.TAG_LATITUDE);
                        latitudeElement.appendChild(doc.createTextNode(String.valueOf(coordinate.getLatitude())));
                        coordinateElement.appendChild(latitudeElement);

                        Element longitudeElement = doc.createElement(Const.PLACE_NS + Const.TAG_LONGITUDE);
                        longitudeElement.appendChild(doc.createTextNode(String.valueOf(coordinate.getLongitude())));
                        coordinateElement.appendChild(longitudeElement);

                        placeElement.appendChild(coordinateElement);
                    }

                    // Для VisitTime
                    if (place.getVisitTime() != null){
                        Element visitTimeElement = doc.createElement(Const.PLACE_NS + Const.TAG_VISIT_TIME);
                        visitTimeElement.appendChild(doc.createTextNode(place.getVisitTime()));
                        placeElement.appendChild(visitTimeElement);
                    }

                    // Для EntranceFee
                    PriceDetail entranceFee = place.getEntranceFee();
                    if (entranceFee != null) {
                        Element entranceFeeElement = doc.createElement(Const.PLACE_NS + Const.TAG_ENTRANCE_FEE);

                        Element priceElement = doc.createElement(Const.PLACE_NS + Const.TAG_PRICE);
                        priceElement.appendChild(doc.createTextNode(String.valueOf(entranceFee.getPrice())));
                        entranceFeeElement.appendChild(priceElement);

                        Element currencyElement = doc.createElement(Const.PLACE_NS + Const.TAG_CURRENCY);
                        currencyElement.appendChild(doc.createTextNode(entranceFee.getCurrency()));
                        entranceFeeElement.appendChild(currencyElement);

                        placeElement.appendChild(entranceFeeElement);
                    }

                    // Для Photos
                    Place.Photos photos = place.getPhotos();
                    if (photos != null) {
                        Element photosElement = doc.createElement(Const.PLACE_NS + Const.TAG_PHOTOS);
                        for (String photo : photos.getPhoto()) {
                            Element photoElement = doc.createElement(Const.PLACE_NS + Const.TAG_PHOTO);
                            photoElement.appendChild(doc.createTextNode(photo));
                            photosElement.appendChild(photoElement);
                        }
                        placeElement.appendChild(photosElement);
                    }

                    // Для Seasonality
                    Element seasonalityElement = doc.createElement(Const.PLACE_NS + Const.TAG_SEASONALITY);
                    seasonalityElement.appendChild(doc.createTextNode(place.getSeasonality().value()));
                    placeElement.appendChild(seasonalityElement);

                    // Для Tags
                    Place.Tags tags = place.getTags();
                    if (tags != null) {
                        Element tagsElement = doc.createElement(Const.PLACE_NS + Const.TAG_TAGS);
                        for (String tag : tags.getTag()) {
                            Element tagElement = doc.createElement(Const.PLACE_NS + Const.TAG_TAG);
                            tagElement.appendChild(doc.createTextNode(tag));
                            tagsElement.appendChild(tagElement);
                        }
                        placeElement.appendChild(tagsElement);
                    }

                    placesElement.appendChild(placeElement);
                }
                locationElement.appendChild(placesElement);
            }
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(out);
        transformer.transform(source, result);
    }


    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
        // Create against validation schema
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(new File("locations.xsd"));

        System.out.println("--== DOM Parser ==--");
        DOMParser domParser = new DOMParser();
        InputStream in = new FileInputStream("locations.xsd.xml");
        List<Location> locations = domParser.parse(in, schema);
        System.out.println("====================================");
        System.out.println("Here is the locations: \n" + locations);
        System.out.println("====================================");

        try {
            saveLocations(locations, new FileOutputStream(Const.XML_FILE + ".dom.xml"));
        } catch (Exception ex) {
            System.err.println("====================================");
            System.err.println("Object tree not valid against XSD.");
            System.err.println(ex.getClass().getName());
            System.err.println("====================================");
        }

        in = new FileInputStream("invalid_locations.xml");
        locations = domParser.parse(in, schema);
        System.out.println("====================================");
        System.out.println("Here is the locations from invalid xml: \n" + locations);
        System.out.println("====================================");
    }
}
