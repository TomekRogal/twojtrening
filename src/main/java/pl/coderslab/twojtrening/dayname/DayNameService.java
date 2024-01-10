package pl.coderslab.twojtrening.dayname;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DayNameService {
    private final DayNameRepository dayNameRepository;

    public DayNameService(DayNameRepository dayNameRepository) {
        this.dayNameRepository = dayNameRepository;
    }

    public List<DayName> findAllDaysNames() {
        return dayNameRepository.findAll();
    }
}
