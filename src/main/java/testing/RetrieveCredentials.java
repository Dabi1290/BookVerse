package testing;

import java.io.File;
import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RetrieveCredentials {
    public static String[] retrieveCredentials(String filePath) {

        try {
            String[] credentials = new String[2];

            File xmlFile = new File(filePath);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            Element rootElement = doc.getDocumentElement();

            NodeList testNodeList = rootElement.getChildNodes();
            String username = testNodeList.item(1).getTextContent();
            String password = testNodeList.item(3).getTextContent();

            credentials[0] = username;
            credentials[1] = password;

            return credentials;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
