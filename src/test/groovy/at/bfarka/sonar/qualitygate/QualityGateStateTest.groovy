package at.bfarka.sonar.qualitygate

import org.junit.Test

import static at.bfarka.sonar.qualitygate.QualityGateState.NO_RESULT
import static at.bfarka.sonar.qualitygate.QualityGateState.OK
import static at.bfarka.sonar.qualitygate.QualityGateState.fromString
import static org.hamcrest.Matchers.equalTo
import static org.junit.Assert.assertThat

/**
 * Created by berndfarka on 27.11.14.
 */
class QualityGateStateTest {

    @Test
    public void testMapping() {
        assertThat(OK, equalTo(fromString("OK")))
        assertThat(QualityGateState.WARNING, equalTo(fromString("WARN")))
        assertThat(QualityGateState.ERROR, equalTo(fromString("ERROR")))

    }

    @Test
    public void testNonExistingMapp(){
        assertThat(NO_RESULT, equalTo(fromString("NOTOK")))
        assertThat(NO_RESULT, equalTo(fromString(null)))
    }

}
