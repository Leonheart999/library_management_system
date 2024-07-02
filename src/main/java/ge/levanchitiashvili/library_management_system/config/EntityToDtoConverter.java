package ge.levanchitiashvili.library_management_system.config;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

@RequiredArgsConstructor
public class EntityToDtoConverter<E,D> {
    @Autowired
    private ModelMapper modelMapper;
    public List<D> ENTITY_DTO_List(List<E> entities) {
        return entities.stream().map(this::ENTITY_DTO).toList();
    }

    public Page<D> ENTITY_DTO_PAGE(Page<E> entitiesPage) {
        return new PageImpl<>(entitiesPage.stream().map(this::ENTITY_DTO).toList());
    }

    public D ENTITY_DTO(E entity) {
        return modelMapper.map(entity, (Class<D>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[1]);
    }
}
