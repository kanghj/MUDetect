package de.tu_darmstadt.stg.mudetect;

import de.tu_darmstadt.stg.mudetect.model.Instance;

import java.util.function.Predicate;

public class InstanceSizePredicate implements Predicate<Instance> {
    private final double overlapRatioThreshold;

    public InstanceSizePredicate(double overlapRatioThreshold) {
        this.overlapRatioThreshold = overlapRatioThreshold;
    }

    @Override
    public boolean test(Instance instance) {
        return instance.getNodeSize() / (float) instance.getPattern().getNodeSize() > overlapRatioThreshold;
    }
}
