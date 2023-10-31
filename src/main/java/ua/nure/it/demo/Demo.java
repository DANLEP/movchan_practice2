package ua.nure.it.demo;

import ua.nure.it.demo.parser.*;

public class Demo {
    public static void main(String[] args) throws Exception{
//        DOMParser.main(new String[] {});
//        SAXParser.main(new String[] {});
//        StaxParser.main(new String[] {});
//        JAXBParser.main(new String[] {});

        XSLTransform.main(new String[] { "locations.xsl", "locations.xsd.xml", "locations.html" });
    }
}
