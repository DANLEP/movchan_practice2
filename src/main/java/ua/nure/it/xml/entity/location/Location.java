//
// This file was generated by the Eclipse Implementation of JAXB, v3.0.0 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.10.29 at 09:32:23 PM EET 
//


package ua.nure.it.xml.entity.location;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import ua.nure.it.xml.entity.Entity;
import ua.nure.it.xml.entity.place.CoordinateType;
import ua.nure.it.xml.entity.place.Place;


/**
 * <p>Java class for Location complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Location"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://it.nure.ua/xml/entity/}Entity"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="title" type="{http://it.nure.ua/xml/entity/place/}Name"/&gt;
 *         &lt;element name="area"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="coordinate" type="{http://it.nure.ua/xml/entity/place/}CoordinateType" maxOccurs="unbounded" minOccurs="3"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="places"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="place" type="{http://it.nure.ua/xml/entity/place/}Place" maxOccurs="unbounded"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Location", propOrder = {
    "title",
    "area",
    "places"
})
public class Location
    extends Entity
{

    @XmlElement(required = true)
    protected String title;
    @XmlElement(required = true)
    protected Location.Area area;
    @XmlElement(required = true)
    protected Location.Places places;

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the area property.
     * 
     * @return
     *     possible object is
     *     {@link Location.Area }
     *     
     */
    public Location.Area getArea() {
        return area;
    }

    /**
     * Sets the value of the area property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location.Area }
     *     
     */
    public void setArea(Location.Area value) {
        this.area = value;
    }

    /**
     * Gets the value of the places property.
     * 
     * @return
     *     possible object is
     *     {@link Location.Places }
     *     
     */
    public Location.Places getPlaces() {
        return places;
    }

    /**
     * Sets the value of the places property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location.Places }
     *     
     */
    public void setPlaces(Location.Places value) {
        this.places = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="coordinate" type="{http://it.nure.ua/xml/entity/place/}CoordinateType" maxOccurs="unbounded" minOccurs="3"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "coordinate"
    })
    public static class Area {

        @XmlElement(required = true)
        public List<CoordinateType> coordinate;

        /**
         * Gets the value of the coordinate property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the Jakarta XML Binding object.
         * This is why there is not a <CODE>set</CODE> method for the coordinate property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCoordinate().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CoordinateType }
         * 
         * 
         */
        public List<CoordinateType> getCoordinate() {
            if (coordinate == null) {
                coordinate = new ArrayList<CoordinateType>();
            }
            return this.coordinate;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="place" type="{http://it.nure.ua/xml/entity/place/}Place" maxOccurs="unbounded"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "place"
    })
    public static class Places {

        @XmlElement(required = true)
        public List<Place> place;

        /**
         * Gets the value of the place property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the Jakarta XML Binding object.
         * This is why there is not a <CODE>set</CODE> method for the place property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPlace().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Place }
         * 
         * 
         */
        public List<Place> getPlace() {
            if (place == null) {
                place = new ArrayList<Place>();
            }
            return this.place;
        }

    }

}
