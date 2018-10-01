package input;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Automaton {
    public final String name;
    public final List<State> states;
    public final List<Event> events;
    public final List<Transition> transitions;

    private Automaton(String name, List<State> states, List<Event> events, List<Transition> transitions) {
        this.name = name;
        this.states = states;
        this.events = events;
        this.transitions = transitions;
    }

    public static Automaton fromXml(String xmlFileName) throws ParserConfigurationException, IOException, SAXException {
        final File file = new File(xmlFileName);
        final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
        doc.getDocumentElement().normalize();

        final String name = doc.getElementsByTagName("name").item(0).getTextContent();
        final Element stateMachine = (Element) doc.getElementsByTagName("Statemachine").item(0);

        final List<Event> events = elementsStream(stateMachine.getElementsByTagName("event"))
                .map(e -> new Event(e.getAttribute("name"), e.getAttribute("comment"))).collect(Collectors.toList());

        final NodeList widgets = doc.getElementsByTagName("widget");
        final List<State> states = elementsStream(widgets).filter(e -> e.getAttribute("type").equals("State")).map(e -> {
            final int id = Integer.valueOf(e.getAttribute("id"));
            final Element attributes = (Element) elementByName(e, "attributes");
            final String stateName = elementByName(attributes, "name").getTextContent();
            final String stateType = elementByName(attributes, "type").getTextContent();

            final List<Integer> incoming = elementsStream(attributes.getElementsByTagName("incoming"))
                    .map(ee -> Integer.valueOf(ee.getAttribute("id"))).collect(Collectors.toList());
            final List<Integer> outgoing = elementsStream(attributes.getElementsByTagName("outgoing"))
                    .map(ee -> Integer.valueOf(ee.getAttribute("id"))).collect(Collectors.toList());

            return new State(id, stateName, Integer.valueOf(stateType), incoming, outgoing);
        }).collect(Collectors.toList());
        final List<Transition> transitions = elementsStream(widgets).filter(e -> e.getAttribute("type").equals("Transition")).map(e -> {
            final int id = Integer.valueOf(e.getAttribute("id"));
            final Element attributes = (Element) elementByName(e, "attributes");

            final Element eventElement = (Element) elementByName(attributes, "event");
            final String event = eventElement.getAttribute("name");
            final String comment = eventElement.getAttribute("comment");

            final String code = elementByName(attributes, "code").getTextContent();
            final String guard = elementByName(attributes, "guard").getTextContent();

            final List<String> actions = elementsStream(attributes.getElementsByTagName("action"))
                    .map(ee -> ee.getAttribute("name")).collect(Collectors.toList());

            return new Transition(id, event, comment, guard, actions, code);
        }).collect(Collectors.toList());
        return new Automaton(name, states, events, transitions);
    }

    private static Stream<Element> elementsStream(NodeList nodeList) {
        final ArrayList<Element> res = new ArrayList<>(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            final Node item = nodeList.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                res.add((Element) item);
            }
        }
        return res.stream();
    }

    private static Node elementByName(Element attributes, String name) {
        return attributes.getElementsByTagName(name).item(0);
    }
}
