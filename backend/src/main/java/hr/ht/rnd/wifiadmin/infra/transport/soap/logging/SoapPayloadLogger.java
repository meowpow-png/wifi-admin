package hr.ht.rnd.wifiadmin.infra.transport.soap.logging;

import hr.ht.rnd.wifiadmin.common.LoggerNames;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static hr.ht.rnd.wifiadmin.common.StructuredLog.*;

final class SoapPayloadLogger {

    private static final Logger log = LoggerFactory.getLogger(LoggerNames.SOAP_PAYLOAD);

    private SoapPayloadLogger() {}

    static boolean isDisabled() {
        return !log.isTraceEnabled();
    }

    static void logRequest(String payload) {
        var request = XmlFormatter.format(payload);

        trace(log).withEvent(Event.OUTBOUND_SOAP_REQUEST)
                .withField("soap_payload", '\n' + request)
                .log();
    }

    static void logResponse(String payload) {
        var response = XmlFormatter.format(payload);

        trace(log).withEvent(Event.INBOUND_SOAP_RESPONSE)
                .withField("soap_payload", '\n' + response)
                .log();
    }

    @SuppressWarnings("HttpUrlsUsage")
    private static final class XmlFormatter {

        private static final TransformerFactory TRANSFORMER_FACTORY = TransformerFactory.newInstance();

        private static final String FEATURE_DISALLOW_DOCTYPE = "http://apache.org/xml/features/disallow-doctype-decl";
        private static final String PROPERTY_INDENT_AMOUNT = "{http://xml.apache.org/xslt}indent-amount";
        private static final String INDENT_AMOUNT = "4";

        private XmlFormatter() {}

        /**
         * Formats an XML document using indentation.
         * <p>
         * <strong>API Note:</strong>
         * If formatting fails, the payload is omitted to prevent
         * logging unredacted sensitive information.
         *
         * @param xml the XML document to format
         *
         * @throws NullPointerException if {@code xml} is {@code null}
         */
        static String format(String xml) {
            Objects.requireNonNull(xml, "xml must not be null");
            try {
                var documentFactory = DocumentBuilderFactory.newInstance();
                documentFactory.setFeature(FEATURE_DISALLOW_DOCTYPE, true);

                var builder = documentFactory.newDocumentBuilder();
                var document = builder.parse(
                        new InputSource(new StringReader(xml))
                );
                obfuscateSecrets(document);

                var transformer = TRANSFORMER_FACTORY.newTransformer();

                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(PROPERTY_INDENT_AMOUNT, INDENT_AMOUNT);
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

                var writer = new StringWriter();

                transformer.transform(
                        new DOMSource(document),
                        new StreamResult(writer)
                );
                return removeBlankLines(writer.toString());
            }
            catch (Exception e) {
                warn(log).withEvent(Event.SOAP_PAYLOAD_FORMATTING_FAILED)
                        .withCause(e)
                        .log();

                return "<SOAP payload unavailable>";
            }
        }

        private static String removeBlankLines(String text) {
            return text.lines()
                    .filter(line -> !line.isBlank())
                    .collect(Collectors.joining(System.lineSeparator()));
        }

        private static void obfuscateSecrets(Node node) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                var name = node.getNodeName();
                if (name.endsWith(":password") || name.equals("password")) {
                    node.setTextContent("********");
                }
            }
            for (var child = node.getFirstChild();
                 child != null;
                 child = child.getNextSibling()) {
                obfuscateSecrets(child);
            }
        }
    }
}
