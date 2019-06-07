package app.gaussian.models;

import java.util.Map;

public class MembraneStructure {
    private int membraneCounts;
    private Map<Integer, Membrane> structure;

    public MembraneStructure(int membraneCounts, Map<Integer, Membrane> structure) {
        this.membraneCounts = membraneCounts;
        this.structure = structure;
    }

    public int getMembraneCounts() {
        return membraneCounts;
    }

    public Map<Integer, Membrane> getStructure() {
        return structure;
    }
}
