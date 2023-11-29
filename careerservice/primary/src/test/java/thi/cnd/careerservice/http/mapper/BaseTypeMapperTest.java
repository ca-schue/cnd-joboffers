package thi.cnd.careerservice.http.mapper;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import thi.cnd.careerservice.http.CommonDTOMapper;

class BaseTypeMapperTest {

    @Test
    void mappingWorksCorrectly() {
        var input = new PageImpl(
                List.of(),
                PageRequest.of(2, 10, Sort.unsorted()),
                100
        );

        var output = CommonDTOMapper.toPaginationDTO(input);

        return;
    }


}
