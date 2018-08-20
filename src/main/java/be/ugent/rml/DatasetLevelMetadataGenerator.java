package be.ugent.rml;

import be.ugent.rml.cli.Main;
import be.ugent.rml.store.QuadStore;
import be.ugent.rml.store.SimpleQuadStore;
import be.ugent.rml.store.TriplesQuads;
import be.ugent.rml.term.*;

import java.util.ArrayList;
import java.util.List;
import java.time.Instant;

/**
 * Unique class -- reusable outside of the mapper
 */
public class DatasetLevelMetadataGenerator {

    public static void createMetadata(Term rdfDataset, Term rdfDatasetGeneration, Term rmlProcessor,
                                           QuadStore result, List<Term> logicalSources,
                                           String startTimestamp, String stopTimestamp, String mappingFile) {
        // <#RDF_Dataset>
        result.addTriple(rdfDataset, new NamedNode(NAMESPACES.RDF + "type"),
                new NamedNode(NAMESPACES.PROV + "Entity"));
        result.addTriple(rdfDataset, new NamedNode(NAMESPACES.RDF + "type"),
                new NamedNode(NAMESPACES.VOID + "Dataset"));
        result.addTriple(rdfDataset, new NamedNode(NAMESPACES.PROV + "generatedAtTime"),
                new Literal(Instant.now().toString(), new NamedNode(NAMESPACES.XSD + "dateTime")));
        result.addTriple(rdfDataset, new NamedNode(NAMESPACES.PROV + "wasGeneratedBy"),
                rdfDatasetGeneration);
        result.addTriple(rdfDataset, new NamedNode(NAMESPACES.PROV + "wasAssociatedWith"),
                rmlProcessor);

        // <#RMLProcessor>
        result.addTriple(rmlProcessor, new NamedNode(NAMESPACES.RDF + "type"),
                new NamedNode(NAMESPACES.PROV + "Agent"));
        result.addTriple(rmlProcessor, new NamedNode(NAMESPACES.PROV + "type"),
                new NamedNode(NAMESPACES.PROV + "SoftwareAgent"));

        createRDFdataset_GenerationMetadata(rdfDataset, rdfDatasetGeneration, result, mappingFile, startTimestamp, stopTimestamp);

        for (Term logicalSource: logicalSources) {
            result.addTriple(rdfDataset, new NamedNode(NAMESPACES.PROV + "wasDerivedFrom"),
                    logicalSource);
            result.addTriple(rdfDatasetGeneration, new NamedNode(NAMESPACES.PROV + "used"),
                    logicalSource);
        }
    }

    public static void createRDFdataset_GenerationMetadata(Term rdfDataset, Term rdfDatasetGeneration, QuadStore result,
                                                           String mappingFile, String startTimestamp, String stopTimestamp) {
        // <#RDFdataset_Generation>
        result.addTriple(rdfDatasetGeneration, new NamedNode(NAMESPACES.RDF + "type"),
                new NamedNode(NAMESPACES.PROV + "Activity"));
        result.addTriple(rdfDatasetGeneration, new NamedNode(NAMESPACES.PROV + "generated"),
                rdfDataset);
        result.addTriple(rdfDatasetGeneration, new NamedNode(NAMESPACES.PROV + "startedAtTime"),
                new Literal(startTimestamp, new NamedNode(NAMESPACES.XSD + "dateTime")));
        result.addTriple(rdfDatasetGeneration, new NamedNode(NAMESPACES.PROV + "endedAtTime"),
                new Literal(stopTimestamp, new NamedNode(NAMESPACES.XSD + "dateTime")));
        result.addTriple(rdfDatasetGeneration, new NamedNode(NAMESPACES.PROV + "used"),
                new NamedNode(String.format("file:%s", mappingFile)));
    }
}
