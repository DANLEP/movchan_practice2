package ua.nure.it.demo.parser;

import org.xml.sax.Attributes;
import ua.nure.it.xml.entity.location.Location;
import ua.nure.it.xml.entity.place.*;

import javax.xml.namespace.QName;
import javax.xml.stream.events.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EventHandler {
    private static final boolean LOG_ENABLED = false;

    private String current;
    private String titleParent;
    private String coordinateParent;
    private List<Location> locations;
    private Location location;
    private CoordinateType coordinateType;
    private PriceDetail entranceFee;
    private Place place;
    private Place.Photos photos;

    private static void log(Object o) {
        if (LOG_ENABLED) {
            System.out.println(o);
        }
    }

    public void startElement(XMLEvent event) {
        StartElement startElement = event.asStartElement();
        current = startElement.getName().getLocalPart();
        log("StartElement: " + startElement.getName());

        if (Const.TAG_LOCATIONS.equals(current)) {
            locations = new ArrayList<>();
        } else if (Const.TAG_LOCATION.equals(current)) {
            titleParent = Const.TAG_LOCATION;
            location = new Location();
            Attribute attr = startElement.getAttributeByName(new QName(Const.ATTR_ID));
            if (attr != null)
                location.setId(Integer.parseInt(attr.getValue()));
        } else if (Const.TAG_AREA.equals(current)) {
            coordinateParent = Const.TAG_AREA;
            location.setArea(new ua.nure.it.xml.entity.location.Location.Area());
        } else if (Const.TAG_COORDINATE.equals(current)) {
            coordinateType = new CoordinateType();
        }else if (Const.TAG_PLACES.equals(current)) {
            location.setPlaces(new Location.Places());
        } else if (Const.TAG_PLACE.equals(current)) {
            titleParent = Const.TAG_PLACE;
            coordinateParent = Const.TAG_PLACE;
            place = new Place();

            Attribute attr = startElement.getAttributeByName(new QName(Const.ATTR_ID));
            if (attr != null)
                place.setId(Integer.parseInt(attr.getValue()));

            attr = startElement.getAttributeByName(new QName(Const.ATTR_IS_RECOMMENDED));
            if (attr != null)
                place.setIsRecommended(Boolean.parseBoolean(attr.getValue()));

            attr = startElement.getAttributeByName(new QName(Const.ATTR_RATING));
            if (attr != null)
                place.setRating(new BigDecimal(attr.getValue()));
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

    public void characters(XMLEvent event) {
        Characters characters = event.asCharacters();
        log("Characters: " + current);

        if (Const.TAG_TITLE.equals(current)){
            if (Const.TAG_LOCATION.equals(titleParent))
                location.setTitle(characters.getData());
            if (Const.TAG_PLACE.equals(titleParent))
                place.setTitle(characters.getData());
        } else if (Const.TAG_LATITUDE.equals(current)){
            coordinateType.setLatitude(new BigDecimal(characters.getData()));
        } else if (Const.TAG_LONGITUDE.equals(current)){
            coordinateType.setLongitude(new BigDecimal(characters.getData()));
        } else if (Const.TAG_DESCRIPTION.equals(current)){
            place.setDescription(characters.getData());
        } else if (Const.TAG_TYPE.equals(current)){
            place.setType(characters.getData());
        } else if (Const.TAG_VISIT_TIME.equals(current)){
            place.setVisitTime(characters.getData());
        } else if (Const.TAG_PRICE.equals(current)){
            entranceFee.setPrice(new BigDecimal(characters.getData()));
        } else if (Const.TAG_CURRENCY.equals(current)){
            entranceFee.setCurrency(characters.getData());
        } else if (Const.TAG_PHOTO.equals(current)){
            photos.getPhoto().add(characters.getData());
        } else if (Const.TAG_SEASONALITY.equals(current)){
            place.setSeasonality(SeasonalityType.fromValue(characters.getData()));
        } else if (Const.TAG_TAG.equals(current)){
            place.getTags().getTag().add(characters.getData());
        } else if (Const.TAG_CITY.equals(current)){
            place.getAddress().setCity(characters.getData());
        } else if (Const.TAG_STREET.equals(current)){
            place.getAddress().setStreet(characters.getData());
        } else if (Const.TAG_POSTAL_CODE.equals(current)){
            place.getAddress().setPostalCode(characters.getData());
        } else if (Const.TAG_NUMBER.equals(current)){
            place.getAddress().getHouseNumber().setNumber(Integer.parseInt(characters.getData()));
        } else if (Const.TAG_NUMBER_WITH_LETTER.equals(current)){
            place.getAddress().getHouseNumber().setNumberWithLetter(characters.getData());
        }
    }

    public void endElement(XMLEvent event) {
        EndElement endElement = event.asEndElement();
        String localName = endElement.getName().getLocalPart();

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

    public List<Location> getLocations() {
        return locations;
    }
}
