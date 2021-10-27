package nl.leonw.supplierservice.impl;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

// Would normally be an interface, but I don't want to add a real db at this point.
@Component
public class SupplierRepository {
    private static final Map<Long, DBSupplier> DATA = new HashMap<>();
    static {
        var tmp = LongStream.rangeClosed(0, 100)
                .mapToObj(i -> DBSupplier.builder().id(i).companyName("Company " + i).marketingName("Sexy company " + i).build())
                .collect(Collectors.toMap(DBSupplier::getId, Function.identity()));

        DATA.putAll(tmp);
    }

    public DBSupplier findById(Long id) {
        return DATA.get(id);
    }
}
