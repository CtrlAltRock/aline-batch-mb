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

        UserBase user = testUser.get();

        assertEquals(1L, user.getId());
        assertNotNull(user.getFirstName());
        assertNotNull(user.getMiddleName());
        assertNotNull(user.getDateOfBirth());
        assertNotNull(user.getPhone());
    }

    @Test
    public void correctCardGeneration() {
        Optional<CardBase> testCard = cardGenerator.generateCard(1, 2);

        assertTrue(testCard.isPresent());

        CardBase card = testCard.get();

        assertEquals(1L, card.getId());
        assertEquals(1L, card.getUser());
        assertNotNull(card.getCardNumber());
    }

    @Test
    public void correctMerchantGeneration() {
        MerchantDto mer = new MerchantDto(1, "Chicago", "IL", "60465", "111");
        Optional<MerchantBase> testMerch = merchantGenerator.generateMerchant(mer);

        assertTrue(testMerch.isPresent());

        MerchantBase merch = testMerch.get();

        assertEquals(1L, merch.getId());
        assertNotNull(merch.getName());
        assertEquals("Chicago", merch.getCity());
        assertEquals("IL", merch.getState());
    }

    @Test
    public void correctStateCollect() {
        Optional<StateBase> testState = stateGenerator.getState("IL");

        assertTrue(testState.isPresent());

        StateBase state = testState.get();

        assertEquals("Illinois", state.getName());
        assertEquals("Springfield", state.getCapital());
    }
}
