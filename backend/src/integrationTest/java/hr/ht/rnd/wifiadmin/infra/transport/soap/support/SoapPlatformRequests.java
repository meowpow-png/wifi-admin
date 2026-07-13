package hr.ht.rnd.wifiadmin.infra.transport.soap.support;

import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SOAP request assertions for the mock platform.
 */
public final class SoapPlatformRequests {

    private SoapPlatformRequests() {}

    public static void assertRetrieveRequest(String body, String cpeId) {
        var document = parse(body);

        assertThat(hasElement(document, "Envelope")).as(body).isTrue();
        assertThat(hasElement(document, "Body")).as(body).isTrue();
        assertThat(hasElement(document, "GetCpeIdRequest")).as(body).isTrue();
        assertThat(text(document, "cpeId")).isEqualTo(cpeId);
    }

    public static void assertUpdateRequest(
            String body,
            WifiConfiguration configuration
    ) {
        var document = parse(body);

        assertThat(hasElement(document, "Envelope")).as(body).isTrue();
        assertThat(hasElement(document, "Body")).as(body).isTrue();
        assertThat(hasElement(document, "UpdateCpeIdRequest")).as(body).isTrue();
        assertThat(text(document, "cpeId")).isEqualTo(configuration.cpeId());
        assertThat(text(document, "wifiBand")).isEqualTo(configuration.wifiBand().name());
        assertThat(text(document, "ssid")).isEqualTo(configuration.ssid());
        assertThat(text(document, "encryptionType")).isEqualTo(configuration.encryptionType().name());

        var password = configuration.password();
        if (password != null) {
            assertThat(text(document, "password")).isEqualTo(password.value());
        }
    }

    @SuppressWarnings("HttpUrlsUsage")
    private static Document parse(String body) {
        try {
            var factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

            var builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(body)));
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            throw new AssertionError("SOAP request body is not valid XML", e);
        }
    }

    private static boolean hasElement(Document document, String localName) {
        return firstElement(document, localName) != null;
    }

    private static String text(Document document, String localName) {
        var element = firstElement(document, localName);

        assertThat(element).as("SOAP element <%s>", localName).isNotNull();
        return element.getTextContent();
    }

    private static Element firstElement(Document document, String localName) {
        var elements = document.getElementsByTagNameNS("*", localName);
        if (elements.getLength() > 0) {
            return (Element) elements.item(0);
        }
        return firstElementByNodeName(document.getDocumentElement(), localName);
    }

    private static Element firstElementByNodeName(Node node, String localName) {
        if (node instanceof Element element && localName.equals(unprefixed(element.getNodeName()))) {
            return element;
        }
        var children = node.getChildNodes();
        for (var i = 0; i < children.getLength(); i++) {
            var element = firstElementByNodeName(children.item(i), localName);
            if (element != null) {
                return element;
            }
        }
        return null;
    }

    private static String unprefixed(String nodeName) {
        var separator = nodeName.indexOf(':');
        if (separator < 0) {
            return nodeName;
        }
        return nodeName.substring(separator + 1);
    }
}
