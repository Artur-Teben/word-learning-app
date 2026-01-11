package com.artur.vocab.wordlearningapp.mapper;


import com.artur.vocab.wordlearningapp.domain.WordEntity;
import com.artur.vocab.wordlearningapp.domain.dto.CreateWordRequest;
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
