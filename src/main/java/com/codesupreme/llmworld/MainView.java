package com.codesupreme.llmworld;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;

@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        add(new H1("Hello LLM World"));

        String turtleFilePath = "src/main/resources/static/LLM.facts.ttl";

        Model model = ModelFactory.createDefaultModel();

        RDFDataMgr.read(model, turtleFilePath);

        for (StmtIterator it = model.listStatements(); it.hasNext(); ) {
            String s = String.valueOf(it.next());
            add(new Paragraph(s));
        }
    }
}
