//
// This file was generated by the Eclipse Implementation of JAXB, v3.0.0 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.10.29 at 09:32:23 PM EET 
//


package ua.nure.it.xml.entity.location;

import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ua.nure.it.xml.entity.location package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ua.nure.it.xml.entity.location
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Location }
     * 
     */
    public Location createLocation() {
        return new Location();
    }

    /**
     * Create an instance of {@link Locations }
     * 
     */
    public Locations createLocations() {
        return new Locations();
    }

    /**
     * Create an instance of {@link Location.Area }
     * 
     */
    public Location.Area createLocationArea() {
        return new Location.Area();
    }

    /**
     * Create an instance of {@link Location.Places }
     * 
     */
    public Location.Places createLocationPlaces() {
        return new Location.Places();
    }

}
