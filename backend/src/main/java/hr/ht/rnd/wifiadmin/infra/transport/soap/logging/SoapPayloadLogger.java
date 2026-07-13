package hr.ht.rnd.wifiadmin.infra.transport.soap.logging;

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

final class SoapPayloadLogger {

    private static final String NAME = "hr.ht.rnd.wifiadmin.infra.transport.soap.payload";
    private static final Logger log = LoggerFactory.getLogger(NAME);

    private SoapPayloadLogger() {}

    static boolean isDisabled() {
        return !log.isTraceEnabled();
    }

    static void logRequest(String payload) {
        log.trace("Outbound SOAP request:\n{}\n{}\n{}",
                "==================== SOAP Request ====================",
                XmlFormatter.format(payload),
                "======================================================="
        );
    }

    static void logResponse(String payload) {
        log.trace("Inbound SOAP response:\n{}\n{}\n{}",
                "==================== SOAP Response ====================",
                XmlFormatter.format(payload),
                "======================================================="
        );
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
         * If formatting fails, the original XML is returned unchanged.
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
                return xml;
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
