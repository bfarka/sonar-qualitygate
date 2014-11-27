package at.bfarka.sonar.qualitygate

import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Test

/**
 * Created by berndfarka on 27.11.14.
 */
class QualityGateStateTest {

    @Test
    public void testMapping(){
        QualityGateState.values().each { state ->
            Assert.assertThat(state, Matchers.equalTo(QualityGateState.fromString(state)))
        }

    }

}
