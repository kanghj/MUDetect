package de.tu_darmstadt.stg.mudetect.ranking;

import de.tu_darmstadt.stg.mudetect.aug.patterns.APIUsagePattern;
import de.tu_darmstadt.stg.mudetect.mining.Model;
import de.tu_darmstadt.stg.mudetect.model.Overlap;
import de.tu_darmstadt.stg.mudetect.model.TestAUGBuilder;
import org.junit.Before;
import org.junit.Test;

import static de.tu_darmstadt.stg.mudetect.mining.TestPatternBuilder.somePattern;
import static de.tu_darmstadt.stg.mudetect.model.TestAUGBuilder.buildAUG;
import static de.tu_darmstadt.stg.mudetect.model.TestOverlapBuilder.buildOverlap;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static utils.SetUtils.asSet;

public class PatternSameSizeSupportWeightFunctionTest {
    private APIUsagePattern pattern;
    private Model model;
    private Overlap violation;

    @Before
    public void setup() {
        TestAUGBuilder aTargetBuilder = buildAUG().withActionNode("a");

        TestAUGBuilder patternBuilder = buildAUG().withActionNodes("a", "b");
        pattern = somePattern(patternBuilder);
        model = () -> asSet(pattern);

        violation = buildOverlap(aTargetBuilder, patternBuilder).withNode("a", "a").build();
    }

    @Test
    public void calculatesPatternSupportWeight_noEquallySizedPattern() throws Exception {
        ViolationWeightFunction weightFunction = new PatternSameSizeSupportWeightFunction();

        double weight = weightFunction.getWeight(violation, null, model);

        assertThat(weight, is(1.0));
    }

    @Test
    public void calculatesPatternSupportWeight_equallySizedPatternWithLargerSupport() throws Exception {
        APIUsagePattern pattern2 = somePattern(pattern, pattern.getSupport() * 2);
        model = () -> asSet(pattern, pattern2);
        ViolationWeightFunction weightFunction = new PatternSameSizeSupportWeightFunction();

        double weight = weightFunction.getWeight(violation, null, model);

        assertThat(weight, is(0.5));
    }

    @Test
    public void calculatesPatternSupportWeight_equallySizedPatternWithSmallerSupport() throws Exception {
        APIUsagePattern pattern2 = somePattern(pattern, 1);
        model = () -> asSet(pattern, pattern2);
        ViolationWeightFunction weightFunction = new PatternSameSizeSupportWeightFunction();

        double weight = weightFunction.getWeight(violation, null, model);

        assertThat(weight, is(1.0));
    }

}
