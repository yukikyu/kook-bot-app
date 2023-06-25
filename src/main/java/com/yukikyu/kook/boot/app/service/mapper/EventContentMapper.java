package com.yukikyu.kook.boot.app.service.mapper;

import com.yukikyu.kook.boot.app.domain.EventContent;
import com.yukikyu.kook.boot.app.service.dto.EventContentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventContent} and its DTO {@link EventContentDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventContentMapper extends EntityMapper<EventContentDTO, EventContent> {}
