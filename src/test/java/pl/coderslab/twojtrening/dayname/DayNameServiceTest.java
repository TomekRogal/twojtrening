package pl.coderslab.twojtrening.dayname;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)

class DayNameServiceTest {
    @Mock DayNameRepository dayNameRepository;
    private DayNameService underTest;

    @BeforeEach
    void setUp() {
        underTest = new DayNameService(dayNameRepository);
    }
    @Test
    void findAllDaysNames() {
        //when
        underTest.findAllDaysNames();
        //then
        verify(dayNameRepository).findAll();
    }
}