package at.bfarka.sonar.qualitygate

import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Test

import static at.bfarka.sonar.qualitygate.QualityGateState.fromString
import static org.hamcrest.Matchers.equalTo
import static org.junit.Assert.assertThat

/**
 * Created by berndfarka on 27.11.14.
 */
class QualityGateStateTest {

    @Test
    public void testMapping(){
        QualityGateState.values().each { state ->
            assertThat(state, equalTo(fromString(state)))
        }

    }

}
