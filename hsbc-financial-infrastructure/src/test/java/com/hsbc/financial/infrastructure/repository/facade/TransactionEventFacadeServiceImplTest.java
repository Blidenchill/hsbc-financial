package com.hsbc.financial.infrastructure.repository.facade;

import com.hsbc.financial.domain.transaction.entity.TransactionEvent;
import com.hsbc.financial.infrastructure.repository.TransactionEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hsbc.financial.domain.common.exception.InfrastructureException;
import org.mockito.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
/**
 * This class is used for unit testing the TransactionEventFacadeServiceImpl class.(Generated by JoyCoder)
 * @author: zhaoyongping8
 * @date: 2025-01-12
 */
@ExtendWith(MockitoExtension.class)
public class TransactionEventFacadeServiceImplTest {

	@Mock
	private TransactionEventRepository transactionEventRepository;

	@InjectMocks
	private TransactionEventFacadeServiceImpl transactionEventFacadeServiceImpl;


    @Test
    public void testSaveSuccess() {
        TransactionEvent transactionEvent = new TransactionEvent();
        
        Mockito.when(transactionEventRepository.save(any(TransactionEvent.class))).thenReturn(transactionEvent);
        transactionEventFacadeServiceImpl.save(transactionEvent);
        
    }

    @Test
    public void testSaveException() {
        // generated by JoyCoder taskId 0bbe0ff0094c
        TransactionEvent transactionEvent = new TransactionEvent();
        
        Mockito.when(transactionEventRepository.save(any(TransactionEvent.class))).thenThrow(new RuntimeException());

        assertThrows(InfrastructureException.class, () -> {
            transactionEventFacadeServiceImpl.save(transactionEvent);
        });
    }

    @Test
    void testFindByTransactionId_Success() {
        String transactionId = "testId";
        TransactionEvent expectedEvent = new TransactionEvent();
        when(transactionEventRepository.findByTransactionId(transactionId)).thenReturn(Optional.of(expectedEvent));

        Optional<TransactionEvent> actualEvent = transactionEventFacadeServiceImpl.findByTransactionId(transactionId);

        assertThat(actualEvent.isPresent()).isTrue();
        assertEquals(expectedEvent, actualEvent.get());
        verify(transactionEventRepository, times(1)).findByTransactionId(transactionId);
    }

}