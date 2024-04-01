package com.smz.gymplus.domain;

import static com.smz.gymplus.domain.GymTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smz.gymplus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GymTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gym.class);
        Gym gym1 = getGymSample1();
        Gym gym2 = new Gym();
        assertThat(gym1).isNotEqualTo(gym2);

        gym2.setId(gym1.getId());
        assertThat(gym1).isEqualTo(gym2);

        gym2 = getGymSample2();
        assertThat(gym1).isNotEqualTo(gym2);
    }
}
