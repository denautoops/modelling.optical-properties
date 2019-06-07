package app.gaussian.models;

public class Membrane {
    private String name;
    private double thikkness;
    private double rafractIndex;

    public Membrane(String name, double thikkness, double rafractIndex) {
        this.name = name;
        this.thikkness = thikkness;
        this.rafractIndex = rafractIndex;
    }

    public String getName() {
        return name;
    }

    public double getThikkness() {
        return thikkness;
    }

    public double getRafractIndex() {
        return rafractIndex;
    }
}
