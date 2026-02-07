@XmlSchema(
        namespace = "com.banew/home-dear-home/storage",
        elementFormDefault = XmlNsForm.QUALIFIED,
        xmlns = {
                @XmlNs(prefix = "hdh", namespaceURI = "com.banew/home-dear-home/storage")
        }
)
package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.XmlNs;
import jakarta.xml.bind.annotation.XmlNsForm;
import jakarta.xml.bind.annotation.XmlSchema;