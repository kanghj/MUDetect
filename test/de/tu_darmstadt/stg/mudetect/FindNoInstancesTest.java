package de.tu_darmstadt.stg.mudetect;

import de.tu_darmstadt.stg.mudetect.model.AUG;
import org.junit.Test;

import java.util.List;

import static de.tu_darmstadt.stg.mudetect.model.AUGBuilder.buildAUG;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

public class FindNoInstancesTest {
    @Test
    public void ignoresNonOverlappingTarget() throws Exception {
        AUG pattern = buildAUG().withActionNode("F.foo()").build();
        AUG target = buildAUG().withActionNode("B.bar()").build();

        assertNoInstance(pattern, target);
    }

    @Test
    public void ignoresTrivialOverlap() throws Exception {
        AUG pattern = buildAUG().withDataNode("Object").build();
        AUG target = buildAUG().withDataNode("Object").build();

        assertNoInstance(pattern, target);
    }

    private void assertNoInstance(AUG pattern, AUG target) {
        List<Instance> instances = new GreedyInstanceFinder().findInstances(target, pattern);

        assertThat(instances, is(empty()));
    }
}
