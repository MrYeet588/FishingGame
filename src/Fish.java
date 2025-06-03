public class Fish {
    private double baseWeight;
    private Mutation mutation;
    private double weight;


    public Fish (double weight, Mutation mutation) {
        this.weight = weight;
        this.mutation = mutation;
    }


    public double getBaseWeight() {
        return baseWeight;
    }


    public Mutation getMutation() {
        return mutation;
    }


    public double getSellValue(){
        return baseWeight * weight * mutation.getMultiplier();
    }
}
