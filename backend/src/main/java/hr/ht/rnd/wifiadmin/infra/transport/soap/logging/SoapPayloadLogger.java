package hr.ht.rnd.wifiadmin.infra.transport.soap.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

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

    private static final class XmlFormatter {

        private static final TransformerFactory FACTORY = TransformerFactory.newInstance();

        @SuppressWarnings("HttpUrlsUsage")
        private static final String INDENT_AMOUNT_PROPERTY = "{http://xml.apache.org/xslt}indent-amount";
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
                var transformer = FACTORY.newTransformer();

                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(INDENT_AMOUNT_PROPERTY, INDENT_AMOUNT);
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

                var writer = new StringWriter();

                transformer.transform(
                        new StreamSource(new StringReader(xml)),
                        new StreamResult(writer)
                );
                return removeBlankLines(writer.toString());
            }
            catch (TransformerException e) {
                return xml;
            }
        }

        private static String removeBlankLines(String text) {
            return text.lines()
                    .filter(line -> !line.isBlank())
                    .collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
