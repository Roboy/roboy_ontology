package roboy.ontology.nodes;

import com.google.gson.Gson;
import roboy.ontology.Neo4jLabel;
import roboy.ontology.Neo4jMemoryInterface;
import roboy.ontology.Neo4jProperty;
import roboy.ontology.Neo4jRelationship;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Encapsulates a MemoryNodeModel and enables dialog states to easily store
 * and retrieve information about Roboy.
 */
public class Roboy extends MemoryNodeModel{
    private ArrayList<Neo4jRelationship> roboyLegalRelationships = new ArrayList<>();
    private ArrayList<Neo4jRelationship> roboyIllegalRelationships = new ArrayList<>();
    private ArrayList<Neo4jProperty> roboyLegalProperties = new ArrayList<>();
    private ArrayList<Neo4jProperty> roboyIllegalProperties = new ArrayList<>();

    /**
     * Initializer for the Roboy node
     */
    public Roboy(Neo4jMemoryInterface memory) {
        super(true, memory);
        this.label = Neo4jLabel.Robot.type;
        this.labels.add(this.label);
        this.Neo4jLegalRelationships = roboyLegalRelationships;
        this.Neo4jIllegalRelationships = roboyIllegalRelationships;
        this.Neo4jLegalProperties = roboyLegalProperties;
        this.Neo4jIllegalProperties = roboyIllegalProperties;
        this.InitializeRoboy();
    }

    /**
     * This method initializes the roboy property as a node that
     * is in sync with memory and represents the Roboy itself.
     *
     * If something  goes wrong during querying, Roboy stays empty
     * and soulless, and has to fallback
     */
    // TODO consider a fallback for the amnesia mode
    private void InitializeRoboy() {
        setProperty("name", "roboy");

        ArrayList<Integer> ids = new ArrayList<>();
        try {
            ids = memory.getByQuery(this);
        } catch (InterruptedException | IOException e) {
            LOGGER.error("Cannot retrieve or find Roboy in the Memory. Go the amnesia mode");
            LOGGER.error(e.getMessage());
        }
        // Pick first if matches found.
        if (ids != null && !ids.isEmpty()) {
            try {
                MemoryNodeModel node = fromJSON(memory.getById(ids.get(0)), new Gson());
                setId(node.getId());
                setRelationships(node.getRelationships() != null ? node.getRelationships() : new HashMap<>());
                setProperties(node.getProperties() != null ? node.getProperties() : new HashMap<>());
            } catch (InterruptedException | IOException e) {
                LOGGER.error("Unexpected memory error: provided ID not found upon querying. Go the amnesia mode");
                LOGGER.error(e.getMessage());
            }
        }
    }
}
