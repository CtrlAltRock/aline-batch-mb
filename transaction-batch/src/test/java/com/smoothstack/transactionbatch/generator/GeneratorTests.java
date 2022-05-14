package com.smoothstack.transactionbatch.generator;

import com.smoothstack.transactionbatch.dto.MerchantDto;
import com.smoothstack.transactionbatch.model.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

@SpringBootTest
public class GeneratorTests {
    private final UserGenerator userGenerator = UserGenerator.getInstance();
    private final CardGenerator cardGenerator = CardGenerator.getInstance();
    private final MerchantGenerator merchantGenerator = MerchantGenerator.getInstance();
    private final StateGenerator stateGenerator = StateGenerator.getInstance();
    
    @Test
    public void correctUserGeneration() {
        Optional<UserBase> testUser = userGenerator.generateUser(1);

        assertTrue(testUser.isPresent());
    }

    @Test
    public void correctCardGeneration() {
        Optional<CardBase> testCard = cardGenerator.generateCard(1, 2);

        assertTrue(testCard.isPresent());
    }

    @Test
    public void correctMerchantGeneration() {
        MerchantDto mer = new MerchantDto(1, "Chicago", "IL", "60465", 111);
        Optional<MerchantBase> testMerch = merchantGenerator.generateMerchant(mer);

        assertTrue(testMerch.isPresent());
    }

    @Test
    public void correctStateCollect() {
        Optional<StateBase> testState = stateGenerator.getState("IL");

        assertTrue(testState.isPresent());
    }
}
