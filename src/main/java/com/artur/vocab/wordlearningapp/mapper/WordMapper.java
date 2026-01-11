package com.artur.vocab.wordlearningapp.mapper;


import com.artur.vocab.wordlearningapp.domain.entity.WordEntity;
import com.artur.vocab.wordlearningapp.web.dto.CreateWordRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WordMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "text", source = "text")
    @Mapping(target = "context", source = "context")
    WordEntity toEntity(CreateWordRequest request);
}
