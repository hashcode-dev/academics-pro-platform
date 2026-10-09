package com.hashcode.invoicely;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoOperations;

@SpringBootTest
class InvoicelyApplicationTests {

    @MockBean
    private MongoOperations mongoOperations;

    @Test
    void contextLoads() {
    }

}
