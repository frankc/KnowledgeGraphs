import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;


public class RDFSInferenceWithLocalName {

    public static void main(String[] args) {
        // File path to the .ttl file
        String filePath = "LLM.fact2V2";

        // Create an empty model
        Model model = ModelFactory.createDefaultModel();

        // Read the .ttl file into the model
        try (InputStream in = RDFDataMgr.open(filePath)) {
            model.read(in, null, "TTL");
        } catch (Exception e) {
            System.err.println("Error reading the file: " + e.getMessage());
            return;
        }

        // Perform RDFS inferencing
        Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
        InfModel infModel = ModelFactory.createInfModel(reasoner, model);

        /*
        // Display triples in the default namespace as local names
        displayTriplesWithLocalNames(infModel);

        // display Rollo info
        displaySubjectInformation(infModel, "AutoGen");
        displaySubjectInformation(infModel, "GPT");
        displaySubjectInformation(infModel, "Rollo");
        */


        // Filter statements based on a specific namespace
        String cs = "http://codesupreme.com/";
        Model filteredModel = ModelFactory.createDefaultModel();
        ExtendedIterator<Statement> iter = infModel.listStatements();
        while (iter.hasNext()) {
            Statement stmt = iter.next();
            String subjectURI = stmt.getSubject().getURI();
            if (subjectURI != null && subjectURI.startsWith(cs)) {
                filteredModel.add(stmt);
            }
        }

        /* Write out the model as TTL
        The RDFDataMgr.write function is used to serialize the model to the TURTLE_PRETTY format,
        which is a variant of Turtle that aims for a more human-readable output. If you have multiple prefixes,
                you can define them in the same way using model.setNsPrefix(prefix, namespace) for each one.
        */

        String filename = "output.ttl";

        // define prefixes for url  !!
        filteredModel.setNsPrefix("cs", "http://codesupreme.com/" );
        filteredModel.setNsPrefix("schema", "http://www.schema.org/" );
        filteredModel.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#" );
        filteredModel.setNsPrefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#" );



        try {
            OutputStream outputStream = new FileOutputStream(filename);
            RDFDataMgr.write(outputStream, filteredModel, RDFFormat.TURTLE_PRETTY);
            System.out.println("Model has been written as Turtle to " + filename);
        } catch (Exception e) {
            System.err.println("Error writing model: " + e.getMessage());
        }
    }






    private static void displaySubjectInformation(Model model, String subjectName) {
        // Find the resource representing the subject
        Resource subject = model.getResource(model.getNsPrefixURI("") + subjectName);

        // Check if the subject exists
        if (subject == null || !subject.isResource()) {
            System.out.println("Subject '" + subjectName + "' not found in the model.");
            return;
        }

        // Create a new model to hold information about the subject
        Model subjectModel = ModelFactory.createDefaultModel();
        subjectModel.add(subject.listProperties());

        // Set prefix mappings for the subjectModel
        subjectModel.setNsPrefixes(model.getNsPrefixMap());

        // Write the subjectModel in Turtle syntax
        subjectModel.write(System.out, "Turtle");
    }

    private static void displayTriplesWithLocalNames(Model model) {
        StmtIterator stmtIterator = model.listStatements();
        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.nextStatement();
            String subject = getLocalName(stmt.getSubject());
            String predicate = getLocalName(stmt.getPredicate());
            //String object = getLocalName(stmt.getObject());
            String object = String.valueOf(stmt.getObject());
            System.out.println(subject + " " + predicate + " " + object);

        }
    }

    private static String getLocalName(Resource resource) {
        if (resource.isAnon()) {
            return "_:" + resource.getId().getLabelString();
        } else {
            String uri = resource.getURI();
            if (uri != null && uri.contains("#")) {
                return uri.substring(uri.lastIndexOf('#') + 1);
            } else {
                return resource.getLocalName();
            }
        }
    }
}

